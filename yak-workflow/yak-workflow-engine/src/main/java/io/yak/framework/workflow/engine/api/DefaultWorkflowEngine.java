package io.yak.framework.workflow.engine.api;

import io.yak.framework.workflow.engine.command.WorkflowCommand;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.event.WorkflowEventListener;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.runtime.DefaultWorkflowRuntime;
import io.yak.framework.workflow.engine.spi.ExecutionLock;
import io.yak.framework.workflow.engine.spi.ExecutionMailbox;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import io.yak.framework.workflow.engine.spi.IdGenerator;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.spi.WorkflowDefinitionRepository;
import io.yak.framework.workflow.engine.support.InMemoryExecutionRepository;
import io.yak.framework.workflow.engine.support.InMemoryWorkflowDefinitionRepository;
import io.yak.framework.workflow.engine.support.LocalExecutionLock;
import io.yak.framework.workflow.engine.support.UuidIdGenerator;
import java.time.Clock;
import java.util.Map;
import java.util.Optional;

/**
 * Stable host-facing facade for the workflow engine.
 *
 * <p>The public API owns compatibility while {@link DefaultWorkflowRuntime} owns command routing,
 * scheduling and execution-state orchestration. Hosts should depend on this facade or
 * {@link WorkflowEngine}, not on runtime implementation packages.</p>
 */
public final class DefaultWorkflowEngine implements WorkflowEngine {

    private final DefaultWorkflowRuntime runtime;

    /** Backward-compatible constructor that adapts the execution lock into a local mailbox. */
    public DefaultWorkflowEngine(
            WorkflowDefinitionRepository definitionRepository,
            ExecutionRepository executionRepository,
            NodeExecutor nodeExecutor,
            ExecutionLock executionLock,
            IdGenerator idGenerator,
            Clock clock,
            WorkflowEventListener eventListener) {
        this.runtime = new DefaultWorkflowRuntime(
                definitionRepository,
                executionRepository,
                nodeExecutor,
                executionLock,
                idGenerator,
                clock,
                eventListener);
    }

    /** Constructor for hosts that provide a custom command mailbox implementation. */
    public DefaultWorkflowEngine(
            WorkflowDefinitionRepository definitionRepository,
            ExecutionRepository executionRepository,
            NodeExecutor nodeExecutor,
            ExecutionMailbox executionMailbox,
            IdGenerator idGenerator,
            Clock clock,
            WorkflowEventListener eventListener) {
        this.runtime = new DefaultWorkflowRuntime(
                definitionRepository,
                executionRepository,
                nodeExecutor,
                executionMailbox,
                idGenerator,
                clock,
                eventListener);
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
        runtime.registerDefinition(definition);
    }

    @Override
    public WorkflowExecution start(String definitionId, Map<String, Object> input) {
        return runtime.start(definitionId, input);
    }

    @Override
    public WorkflowExecution submit(WorkflowCommand command) {
        return runtime.submit(command);
    }

    @Override
    public Optional<WorkflowExecution> findExecution(String executionId) {
        return runtime.findExecution(executionId);
    }
}
