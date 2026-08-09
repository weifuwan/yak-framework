package io.yak.framework.workflow.engine.spi;

import io.yak.framework.workflow.engine.state.NodeAttemptStatus;

public interface NodeExecutor {

    /**
     * Submit one concrete attempt to the underlying executor.
     *
     * <p>Executor lifecycle callbacks must not synchronously re-enter the same workflow execution
     * before this method returns. With the command mailbox model, callbacks belong to a later mailbox
     * turn so NODE_SUBMITTED and the surrounding state transition finish before NODE_STARTED or a
     * terminal callback is processed.
     */
    void submit(NodeDispatch dispatch);

    /**
     * Reconcile an already persisted attempt after host restart.
     *
     * <p>The default only re-submits SUBMITTED attempts with the original attempt id. This is safe for
     * idempotent executors and closes the crash window between persistence and remote submission.
     * RUNNING/PAUSED attempts are deliberately left to host-specific reconciliation so recovery never
     * creates a second remote execution accidentally.</p>
     */
    default void recover(NodeRecovery recovery) {
        if (recovery.attemptStatus() == NodeAttemptStatus.SUBMITTED) {
            submit(recovery.dispatch());
        }
    }

    default void cancel(NodeCancellation cancellation) {
        // Implement when the underlying executor supports cancellation.
    }

    /**
     * Request pausing one concrete attempt.
     * Returning ACCEPTED means the executor must later acknowledge the pause through
     * WorkflowEngine.acknowledgeNodePaused(...). Returning UNSUPPORTED leaves the attempt running
     * naturally while the workflow remains PAUSING.
     *
     * <p>The acknowledgement must be delivered after this call returns, not synchronously from inside
     * pause(...), so it becomes a separate mailbox command.
     */
    default NodeControlResult pause(NodePauseRequest request) {
        return NodeControlResult.UNSUPPORTED;
    }

    /**
     * Resume one concrete attempt whose pause was previously accepted.
     * The executor must later acknowledge the resumed state through
     * WorkflowEngine.acknowledgeNodeResumed(...).
     *
     * <p>The acknowledgement must be delivered after this call returns, not synchronously from inside
     * resume(...), so it becomes a separate mailbox command.
     */
    default void resume(NodeResumeRequest request) {
        // Implement when pause(...) may return ACCEPTED.
    }
}
