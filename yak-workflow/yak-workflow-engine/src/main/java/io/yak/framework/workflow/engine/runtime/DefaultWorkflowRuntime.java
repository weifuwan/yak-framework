package io.yak.framework.workflow.engine.runtime;

import io.yak.framework.workflow.engine.command.WorkflowCommand;
import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.NodeTimeoutPolicy;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.event.WorkflowEvent;
import io.yak.framework.workflow.engine.event.WorkflowEventListener;
import io.yak.framework.workflow.engine.execution.NodeAttempt;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.NodeInputResolver;
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
import io.yak.framework.workflow.engine.spi.ExecutionMailbox;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import io.yak.framework.workflow.engine.spi.IdGenerator;
import io.yak.framework.workflow.engine.spi.NodeCancellation;
import io.yak.framework.workflow.engine.spi.NodeControlResult;
import io.yak.framework.workflow.engine.spi.NodeDispatch;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.spi.NodePauseRequest;
import io.yak.framework.workflow.engine.spi.NodeResumeRequest;
import io.yak.framework.workflow.engine.spi.WorkflowDefinitionRepository;
import io.yak.framework.workflow.engine.state.NodeAttemptFailureReason;
import io.yak.framework.workflow.engine.state.NodeAttemptStatus;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import io.yak.framework.workflow.engine.support.InMemoryExecutionRepository;
import io.yak.framework.workflow.engine.support.InMemoryWorkflowDefinitionRepository;
import io.yak.framework.workflow.engine.support.LocalExecutionLock;
import io.yak.framework.workflow.engine.support.LocalExecutionMailbox;
import io.yak.framework.workflow.engine.support.UuidIdGenerator;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Internal runtime that owns normal workflow command routing and execution-state orchestration.
 *
 * <p>Public host compatibility stays in the api facade. This runtime operates on the single
 * WorkflowExecution/NodeExecution/NodeAttempt truth and routes existing-execution commands through
 * the configured ExecutionMailbox.</p>
 */
public final class DefaultWorkflowRuntime {

    private final WorkflowDefinitionRepository definitionRepository;
    private final ExecutionRepository executionRepository;
    private final NodeExecutor nodeExecutor;
    private final ExecutionMailbox executionMailbox;
    private final IdGenerator idGenerator;
    private final Clock clock;
    private final WorkflowEventListener eventListener;
    private final WorkflowDefinitionValidator validator;
    private final WorkflowGraphBuilder graphBuilder;
    private final WorkflowScheduler scheduler;
    private final WorkflowCompletionResolver completionResolver;
    private final RetryDecider retryDecider;
    private final FailurePropagationPolicy failurePropagationPolicy;
    private final NodeInputResolver nodeInputResolver;

    /** Backward-compatible runtime constructor that adapts an execution lock into a local mailbox. */
    public DefaultWorkflowRuntime(
            WorkflowDefinitionRepository definitionRepository,
            ExecutionRepository executionRepository,
            NodeExecutor nodeExecutor,
            ExecutionLock executionLock,
            IdGenerator idGenerator,
            Clock clock,
            WorkflowEventListener eventListener) {
        this(
                definitionRepository,
                executionRepository,
                nodeExecutor,
                new LocalExecutionMailbox(executionLock),
                idGenerator,
                clock,
                eventListener);
    }

    /** Runtime constructor for hosts that provide a custom command mailbox implementation. */
    public DefaultWorkflowRuntime(
            WorkflowDefinitionRepository definitionRepository,
            ExecutionRepository executionRepository,
            NodeExecutor nodeExecutor,
            ExecutionMailbox executionMailbox,
            IdGenerator idGenerator,
            Clock clock,
            WorkflowEventListener eventListener) {
        this.definitionRepository = Objects.requireNonNull(definitionRepository, "definitionRepository");
        this.executionRepository = Objects.requireNonNull(executionRepository, "executionRepository");
        this.nodeExecutor = Objects.requireNonNull(nodeExecutor, "nodeExecutor");
        this.executionMailbox = Objects.requireNonNull(executionMailbox, "executionMailbox");
        this.idGenerator = Objects.requireNonNull(idGenerator, "idGenerator");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.eventListener = Objects.requireNonNull(eventListener, "eventListener");
        this.validator = new WorkflowDefinitionValidator();
        this.graphBuilder = new WorkflowGraphBuilder();
        this.scheduler = new DefaultWorkflowScheduler(
                new DefaultReadyNodeResolver(new DefaultTriggerRuleEvaluator()));
        this.completionResolver = new WorkflowCompletionResolver();
        this.retryDecider = new DefaultRetryDecider();
        this.failurePropagationPolicy = new DefaultFailurePropagationPolicy();
        this.nodeInputResolver = new NodeInputResolver();
    }

    public static DefaultWorkflowRuntime inMemory(NodeExecutor nodeExecutor) {
        return inMemory(nodeExecutor, WorkflowEventListener.noop());
    }

    public static DefaultWorkflowRuntime inMemory(
            NodeExecutor nodeExecutor,
            WorkflowEventListener eventListener) {
        return inMemory(nodeExecutor, eventListener, Clock.systemUTC());
    }

    public static DefaultWorkflowRuntime inMemory(
            NodeExecutor nodeExecutor,
            WorkflowEventListener eventListener,
            Clock clock) {
        return new DefaultWorkflowRuntime(
                new InMemoryWorkflowDefinitionRepository(),
                new InMemoryExecutionRepository(),
                nodeExecutor,
                new LocalExecutionLock(),
                new UuidIdGenerator(),
                clock,
                eventListener);
    }

    public void registerDefinition(WorkflowDefinition definition) {
        validator.validate(definition);
        definitionRepository.save(definition);
    }

    public WorkflowExecution start(String definitionId, Map<String, Object> input) {
        WorkflowDefinition definition = requireDefinition(definitionId);
        return createAndStartExecution(definition, input, null, null);
    }

    public WorkflowExecution submit(WorkflowCommand command) {
        Objects.requireNonNull(command, "command");
        return executionMailbox.submit(command, this::handleCommand);
    }

    public Optional<WorkflowExecution> findExecution(String executionId) {
        return executionRepository.findById(executionId);
    }

    private WorkflowExecution handleCommand(WorkflowCommand command) {
        return switch (command) {
            case WorkflowCommand.NodeStarted value -> handleNodeStarted(value);
            case WorkflowCommand.NodeSucceeded value -> handleNodeSucceeded(value);
            case WorkflowCommand.NodeFailed value -> handleNodeFailed(value);
            case WorkflowCommand.NodePaused value -> handleNodePaused(value);
            case WorkflowCommand.NodeResumed value -> handleNodeResumed(value);
            case WorkflowCommand.CheckTimeouts value -> handleCheckTimeouts(value);
            case WorkflowCommand.PauseWorkflow value -> handlePauseWorkflow(value);
            case WorkflowCommand.ResumeWorkflow value -> handleResumeWorkflow(value);
            case WorkflowCommand.CancelWorkflow value -> handleCancelWorkflow(value);
            case WorkflowCommand.ContinueAfterFailure value -> handleContinueAfterFailure(value);
            case WorkflowCommand.RetryFailedNode value -> handleRetryFailedNode(value);
            case WorkflowCommand.RetryFailedNodes value -> handleRetryFailedNodes(value);
            case WorkflowCommand.RestartWorkflow value -> handleRestartWorkflow(value);
            case WorkflowCommand.RerunFromNode value -> handleRerunFromNode(value);
        };
    }

    private WorkflowExecution handleNodeStarted(WorkflowCommand.NodeStarted command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        NodeExecution node = execution.node(command.nodeId());
        if (!shouldApplyStartCallback(node, command.attemptId())) {
            return execution.copy();
        }
        ensureCallbackLifecycle(execution);
        node.markRunning(now());
        execution.touch(now());
        save(execution);
        publish(
                WorkflowEvent.Type.NODE_STARTED,
                command.executionId(),
                command.nodeId(),
                command.attemptId(),
                null);
        return execution.copy();
    }

    private WorkflowExecution handlePauseWorkflow(WorkflowCommand.PauseWorkflow command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        if (execution.status() == WorkflowExecutionStatus.PAUSING
                || execution.status() == WorkflowExecutionStatus.PAUSED) {
            return execution.copy();
        }
        if (execution.status() != WorkflowExecutionStatus.RUNNING) {
            throw new IllegalStateException(
                    "Only a running workflow can pause: " + execution.status());
        }

        String pauseReason = command.reason() == null || command.reason().isBlank()
                ? "Workflow pause requested"
                : command.reason();
        execution.transitionTo(WorkflowExecutionStatus.PAUSING, now());
        publish(
                WorkflowEvent.Type.WORKFLOW_PAUSE_REQUESTED,
                execution.id(),
                null,
                null,
                pauseReason);

        for (NodeExecution node : execution.nodes().values()) {
            if (node.status() != NodeExecutionStatus.SUBMITTED
                    && node.status() != NodeExecutionStatus.RUNNING) {
                continue;
            }
            NodePauseRequest request = new NodePauseRequest(
                    execution.id(),
                    node.id(),
                    node.nodeId(),
                    node.currentAttemptId(),
                    pauseReason);
            if (nodeExecutor.pause(request) == NodeControlResult.ACCEPTED) {
                node.markPausing();
                publish(
                        WorkflowEvent.Type.NODE_PAUSE_REQUESTED,
                        execution.id(),
                        node.nodeId(),
                        node.currentAttemptId(),
                        pauseReason);
            }
        }

        settlePauseIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleNodePaused(WorkflowCommand.NodePaused command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        NodeExecution node = execution.node(command.nodeId());
        if (execution.status() != WorkflowExecutionStatus.PAUSING
                || !node.isCurrentAttempt(command.attemptId())
                || node.currentAttemptStatus() != NodeAttemptStatus.PAUSING) {
            return execution.copy();
        }

        node.markPaused(now());
        execution.touch(now());
        publish(
                WorkflowEvent.Type.NODE_PAUSED,
                command.executionId(),
                command.nodeId(),
                command.attemptId(),
                null);
        settlePauseIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleResumeWorkflow(WorkflowCommand.ResumeWorkflow command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        if (execution.status() == WorkflowExecutionStatus.RUNNING
                || execution.status() == WorkflowExecutionStatus.RESUMING) {
            return execution.copy();
        }
        if (execution.status() == WorkflowExecutionStatus.PAUSING) {
            throw new IllegalStateException(
                    "Workflow is still pausing; wait until PAUSED before resume");
        }
        if (execution.status() != WorkflowExecutionStatus.PAUSED) {
            throw new IllegalStateException(
                    "Only a paused workflow can resume: " + execution.status());
        }

        execution.transitionTo(WorkflowExecutionStatus.RESUMING, now());
        publish(
                WorkflowEvent.Type.WORKFLOW_RESUME_REQUESTED,
                execution.id(),
                null,
                null,
                null);

        for (NodeExecution node : execution.nodes().values()) {
            if (node.status() != NodeExecutionStatus.PAUSED) {
                continue;
            }
            node.markResuming();
            nodeExecutor.resume(new NodeResumeRequest(
                    execution.id(),
                    node.id(),
                    node.nodeId(),
                    node.currentAttemptId()));
            publish(
                    WorkflowEvent.Type.NODE_RESUME_REQUESTED,
                    execution.id(),
                    node.nodeId(),
                    node.currentAttemptId(),
                    null);
        }

        completeResumeIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleNodeResumed(WorkflowCommand.NodeResumed command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        NodeExecution node = execution.node(command.nodeId());
        if (execution.status() != WorkflowExecutionStatus.RESUMING
                || !node.isCurrentAttempt(command.attemptId())
                || node.currentAttemptStatus() != NodeAttemptStatus.RESUMING) {
            return execution.copy();
        }

        node.markResumed(now());
        execution.touch(now());
        publish(
                WorkflowEvent.Type.NODE_RESUMED,
                command.executionId(),
                command.nodeId(),
                command.attemptId(),
                null);
        completeResumeIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleNodeSucceeded(WorkflowCommand.NodeSucceeded command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        NodeExecution node = execution.node(command.nodeId());
        if (!shouldApplyTerminalCallback(node, command.attemptId())) {
            return execution.copy();
        }
        ensureCallbackLifecycle(execution);
        WorkflowDefinition definition = requireDefinition(execution.definitionId());
        WorkflowGraph graph = graphBuilder.build(definition);
        node.markSuccess(command.output(), now());
        publish(
                WorkflowEvent.Type.NODE_SUCCEEDED,
                command.executionId(),
                command.nodeId(),
                command.attemptId(),
                null);
        if (execution.status() == WorkflowExecutionStatus.RUNNING) {
            List<NodeExecution> ready = scheduler.advance(
                    definition, graph, execution, graph.successors(command.nodeId()));
            dispatchReady(definition, execution, ready);
        }
        finishIfPossible(execution);
        settleLifecycleIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleNodeFailed(WorkflowCommand.NodeFailed command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        NodeExecution node = execution.node(command.nodeId());
        if (!shouldApplyTerminalCallback(node, command.attemptId())) {
            return execution.copy();
        }
        ensureCallbackLifecycle(execution);
        WorkflowDefinition definition = requireDefinition(execution.definitionId());
        WorkflowGraph graph = graphBuilder.build(definition);
        failCurrentAttempt(
                definition,
                graph,
                execution,
                node,
                NodeAttemptFailureReason.EXECUTOR_FAILURE,
                WorkflowEvent.Type.NODE_FAILED,
                command.errorMessage());
        finishIfPossible(execution);
        settleLifecycleIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleCheckTimeouts(WorkflowCommand.CheckTimeouts command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        if (execution.status() != WorkflowExecutionStatus.RUNNING
                && execution.status() != WorkflowExecutionStatus.PAUSING) {
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
            if (execution.status().isTerminal()) {
                break;
            }
            NodeDefinition nodeDefinition = definition.node(node.nodeId());
            NodeTimeoutPolicy timeoutPolicy = nodeDefinition.timeoutPolicy();
            if (node.status() == NodeExecutionStatus.SUBMITTED
                    && timeoutPolicy.hasDispatchTimeout()) {
                Instant dispatchDeadline = node.currentAttemptDispatchDeadline(
                        timeoutPolicy.dispatchTimeout());
                if (dispatchDeadline != null && hasReached(currentTime, dispatchDeadline)) {
                    timeoutCurrentAttempt(
                            definition,
                            graph,
                            execution,
                            node,
                            NodeAttemptFailureReason.DISPATCH_TIMEOUT,
                            WorkflowEvent.Type.NODE_DISPATCH_TIMED_OUT,
                            "Node dispatch timed out after " + timeoutPolicy.dispatchTimeout());
                }
            } else if (node.status() == NodeExecutionStatus.RUNNING
                    && timeoutPolicy.hasExecutionTimeout()) {
                Instant executionDeadline = node.currentAttemptExecutionDeadline(
                        timeoutPolicy.executionTimeout());
                if (executionDeadline != null && hasReached(currentTime, executionDeadline)) {
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
        }

        finishIfPossible(execution);
        settleLifecycleIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleContinueAfterFailure(
            WorkflowCommand.ContinueAfterFailure command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        ensureNotPauseLifecycle(execution);
        WorkflowDefinition definition = requireDefinition(execution.definitionId());
        WorkflowGraph graph = graphBuilder.build(definition);
        NodeExecution failedNode = execution.node(command.nodeId());
        if (failedNode.status() != NodeExecutionStatus.FAILED) {
            throw new IllegalStateException(
                    "Only a failed node can continue downstream: " + command.nodeId());
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

        for (String descendantId : graph.descendants(command.nodeId())) {
            NodeExecution descendant = execution.node(descendantId);
            if (descendant.status() == NodeExecutionStatus.UPSTREAM_FAILED) {
                descendant.resetSyntheticState();
            }
        }

        List<NodeExecution> ready = scheduler.advance(
                definition, graph, execution, graph.successors(command.nodeId()));
        dispatchReady(definition, execution, ready);
        finishIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleRetryFailedNode(WorkflowCommand.RetryFailedNode command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        ensureNotPauseLifecycle(execution);
        WorkflowDefinition definition = requireDefinition(execution.definitionId());
        WorkflowGraph graph = graphBuilder.build(definition);
        NodeExecution failedNode = execution.node(command.nodeId());
        if (failedNode.status() != NodeExecutionStatus.FAILED) {
            throw new IllegalStateException(
                    "Only a failed node can be retried: " + command.nodeId());
        }
        if (failedNode.downstreamContinuationAllowed()) {
            throw new IllegalStateException(
                    "Cannot retry a failed node after its downstream branch was continued: "
                            + command.nodeId());
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
        for (String descendantId : graph.descendants(command.nodeId())) {
            NodeExecution descendant = execution.node(descendantId);
            if (descendant.status() == NodeExecutionStatus.UPSTREAM_FAILED) {
                descendant.resetSyntheticState();
            }
        }

        List<NodeExecution> ready = scheduler.advance(
                definition, graph, execution, List.of(command.nodeId()));
        dispatchReady(definition, execution, ready);
        finishIfPossible(execution);
        save(execution);
        return execution.copy();
    }

    private WorkflowExecution handleCancelWorkflow(WorkflowCommand.CancelWorkflow command) {
        WorkflowExecution execution = requireExecution(command.executionId());
        if (execution.status().isTerminal()) {
            return execution.copy();
        }
        execution.stopScheduling();
        cancelNonTerminalNodes(execution, command.reason());
        execution.transitionTo(WorkflowExecutionStatus.CANCELED, now());
        save(execution);
        publish(
                WorkflowEvent.Type.WORKFLOW_CANCELED,
                command.executionId(),
                null,
                null,
                command.reason());
        return execution.copy();
    }

    private WorkflowExecution handleRetryFailedNodes(WorkflowCommand.RetryFailedNodes command) {
        WorkflowExecution execution = requireExecution(command.executionId());
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
    }

    private WorkflowExecution handleRestartWorkflow(WorkflowCommand.RestartWorkflow command) {
        WorkflowExecution source = requireExecution(command.executionId());
        WorkflowDefinition definition = requireDefinition(source.definitionId());
        return createAndStartExecution(definition, source.input(), command.executionId(), null);
    }

    private WorkflowExecution handleRerunFromNode(WorkflowCommand.RerunFromNode command) {
        WorkflowExecution source = requireExecution(command.executionId());
        WorkflowDefinition definition = requireDefinition(source.definitionId());
        WorkflowGraph graph = graphBuilder.build(definition);
        if (!definition.nodes().containsKey(command.nodeId())) {
            throw new IllegalArgumentException("Unknown node: " + command.nodeId());
        }
        return createAndStartExecution(definition, source.input(), command.executionId(), rerun -> {
            Set<String> ancestors = graph.ancestors(command.nodeId());
            Set<String> descendants = new LinkedHashSet<>(graph.descendants(command.nodeId()));
            descendants.add(command.nodeId());
            for (String ancestor : ancestors) {
                NodeExecution previous = source.node(ancestor);
                if (!previous.isEffectiveSuccess()) {
                    throw new IllegalStateException(
                            "Cannot rerun from " + command.nodeId() + ": ancestor " + ancestor
                                    + " was not successful");
                }
                rerun.node(ancestor).markCopiedSuccess(previous.output());
            }
            for (String candidate : graph.nodes()) {
                if (!ancestors.contains(candidate) && !descendants.contains(candidate)) {
                    rerun.node(candidate).transitionTo(NodeExecutionStatus.SKIPPED);
                }
            }
            return Set.of(command.nodeId());
        });
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
            if (execution.status() == WorkflowExecutionStatus.RUNNING
                    && !execution.schedulingStopped()) {
                dispatchReady(definition, execution, List.of(node));
            }
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
        Instant deadline = execution.workflowDeadline(definition.timeoutPolicy().timeout());
        return definition.timeoutPolicy().enabled()
                && deadline != null
                && hasReached(currentTime, deadline);
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
        if (execution.status() != WorkflowExecutionStatus.RUNNING) {
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
        if (execution.status() != WorkflowExecutionStatus.RUNNING
                || execution.schedulingStopped()) {
            return;
        }
        WorkflowGraph graph = graphBuilder.build(definition);
        for (NodeExecution node : readyNodes) {
            NodeDefinition nodeDefinition = definition.node(node.nodeId());
            Instant availableAt = node.attempts().isEmpty()
                    ? now()
                    : now().plus(nodeDefinition.retryPolicy().delay());
            NodeAttempt attempt = node.beginAttempt(idGenerator.nextId(), availableAt);
            Map<String, Map<String, Object>> predecessorOutputs = collectPredecessorOutputs(
                    execution, graph.predecessors(node.nodeId()));
            Map<String, Object> nodeInput = nodeInputResolver.resolve(
                    nodeDefinition.inputMapping(), execution.input(), predecessorOutputs);
            Instant dispatchDeadline = attempt.dispatchDeadline(
                    nodeDefinition.timeoutPolicy().dispatchTimeout());
            save(execution);
            nodeExecutor.submit(new NodeDispatch(
                    execution.id(),
                    node.id(),
                    node.nodeId(),
                    attempt.id(),
                    attempt.attemptNumber(),
                    attempt.availableAt(),
                    execution.input(),
                    nodeDefinition.configuration(),
                    predecessorOutputs,
                    nodeInput,
                    dispatchDeadline,
                    nodeDefinition.timeoutPolicy().executionTimeout()));
            publish(
                    WorkflowEvent.Type.NODE_SUBMITTED,
                    execution.id(),
                    node.nodeId(),
                    attempt.id(),
                    null);
        }
    }

    private Map<String, Map<String, Object>> collectPredecessorOutputs(
            WorkflowExecution execution,
            Set<String> predecessorIds) {
        if (predecessorIds.isEmpty()) {
            return Map.of();
        }
        Map<String, Map<String, Object>> outputs = new LinkedHashMap<>();
        for (String predecessorId : predecessorIds) {
            outputs.put(predecessorId, execution.node(predecessorId).output());
        }
        return outputs;
    }

    private void cancelNonTerminalNodes(WorkflowExecution execution, String reason) {
        List<NodeExecution> nodes = new ArrayList<>(execution.nodes().values());
        for (NodeExecution node : nodes) {
            if (node.status().isTerminal()) {
                continue;
            }
            if (node.status() == NodeExecutionStatus.SUBMITTED
                    || node.status() == NodeExecutionStatus.RUNNING
                    || node.status() == NodeExecutionStatus.PAUSING
                    || node.status() == NodeExecutionStatus.PAUSED
                    || node.status() == NodeExecutionStatus.RESUMING) {
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
                || execution.status() == WorkflowExecutionStatus.TIMED_OUT
                || execution.status() == WorkflowExecutionStatus.PAUSED) {
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

    private void settleLifecycleIfPossible(WorkflowExecution execution) {
        if (execution.status() == WorkflowExecutionStatus.PAUSING) {
            settlePauseIfPossible(execution);
        } else if (execution.status() == WorkflowExecutionStatus.RESUMING) {
            completeResumeIfPossible(execution);
        }
    }

    private void settlePauseIfPossible(WorkflowExecution execution) {
        if (execution.status() != WorkflowExecutionStatus.PAUSING) {
            return;
        }
        boolean activeUnsettled = execution.nodes().values().stream()
                .map(NodeExecution::status)
                .anyMatch(status -> status == NodeExecutionStatus.SUBMITTED
                        || status == NodeExecutionStatus.RUNNING
                        || status == NodeExecutionStatus.PAUSING
                        || status == NodeExecutionStatus.RESUMING);
        if (!activeUnsettled) {
            execution.transitionTo(WorkflowExecutionStatus.PAUSED, now());
            publish(
                    WorkflowEvent.Type.WORKFLOW_PAUSED,
                    execution.id(),
                    null,
                    null,
                    null);
        }
    }

    private void completeResumeIfPossible(WorkflowExecution execution) {
        if (execution.status() != WorkflowExecutionStatus.RESUMING) {
            return;
        }
        boolean waitingForAttempt = execution.nodes().values().stream()
                .map(NodeExecution::status)
                .anyMatch(status -> status == NodeExecutionStatus.PAUSED
                        || status == NodeExecutionStatus.RESUMING
                        || status == NodeExecutionStatus.PAUSING);
        if (waitingForAttempt) {
            return;
        }

        execution.transitionTo(WorkflowExecutionStatus.RUNNING, now());
        publish(
                WorkflowEvent.Type.WORKFLOW_RESUMED,
                execution.id(),
                null,
                null,
                null);

        WorkflowDefinition definition = requireDefinition(execution.definitionId());
        WorkflowGraph graph = graphBuilder.build(definition);
        if (!execution.schedulingStopped()) {
            List<NodeExecution> deferredReady = execution.nodes().values().stream()
                    .filter(node -> node.status() == NodeExecutionStatus.READY)
                    .toList();
            dispatchReady(definition, execution, deferredReady);
        }
        List<NodeExecution> ready = scheduler.advance(
                definition, graph, execution, waitingNodeIds(execution));
        dispatchReady(definition, execution, ready);
        finishIfPossible(execution);
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
        return status == NodeAttemptStatus.SUBMITTED
                || status == NodeAttemptStatus.RUNNING
                || status == NodeAttemptStatus.PAUSING
                || status == NodeAttemptStatus.RESUMING;
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

    private void ensureCallbackLifecycle(WorkflowExecution execution) {
        WorkflowExecutionStatus status = execution.status();
        if (status != WorkflowExecutionStatus.RUNNING
                && status != WorkflowExecutionStatus.PAUSING
                && status != WorkflowExecutionStatus.RESUMING) {
            throw new IllegalStateException(
                    "Workflow does not accept executor callbacks: " + status);
        }
    }

    private void ensureNotPauseLifecycle(WorkflowExecution execution) {
        if (execution.status().isPauseLifecycle()) {
            throw new IllegalStateException(
                    "Workflow recovery is unavailable while pause/resume is in progress: "
                            + execution.status());
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
