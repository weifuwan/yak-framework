package io.yak.framework.workflow.engine.state;

/** Why one node attempt ended in failure. */
public enum NodeAttemptFailureReason {
    EXECUTOR_FAILURE,
    DISPATCH_TIMEOUT,
    EXECUTION_TIMEOUT
}
