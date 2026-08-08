package io.yak.framework.workflow.engine.api;

import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.NodeTimeoutPolicy;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.event.WorkflowEvent;
import io.yak.framework.workflow.engine.event.WorkflowEventListener;
import io.yak.framework.workflow.engine.execution.NodeAttempt;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.graph.WorkflowDefinitionValidator;
import io.yak.framework.workflow.engine.graph.WorkflowGraph;
import io.yak.framework.workflow.engine.graph.WorkflowGraphBuilder;
import io.yak.framework.workflow.engine.policy.DefaultFailurePropagationPolicy;
import io.yak.framework.workflow.engine.policy.DefaultRetryDecider;
import io.yak.framework.workflow.engine.policy.DefaultTriggerRuleEvaluator;
import io.yak.framework.workflow.engine.policy.FailureHandlingResult;
import io.yak.framework.workflow.engine.policy.FailurePropagationPolicy;
import io.yak.framework.workflow.engine.policy.RetryDecider;
import io.yak.framework.workflow.engine.scheduler.DefaultReadyNodeResolver;
import io.yak.framework.workflow.engine.scheduler.DefaultWorkflowScheduler;
import io.yak.framework.workflow.engine.scheduler.WorkflowCompletionResolver;
import io.yak.framework.workflow.engine.scheduler.WorkflowScheduler;
import io.yak.framework.workflow.engine.spi.ExecutionLock;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import io.yak.framework.workflow.engine.spi.IdGenerator;
import io.yak.framework.workflow.engine.spi.NodeCancellation;
import io.yak.framework.workflow.engine.spi.NodeDispatch;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.spi.WorkflowDefinitionRepository;
import io.yak.framework.workflow.engine.state.NodeAttemptFailureReason;
import io.yak.framework.workflow.engine.state.NodeAttemptStatus;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import io.yak.framework.workflow.engine.support.InMemoryExecutionRepository;
import io.yak.framework.workflow.engine.support.InMemoryWorkflowDefinitionRepository;
import io.yak.framework.workflow.engine.support.LocalExecutionLock;
import io.yak.framework.workflow.engine.support.UuidIdGenerator;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class DefaultWorkflowEngine implements WorkflowEngine {

    private final WorkflowDefinitionRepository definitionRepository;
    private final ExecutionRepository executionRepository;
    private final NodeExecutor nodeExecutor;
    private final ExecutionLock executionLock;
    private final IdGenerator idGenerator;
    private final Clock clock;
    private final WorkflowEventListener eventListener;
    private final WorkflowDefinitionValidator validator;
    private final WorkflowGraphBuilder graphBuilder;
    private final WorkflowScheduler scheduler;
    private final WorkflowCompletionResolver completionResolver;
    private final RetryDecider retryDecider;
    private final FailurePropagationPolicy failurePropagationPolicy;

    public DefaultWorkflowEngine(
            WorkflowDefinitionRepository definitionRepository,
            ExecutionRepository executionRepository,
            NodeExecutor nodeExecutor,
            ExecutionLock executionLock,
            IdGenerator idGenerator,
            Clock clock,
            WorkflowEventListener eventListener) {
        this.definitionRepository = definitionRepository;
        this.executionRepository = executionRepository;
        this.nodeExecutor = nodeExecutor;
        this.executionLock = executionLock;
        this.idGenerator = idGenerator;
        this.clock = clock;
        this.eventListener = eventListener;
        this.validator = new WorkflowDefinitionValidator();
        this.graphBuilder = new WorkflowGraphBuilder();
        this.scheduler = new DefaultWorkflowScheduler(
                new DefaultReadyNodeResolver(new DefaultTriggerRuleEvaluator()));
        this.completionResolver = new WorkflowCompletionResolver();
        this.retryDecider = new DefaultRetryDecider();
        this.failurePropagationPolicy = new DefaultFailurePropagationPolicy();
    }

    public static DefaultWorkflowEngine inMemory(NodeExecutor nodeExecutor) {
        return inMemory(nodeExecutor, WorkflowEventListener.noop());
    }

    public static DefaultWorkflowEngine inMemory(
            NodeExecutor nodeExecutor,
            WorkflowEventListener eventListener) {
        return inMemory(nodeExecutor, eventListener, Clock.systemUTC());
    }

    public static DefaultWorkflowEngine inMemory(
            NodeExecutor nodeExecutor,
            WorkflowEventListener eventListener,
            Clock clock) {
        return new DefaultWorkflowEngine(
                new InMemoryWorkflowDefinitionRepository(),
                new InMemoryExecutionRepository(),
                nodeExecutor,
                new LocalExecutionLock(),
                new UuidIdGenerator(),
                clock,
                eventListener);
    }

    @Override
    public void registerDefinition(WorkflowDefinition definition) {
        validator.validate(definition);
        definitionRepository.save(definition);
    }

    @Override
    public WorkflowExecution start(String definitionId, Map<String, Object> input) {
        WorkflowDefinition definition = requireDefinition(definitionId);
        return createAndStartExecution(definition, input, null, null);
    }

    @Override
    public WorkflowExecution acknowledgeNodeStarted(
            String executionId, String nodeId, String attemptId) {
        return executionLock.execute(executionId, () -> {
            WorkflowExecution execution = requireExecution(executionId);
            NodeExecution node = execution.node(nodeId);
            if (!shouldApplyStartCallback(node, attemptId)) {
                return execution.copy();
            }
            ensureRunning(execution);
            node.markRunning(now());
            execution.touch(now());
            save(execution);
            publish(WorkflowEvent.Type.NODE_STARTED, executionId, nodeId, attemptId, null);
            return execution.copy();
        });
    }

    @Override
    public WorkflowExecution completeNode(
            String executionId,
            String nodeId,
            String attemptId,
            Map<String, Object> output) {
        return executionLock.execute(executionId, () -> {
            WorkflowExecution execution = requireExecution(executionId);
            NodeExecution node = execution.node(nodeId);
            if (!shouldApplyTerminalCallback(node, attemptId)) {
                return execution.copy();
            }
            ensureRunning(execution);
            WorkflowDefinition definition = requireDefinition(execution.definitionId());
            WorkflowGraph graph = graphBuilder.build(definition);
            node.markSuccess(output, now());
            publish(WorkflowEvent.Type.NODE_SUCCEEDED, executionId, nodeId, attemptId, null);
            List<NodeExecution> ready = scheduler.advance(
                    definition, graph, execution, graph.successors(nodeId));
            dispatchReady(definition, execution, ready);
            finishIfPossible(execution);
            save(execution);
            return execution.copy();
        });
    }

    @Override
    public WorkflowExecution failNode(
            String executionId,
            String nodeId,
            String attemptId,
            String errorMessage) {
        return executionLock.execute(executionId, () -> {
            WorkflowExecution execution = requireExecution(executionId);
            NodeExecution node = execution.node(nodeId);
            if (!shouldApplyTerminalCallback(node, attemptId)) {
                return execution.copy();
            }
            ensureRunning(execution);
            WorkflowDefinition definition = requireDefinition(execution.definitionId());
            WorkflowGraph graph = graphBuilder.build(definition);
            failCurrentAttempt(
                    definition,
                    graph,
                    execution,
                    node,
                    NodeAttemptFailureReason.EXECUTOR_FAILURE,
                    WorkflowEvent.Type.NODE_FAILED,
                    errorMessage);
            finishIfPossible(execution);
            save(execution);
            return execution.copy();
        });
    }

    @Override
    public WorkflowExecution checkTimeouts(String executionId) {
        return executionLock.execute(executionId, () -> {
            WorkflowExecution execution = requireExecution(executionId);
            if (execution.status() != WorkflowExecutionStatus.RUNNING) {
                return execution.copy();
            }

            WorkflowDefinition definition = requireDefinition(execution.definitionId());
            Instant currentTime = now();
            if (isWorkflowTimedOut(definition, execution, currentTime)) {
                timeoutWorkflow(definition, execution, currentTime);
                return execution.copy();
            }

            WorkflowGraph graph = graphBuilder.build(definition);
            for (NodeExecution node : new ArrayList<>(execution.nodes().values())) {
                if (execution.status() != WorkflowExecutionStatus.RUNNING) {
                    break;
                }
                NodeDefinition nodeDefinition = definition.node(node.nodeId());
                NodeTimeoutPolicy timeoutPolicy = nodeDefinition.timeoutPolicy();
                if (node.status() == NodeExecutionStatus.SUBMITTED
                        && timeoutPolicy.hasDispatchTimeout()
                        && hasReached(
                                currentTime,
                                node.currentAttemptAvailableAt()
                                        .plus(timeoutPolicy.dispatchTimeout()))) {
                    timeoutCurrentAttempt(
                            definition,
                            graph,
                            execution,
                            node,
                            NodeAttemptFailureReason.DISPATCH_TIMEOUT,
                            WorkflowEvent.Type.NODE_DISPATCH_TIMED_OUT,
                            "Node dispatch timed out after " + timeoutPolicy.dispatchTimeout());
                } else if (node.status() == NodeExecutionStatus.RUNNING
                        && timeoutPolicy.hasExecutionTimeout()
                        && node.currentAttemptStartedAt() != null
                        && hasReached(
                                currentTime,
                                node.currentAttemptStartedAt()
                                        .plus(timeoutPolicy.executionTimeout()))) {
                    timeoutCurrentAttempt(
                            definition,
                            graph,
                            execution,
                            node,
                            NodeAttemptFailureReason.EXECUTION_TIMEOUT,
                            WorkflowEvent.Type.NODE_EXECUTION_TIMED_OUT,
                            "Node execution timed out after " + timeoutPolicy.executionTimeout());
                }
            }

            finishIfPossible(execution);
            save(execution);
            return execution.copy();
        });
    }

    @Override
    public WorkflowExecution continueAfterFailure(String executionId, String nodeId) {
        return executionLock.execute(executionId, () -> {
            WorkflowExecution execution = requireExecution(executionId);
            WorkflowDefinition definition = requireDefinition(execution.definitionId());
            WorkflowGraph graph = graphBuilder.build(definition);
            NodeExecution failedNode = execution.node(nodeId);
            if (failedNode.status() != NodeExecutionStatus.FAILED) {
                throw new IllegalStateException(
                        "Only a failed node can continue downstream: " + nodeId);
            }
            if (failedNode.downstreamContinuationAllowed()) {
                return execution.copy();
            }

            if (execution.status() == WorkflowExecutionStatus.SUCCESS) {
                throw new IllegalStateException(
                        "A successful workflow has no failed node to continue");
            }
            if (execution.status().isTerminal()) {
                execution.transitionTo(WorkflowExecutionStatus.RUNNING, now());
            }
            execution.resumeScheduling();
            failedNode.allowDownstreamContinuation();

            for (String descendantId : graph.descendants(nodeId)) {
                NodeExecution descendant = execution.node(descendantId);
                if (descendant.status() == NodeExecutionStatus.UPSTREAM_FAILED) {
                    descendant.resetSyntheticState();
                }
            }

            List<NodeExecution> ready = scheduler.advance(
                    definition, graph, execution, graph.successors(nodeId));
            dispatchReady(definition, execution, ready);
            finishIfPossible(execution);
            save(execution);
            return execution.copy();
        });
    }

    @Override
    public WorkflowExecution retryFailedNode(String executionId, String nodeId) {
        return executionLock.execute(executionId, () -> {
            WorkflowExecution execution = requireExecution(executionId);
            WorkflowDefinition definition = requireDefinition(execution.definitionId());
            WorkflowGraph graph = graphBuilder.build(definition);
            NodeExecution failedNode = execution.node(nodeId);
            if (failedNode.status() != NodeExecutionStatus.FAILED) {
                throw new IllegalStateException(
                        "Only a failed node can be retried: " + nodeId);
            }
            if (failedNode.downstreamContinuationAllowed()) {
                throw new IllegalStateException(
                        "Cannot retry a failed node after its downstream branch was continued: " + nodeId);
            }
            if (execution.status() == WorkflowExecutionStatus.SUCCESS) {
                throw new IllegalStateException(
                        "A successful workflow has no failed node to retry");
            }
            if (execution.status() == WorkflowExecutionStatus.CANCELED) {
                throw new IllegalStateException(
                        "A canceled workflow cannot retry a single failed node");
            }
            if (execution.status().isTerminal()) {
                execution.transitionTo(WorkflowExecutionStatus.RUNNING, now());
            }
            execution.resumeScheduling();

            failedNode.resetForManualRetry();
            for (String descendantId : graph.descendants(nodeId)) {
                NodeExecution descendant = execution.node(descendantId);
                if (descendant.status() == NodeExecutionStatus.UPSTREAM_FAILED) {
                    descendant.resetSyntheticState();
                }
            }

            List<NodeExecution> ready = scheduler.advance(
                    definition, graph, execution, List.of(nodeId));
            dispatchReady(definition, execution, ready);
            finishIfPossible(execution);
            save(execution);
            return execution.copy();
        });
    }

    @Override
    public WorkflowExecution cancel(String executionId, String reason) {
        return executionLock.execute(executionId, () -> {
            WorkflowExecution execution = requireExecution(executionId);
            if (execution.status().isTerminal()) {
                return execution.copy();
            }
            execution.stopScheduling();
            cancelNonTerminalNodes(execution, reason);
            execution.transitionTo(WorkflowExecutionStatus.CANCELED, now());
            save(execution);
            publish(WorkflowEvent.Type.WORKFLOW_CANCELED, executionId, null, null, reason);
            return execution.copy();
        });
    }

    @Override
    public WorkflowExecution retryFailedNodes(String executionId) {
        return executionLock.execute(executionId, () -> {
            WorkflowExecution execution = requireExecution(executionId);
            if (!execution.status().isTerminal()
                    || execution.status() == WorkflowExecutionStatus.SUCCESS) {
                throw new IllegalStateException(
                        "Only a failed, canceled, warning, or timed out workflow can be retried");
            }
            WorkflowDefinition definition = requireDefinition(execution.definitionId());
            WorkflowGraph graph = graphBuilder.build(definition);
            Set<String> resetNodes = new LinkedHashSet<>();
            for (NodeExecution node : execution.nodes().values()) {
                if (node.status() == NodeExecutionStatus.FAILED) {
                    node.resetForManualRetry();
                    resetNodes.add(node.nodeId());
                } else if (node.status() == NodeExecutionStatus.UPSTREAM_FAILED
                        || node.status() == NodeExecutionStatus.SKIPPED
                        || node.status() == NodeExecutionStatus.CANCELED) {
                    node.resetSyntheticState();
                    resetNodes.add(node.nodeId());
                }
            }
            if (resetNodes.isEmpty()) {
                throw new IllegalStateException("Workflow has no retryable nodes");
            }
            execution.resumeScheduling();
            execution.transitionTo(WorkflowExecutionStatus.RUNNING, now());
            List<NodeExecution> ready = scheduler.advance(
                    definition, graph, execution, resetNodes);
            dispatchReady(definition, execution, ready);
            finishIfPossible(execution);
            save(execution);
            return execution.copy();
        });
    }

    @Override
    public WorkflowExecution restart(String sourceExecutionId) {
        WorkflowExecution source = requireExecution(sourceExecutionId);
        WorkflowDefinition definition = requireDefinition(source.definitionId());
        return createAndStartExecution(definition, source.input(), sourceExecutionId, null);
    }

    @Override
    public WorkflowExecution rerunFromNode(String sourceExecutionId, String nodeId) {
        WorkflowExecution source = requireExecution(sourceExecutionId);
        WorkflowDefinition definition = requireDefinition(source.definitionId());
        WorkflowGraph graph = graphBuilder.build(definition);
        if (!definition.nodes().containsKey(nodeId)) {
            throw new IllegalArgumentException("Unknown node: " + nodeId);
        }
        return createAndStartExecution(definition, source.input(), sourceExecutionId, rerun -> {
            Set<String> ancestors = graph.ancestors(nodeId);
            Set<String> descendants = new LinkedHashSet<>(graph.descendants(nodeId));
            descendants.add(nodeId);
            for (String ancestor : ancestors) {
                NodeExecution previous = source.node(ancestor);
                if (!previous.isEffectiveSuccess()) {
                    throw new IllegalStateException(
                            "Cannot rerun from " + nodeId + ": ancestor " + ancestor
                                    + " was not successful");
                }
                rerun.node(ancestor).markCopiedSuccess(previous.output());
            }
            for (String candidate : graph.nodes()) {
                if (!ancestors.contains(candidate) && !descendants.contains(candidate)) {
                    rerun.node(candidate).transitionTo(NodeExecutionStatus.SKIPPED);
                }
            }
            return Set.of(nodeId);
        });
    }

    @Override
    public Optional<WorkflowExecution> findExecution(String executionId) {
        return executionRepository.findById(executionId);
    }

    private WorkflowExecution createAndStartExecution(
            WorkflowDefinition definition,
            Map<String, Object> input,
            String sourceExecutionId,
            ExecutionInitializer initializer) {
        validator.validate(definition);
        WorkflowGraph graph = graphBuilder.build(definition);
        String executionId = idGenerator.nextId();
        Map<String, NodeExecution> nodes = new LinkedHashMap<>();
        for (NodeDefinition node : definition.nodes().values()) {
            nodes.put(
                    node.id(),
                    new NodeExecution(
                            idGenerator.nextId(), executionId, node.id(), node.failurePolicy()));
        }
        WorkflowExecution execution = new WorkflowExecution(
                executionId, definition.id(), sourceExecutionId, input, nodes, now());
        Collection<String> candidates = graph.startNodes();
        if (initializer != null) {
            candidates = initializer.initialize(execution);
        }
        execution.transitionTo(WorkflowExecutionStatus.RUNNING, now());
        save(execution);
        publish(WorkflowEvent.Type.WORKFLOW_STARTED, executionId, null, null, null);
        List<NodeExecution> ready = scheduler.advance(definition, graph, execution, candidates);
        dispatchReady(definition, execution, ready);
        finishIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private void failCurrentAttempt(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            NodeExecution node,
            NodeAttemptFailureReason failureReason,
            WorkflowEvent.Type failureEventType,
            String errorMessage) {
        String attemptId = node.currentAttemptId();
        node.markFailure(failureReason, errorMessage, now());
        publish(failureEventType, execution.id(), node.nodeId(), attemptId, errorMessage);
        if (retryDecider.shouldRetry(definition.node(node.nodeId()), node)) {
            node.transitionTo(NodeExecutionStatus.READY);
            publish(
                    WorkflowEvent.Type.NODE_RETRY_SCHEDULED,
                    execution.id(),
                    node.nodeId(),
                    attemptId,
                    errorMessage);
            dispatchReady(definition, execution, List.of(node));
        } else {
            handleFinalFailure(definition, graph, execution, node);
        }
    }

    private void timeoutCurrentAttempt(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            NodeExecution node,
            NodeAttemptFailureReason failureReason,
            WorkflowEvent.Type timeoutEventType,
            String errorMessage) {
        nodeExecutor.cancel(new NodeCancellation(
                execution.id(),
                node.id(),
                node.nodeId(),
                node.currentAttemptId(),
                errorMessage));
        failCurrentAttempt(
                definition,
                graph,
                execution,
                node,
                failureReason,
                timeoutEventType,
                errorMessage);
    }

    private boolean isWorkflowTimedOut(
            WorkflowDefinition definition,
            WorkflowExecution execution,
            Instant currentTime) {
        return definition.timeoutPolicy().enabled()
                && execution.runStartedAt() != null
                && hasReached(
                        currentTime,
                        execution.runStartedAt().plus(definition.timeoutPolicy().timeout()));
    }

    private void timeoutWorkflow(
            WorkflowDefinition definition,
            WorkflowExecution execution,
            Instant currentTime) {
        String message = "Workflow timed out after " + definition.timeoutPolicy().timeout();
        execution.stopScheduling();
        cancelNonTerminalNodes(execution, message);
        execution.transitionTo(WorkflowExecutionStatus.TIMED_OUT, currentTime);
        save(execution);
        publish(WorkflowEvent.Type.WORKFLOW_TIMED_OUT, execution.id(), null, null, message);
    }

    private boolean hasReached(Instant currentTime, Instant deadline) {
        return !currentTime.isBefore(deadline);
    }

    private void handleFinalFailure(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            NodeExecution failedNode) {
        FailureHandlingResult result = failurePropagationPolicy.onFinalFailure(
                definition, execution, failedNode);
        if (result.stopScheduling()) {
            execution.stopScheduling();
        }
        if (result.terminateActiveNodes()) {
            cancelNonTerminalNodes(execution, "Workflow terminated after node failure");
            return;
        }
        Collection<String> candidates = result.stopScheduling()
                ? waitingNodeIds(execution)
                : graph.successors(failedNode.nodeId());
        List<NodeExecution> ready = scheduler.advance(
                definition, graph, execution, candidates);
        dispatchReady(definition, execution, ready);
    }

    private void dispatchReady(
            WorkflowDefinition definition,
            WorkflowExecution execution,
            Collection<NodeExecution> readyNodes) {
        for (NodeExecution node : readyNodes) {
            NodeDefinition nodeDefinition = definition.node(node.nodeId());
            Instant availableAt = node.attempts().isEmpty()
                    ? now()
                    : now().plus(nodeDefinition.retryPolicy().delay());
            NodeAttempt attempt = node.beginAttempt(idGenerator.nextId(), availableAt);
            save(execution);
            nodeExecutor.submit(new NodeDispatch(
                    execution.id(),
                    node.id(),
                    node.nodeId(),
                    attempt.id(),
                    attempt.attemptNumber(),
                    attempt.availableAt(),
                    execution.input(),
                    nodeDefinition.configuration()));
            publish(
                    WorkflowEvent.Type.NODE_SUBMITTED,
                    execution.id(),
                    node.nodeId(),
                    attempt.id(),
                    null);
        }
    }

    private void cancelNonTerminalNodes(WorkflowExecution execution, String reason) {
        List<NodeExecution> nodes = new ArrayList<>(execution.nodes().values());
        for (NodeExecution node : nodes) {
            if (node.status().isTerminal()) {
                continue;
            }
            if (node.status() == NodeExecutionStatus.SUBMITTED
                    || node.status() == NodeExecutionStatus.RUNNING) {
                nodeExecutor.cancel(new NodeCancellation(
                        execution.id(),
                        node.id(),
                        node.nodeId(),
                        node.currentAttemptId(),
                        reason));
            }
            node.markCanceled(now());
        }
    }

    private void finishIfPossible(WorkflowExecution execution) {
        if (execution.status() == WorkflowExecutionStatus.CANCELED
                || execution.status() == WorkflowExecutionStatus.TIMED_OUT) {
            return;
        }
        completionResolver.resolve(execution).ifPresent(status -> {
            execution.transitionTo(status, now());
            publish(
                    WorkflowEvent.Type.WORKFLOW_COMPLETED,
                    execution.id(),
                    null,
                    null,
                    status.name());
        });
    }

    private boolean shouldApplyStartCallback(NodeExecution node, String attemptId) {
        return node.isCurrentAttempt(attemptId)
                && node.currentAttemptStatus() == NodeAttemptStatus.SUBMITTED;
    }

    private boolean shouldApplyTerminalCallback(NodeExecution node, String attemptId) {
        if (!node.isCurrentAttempt(attemptId)) {
            return false;
        }
        NodeAttemptStatus status = node.currentAttemptStatus();
        return status == NodeAttemptStatus.SUBMITTED || status == NodeAttemptStatus.RUNNING;
    }

    private Set<String> waitingNodeIds(WorkflowExecution execution) {
        Set<String> result = new LinkedHashSet<>();
        for (NodeExecution node : execution.nodes().values()) {
            if (node.status() == NodeExecutionStatus.WAITING) {
                result.add(node.nodeId());
            }
        }
        return result;
    }

    private WorkflowDefinition requireDefinition(String definitionId) {
        return definitionRepository.findById(definitionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Workflow definition not found: " + definitionId));
    }

    private WorkflowExecution requireExecution(String executionId) {
        return executionRepository.findById(executionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Workflow execution not found: " + executionId));
    }

    private void ensureRunning(WorkflowExecution execution) {
        if (execution.status() != WorkflowExecutionStatus.RUNNING) {
            throw new IllegalStateException(
                    "Workflow is not running: " + execution.status());
        }
    }

    private void save(WorkflowExecution execution) {
        executionRepository.save(execution);
    }

    private Instant now() {
        return clock.instant();
    }

    private void publish(
            WorkflowEvent.Type type,
            String executionId,
            String nodeId,
            String attemptId,
            String message) {
        eventListener.onEvent(new WorkflowEvent(
                type, executionId, nodeId, attemptId, message, now()));
    }

    @FunctionalInterface
    private interface ExecutionInitializer {
        Collection<String> initialize(WorkflowExecution execution);
    }
}
