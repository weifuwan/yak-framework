package io.yak.framework.workflow.engine.policy;

public record FailureHandlingResult(boolean stopScheduling, boolean terminateActiveNodes) {

    public static FailureHandlingResult continueExecution() {
        return new FailureHandlingResult(false, false);
    }
}
