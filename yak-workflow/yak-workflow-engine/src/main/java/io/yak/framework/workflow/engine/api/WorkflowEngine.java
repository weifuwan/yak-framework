package io.yak.framework.workflow.engine.api;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import java.util.Map;
import java.util.Optional;

public interface WorkflowEngine {

    void registerDefinition(WorkflowDefinition definition);

    WorkflowExecution start(String definitionId, Map<String, Object> input);

    /**
     * Acknowledge that one concrete node attempt started running.
     * Stale or duplicate callbacks are ignored and return the current execution snapshot.
     */
    WorkflowExecution acknowledgeNodeStarted(
            String executionId, String nodeId, String attemptId);

    /** Request workflow pause. Active attempts are paused when supported by their executor. */
    WorkflowExecution pause(String executionId, String reason);

    /** Acknowledge that one concrete attempt accepted earlier has reached PAUSED. */
    WorkflowExecution acknowledgeNodePaused(
            String executionId, String nodeId, String attemptId);

    /** Resume a fully paused workflow. */
    WorkflowExecution resume(String executionId);

    /** Acknowledge that one concrete paused attempt has resumed its pre-pause state. */
    WorkflowExecution acknowledgeNodeResumed(
            String executionId, String nodeId, String attemptId);

    /**
     * Complete one concrete node attempt successfully.
     * Only the current active attempt may mutate workflow state; duplicate, stale, or conflicting
     * callbacks are idempotent no-ops.
     */
    WorkflowExecution completeNode(
            String executionId,
            String nodeId,
            String attemptId,
            Map<String, Object> output);

    /**
     * Complete one concrete node attempt with failure.
     * Only the current active attempt may mutate workflow state; duplicate, stale, or conflicting
     * callbacks are idempotent no-ops.
     */
    WorkflowExecution failNode(
            String executionId,
            String nodeId,
            String attemptId,
            String errorMessage);

    /**
     * Evaluate workflow, dispatch, and running-node timeouts against the engine clock.
     * Hosts should call this periodically for active executions; the engine itself owns no timer thread.
     */
    WorkflowExecution checkTimeouts(String executionId);

    WorkflowExecution continueAfterFailure(String executionId, String nodeId);

    WorkflowExecution retryFailedNode(String executionId, String nodeId);

    WorkflowExecution cancel(String executionId, String reason);

    WorkflowExecution retryFailedNodes(String executionId);

    WorkflowExecution restart(String sourceExecutionId);

    WorkflowExecution rerunFromNode(String sourceExecutionId, String nodeId);

    Optional<WorkflowExecution> findExecution(String executionId);
}
