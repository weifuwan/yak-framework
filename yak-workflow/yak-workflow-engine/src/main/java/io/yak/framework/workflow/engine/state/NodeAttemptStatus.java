package io.yak.framework.workflow.engine.state;

public enum NodeAttemptStatus {
    SUBMITTED,
    RUNNING,
    PAUSING,
    PAUSED,
    RESUMING,
    SUCCESS,
    FAILED,
    CANCELED
}
