package io.yak.framework.workflow.engine.api;

import io.yak.framework.workflow.engine.command.WorkflowCommand;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import java.util.Map;
import java.util.Optional;

public interface WorkflowEngine {

    void registerDefinition(WorkflowDefinition definition);

    WorkflowExecution start(String definitionId, Map<String, Object> input);

    /**
     * Submit an immutable command for one existing execution.
     *
     * <p>The default keeps source compatibility for third-party engine implementations that only
     * implement the legacy convenience methods. The built-in engine overrides this and routes commands
     * through its execution mailbox.</p>
     */
    default WorkflowExecution submit(WorkflowCommand command) {
        throw new UnsupportedOperationException("This workflow engine does not expose command submission");
    }

    /**
     * Reconcile one already persisted execution after host restart.
     * Existing attempts keep their identities; recovery never creates a replacement attempt merely
     * because the host process restarted.
     */
    default WorkflowExecution recover(String executionId) {
        return submit(new WorkflowCommand.RecoverWorkflow(executionId));
    }

    /**
     * Acknowledge that one concrete node attempt started running.
     * Stale or duplicate callbacks are ignored and return the current execution snapshot.
     */
    default WorkflowExecution acknowledgeNodeStarted(
            String executionId, String nodeId, String attemptId) {
        return submit(new WorkflowCommand.NodeStarted(executionId, nodeId, attemptId));
    }

    /** Request workflow pause. Active attempts are paused when supported by their executor. */
    default WorkflowExecution pause(String executionId, String reason) {
        return submit(new WorkflowCommand.PauseWorkflow(executionId, reason));
    }

    /** Acknowledge that one concrete attempt accepted earlier has reached PAUSED. */
    default WorkflowExecution acknowledgeNodePaused(
            String executionId, String nodeId, String attemptId) {
        return submit(new WorkflowCommand.NodePaused(executionId, nodeId, attemptId));
    }

    /** Resume a fully paused workflow. */
    default WorkflowExecution resume(String executionId) {
        return submit(new WorkflowCommand.ResumeWorkflow(executionId));
    }

    /** Acknowledge that one concrete paused attempt has resumed its pre-pause state. */
    default WorkflowExecution acknowledgeNodeResumed(
            String executionId, String nodeId, String attemptId) {
        return submit(new WorkflowCommand.NodeResumed(executionId, nodeId, attemptId));
    }

    /**
     * Complete one concrete node attempt successfully.
     * Only the current active attempt may mutate workflow state; duplicate, stale, or conflicting
     * callbacks are idempotent no-ops.
     */
    default WorkflowExecution completeNode(
            String executionId,
            String nodeId,
            String attemptId,
            Map<String, Object> output) {
        return submit(new WorkflowCommand.NodeSucceeded(executionId, nodeId, attemptId, output));
    }

    /**
     * Complete one concrete node attempt with failure.
     * Only the current active attempt may mutate workflow state; duplicate, stale, or conflicting
     * callbacks are idempotent no-ops.
     */
    default WorkflowExecution failNode(
            String executionId,
            String nodeId,
            String attemptId,
            String errorMessage) {
        return submit(new WorkflowCommand.NodeFailed(
                executionId, nodeId, attemptId, errorMessage));
    }

    /**
     * Evaluate workflow, dispatch, and running-node timeouts against the engine clock.
     * Hosts should call this periodically for active executions; the engine itself owns no timer thread.
     */
    default WorkflowExecution checkTimeouts(String executionId) {
        return submit(new WorkflowCommand.CheckTimeouts(executionId));
    }

    default WorkflowExecution continueAfterFailure(String executionId, String nodeId) {
        return submit(new WorkflowCommand.ContinueAfterFailure(executionId, nodeId));
    }

    default WorkflowExecution retryFailedNode(String executionId, String nodeId) {
        return submit(new WorkflowCommand.RetryFailedNode(executionId, nodeId));
    }

    default WorkflowExecution cancel(String executionId, String reason) {
        return submit(new WorkflowCommand.CancelWorkflow(executionId, reason));
    }

    default WorkflowExecution retryFailedNodes(String executionId) {
        return submit(new WorkflowCommand.RetryFailedNodes(executionId));
    }

    default WorkflowExecution restart(String sourceExecutionId) {
        return submit(new WorkflowCommand.RestartWorkflow(sourceExecutionId));
    }

    default WorkflowExecution rerunFromNode(String sourceExecutionId, String nodeId) {
        return submit(new WorkflowCommand.RerunFromNode(sourceExecutionId, nodeId));
    }

    Optional<WorkflowExecution> findExecution(String executionId);
}
