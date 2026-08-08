package io.yak.framework.workflow.engine.spi;

public interface NodeExecutor {

    void submit(NodeDispatch dispatch);

    default void cancel(NodeCancellation cancellation) {
        // Implement when the underlying executor supports cancellation.
    }

    /**
     * Request pausing one concrete attempt.
     * Returning ACCEPTED means the executor must later acknowledge the pause through
     * WorkflowEngine.acknowledgeNodePaused(...). Returning UNSUPPORTED leaves the attempt running
     * naturally while the workflow remains PAUSING.
     */
    default NodeControlResult pause(NodePauseRequest request) {
        return NodeControlResult.UNSUPPORTED;
    }

    /**
     * Resume one concrete attempt whose pause was previously accepted.
     * The executor must later acknowledge the resumed state through
     * WorkflowEngine.acknowledgeNodeResumed(...).
     */
    default void resume(NodeResumeRequest request) {
        // Implement when pause(...) may return ACCEPTED.
    }
}
