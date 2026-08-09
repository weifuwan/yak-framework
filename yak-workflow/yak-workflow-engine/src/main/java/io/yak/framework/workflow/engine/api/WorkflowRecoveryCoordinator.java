package io.yak.framework.workflow.engine.api;

import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.NodeAttempt;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.NodeInputResolver;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.graph.WorkflowDefinitionValidator;
import io.yak.framework.workflow.engine.graph.WorkflowGraph;
import io.yak.framework.workflow.engine.graph.WorkflowGraphBuilder;
import io.yak.framework.workflow.engine.scheduler.DefaultReadyNodeResolver;
import io.yak.framework.workflow.engine.scheduler.DefaultWorkflowScheduler;
import io.yak.framework.workflow.engine.scheduler.WorkflowCompletionResolver;
import io.yak.framework.workflow.engine.scheduler.WorkflowScheduler;
import io.yak.framework.workflow.engine.policy.DefaultTriggerRuleEvaluator;
import io.yak.framework.workflow.engine.spi.ExecutionLock;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import io.yak.framework.workflow.engine.spi.IdGenerator;
import io.yak.framework.workflow.engine.spi.NodeDispatch;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.spi.NodeRecovery;
import io.yak.framework.workflow.engine.spi.WorkflowDefinitionRepository;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Reconciles persisted workflow state after a host restart.
 *
 * <p>This coordinator deliberately sits beside {@link DefaultWorkflowEngine}: normal commands keep
 * flowing through the engine mailbox, while startup recovery is an explicit host lifecycle action.
 * The coordinator shares the same repositories, id generator and execution lock with the engine, so
 * recovery never creates a second identity for an already persisted attempt.</p>
 */
public final class WorkflowRecoveryCoordinator {

    private final WorkflowDefinitionRepository definitionRepository;
    private final ExecutionRepository executionRepository;
    private final NodeExecutor nodeExecutor;
    private final ExecutionLock executionLock;
    private final IdGenerator idGenerator;
    private final Clock clock;
    private final WorkflowDefinitionValidator validator = new WorkflowDefinitionValidator();
    private final WorkflowGraphBuilder graphBuilder = new WorkflowGraphBuilder();
    private final WorkflowScheduler scheduler = new DefaultWorkflowScheduler(
            new DefaultReadyNodeResolver(new DefaultTriggerRuleEvaluator()));
    private final WorkflowCompletionResolver completionResolver = new WorkflowCompletionResolver();
    private final NodeInputResolver nodeInputResolver = new NodeInputResolver();

    public WorkflowRecoveryCoordinator(
            WorkflowDefinitionRepository definitionRepository,
            ExecutionRepository executionRepository,
            NodeExecutor nodeExecutor,
            ExecutionLock executionLock,
            IdGenerator idGenerator,
            Clock clock) {
        this.definitionRepository = Objects.requireNonNull(
                definitionRepository, "definitionRepository");
        this.executionRepository = Objects.requireNonNull(executionRepository, "executionRepository");
        this.nodeExecutor = Objects.requireNonNull(nodeExecutor, "nodeExecutor");
        this.executionLock = Objects.requireNonNull(executionLock, "executionLock");
        this.idGenerator = Objects.requireNonNull(idGenerator, "idGenerator");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /** Recover one execution from the state already persisted by the host repository. */
    public WorkflowExecution recover(String executionId) {
        Objects.requireNonNull(executionId, "executionId");
        return executionLock.execute(executionId, () -> recoverLocked(executionId));
    }

    private WorkflowExecution recoverLocked(String executionId) {
        WorkflowExecution execution = executionRepository.findById(executionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown workflow execution: " + executionId));
        if (execution.status().isTerminal()) {
            return execution.copy();
        }

        WorkflowDefinition definition = definitionRepository.findById(execution.definitionId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown workflow definition: " + execution.definitionId()));
        validator.validate(definition);
        WorkflowGraph graph = graphBuilder.build(definition);

        reconcilePersistedAttempts(definition, graph, execution);

        if (execution.status() == WorkflowExecutionStatus.RUNNING
                && !execution.schedulingStopped()) {
            List<NodeExecution> ready = new ArrayList<>();
            for (NodeExecution node : execution.nodes().values()) {
                if (node.status() == NodeExecutionStatus.READY) {
                    ready.add(node);
                }
            }
            ready.addAll(scheduler.advance(
                    definition,
                    graph,
                    execution,
                    waitingNodeIds(execution)));
            dispatchReady(definition, graph, execution, ready);
            finishIfPossible(execution);
            executionRepository.save(execution);
        }
        return execution.copy();
    }

    private void reconcilePersistedAttempts(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution) {
        for (NodeExecution node : execution.nodes().values()) {
            if (!isRecoverableAttemptState(node.status()) || node.attempts().isEmpty()) {
                continue;
            }
            NodeAttempt attempt = node.attempts().get(node.attempts().size() - 1);
            nodeExecutor.recover(new NodeRecovery(
                    toDispatch(definition, graph, execution, node, attempt),
                    execution.status(),
                    node.status(),
                    attempt.status()));
        }
    }

    private boolean isRecoverableAttemptState(NodeExecutionStatus status) {
        return status == NodeExecutionStatus.SUBMITTED
                || status == NodeExecutionStatus.RUNNING
                || status == NodeExecutionStatus.PAUSING
                || status == NodeExecutionStatus.PAUSED
                || status == NodeExecutionStatus.RESUMING;
    }

    private Collection<String> waitingNodeIds(WorkflowExecution execution) {
        return execution.nodes().values().stream()
                .filter(node -> node.status() == NodeExecutionStatus.WAITING)
                .map(NodeExecution::nodeId)
                .toList();
    }

    private void dispatchReady(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            Collection<NodeExecution> readyNodes) {
        if (execution.status() != WorkflowExecutionStatus.RUNNING
                || execution.schedulingStopped()) {
            return;
        }
        for (NodeExecution node : readyNodes) {
            NodeDefinition nodeDefinition = definition.node(node.nodeId());
            Instant availableAt = node.attempts().isEmpty()
                    ? now()
                    : now().plus(nodeDefinition.retryPolicy().delay());
            NodeAttempt attempt = node.beginAttempt(idGenerator.nextId(), availableAt);
            executionRepository.save(execution);
            nodeExecutor.submit(toDispatch(definition, graph, execution, node, attempt));
        }
    }

    private NodeDispatch toDispatch(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            NodeExecution node,
            NodeAttempt attempt) {
        NodeDefinition nodeDefinition = definition.node(node.nodeId());
        Map<String, Map<String, Object>> predecessorOutputs = collectPredecessorOutputs(
                execution, graph.predecessors(node.nodeId()));
        Map<String, Object> nodeInput = nodeInputResolver.resolve(
                nodeDefinition.inputMapping(), execution.input(), predecessorOutputs);
        Instant dispatchDeadline = attempt.dispatchDeadline(
                nodeDefinition.timeoutPolicy().dispatchTimeout());
        return new NodeDispatch(
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
                nodeDefinition.timeoutPolicy().executionTimeout());
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

    private void finishIfPossible(WorkflowExecution execution) {
        if (execution.status() != WorkflowExecutionStatus.RUNNING) {
            return;
        }
        completionResolver.resolve(execution)
                .ifPresent(status -> execution.transitionTo(status, now()));
    }

    private Instant now() {
        return clock.instant();
    }
}
