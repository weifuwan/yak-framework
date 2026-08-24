package io.yak.framework.workflow.engine.api;

import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.recovery.WorkflowRecoveryRuntime;
import io.yak.framework.workflow.engine.spi.ExecutionLock;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import io.yak.framework.workflow.engine.spi.IdGenerator;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.spi.WorkflowDefinitionRepository;
import java.time.Clock;

/**
 * Stable host-facing recovery facade.
 *
 * <p>Recovery reconciles already-persisted execution truth after host restart; it never creates a
 * second identity for an existing attempt.</p>
 */
public final class WorkflowRecoveryCoordinator {

    private final WorkflowRecoveryRuntime runtime;

    public WorkflowRecoveryCoordinator(
            WorkflowDefinitionRepository definitionRepository,
            ExecutionRepository executionRepository,
            NodeExecutor nodeExecutor,
            ExecutionLock executionLock,
            IdGenerator idGenerator,
            Clock clock) {
        this.runtime = new WorkflowRecoveryRuntime(
                definitionRepository,
                executionRepository,
                nodeExecutor,
                executionLock,
                idGenerator,
                clock);
    }

    public WorkflowExecution recover(String executionId) {
        return runtime.recover(executionId);
    }
}
