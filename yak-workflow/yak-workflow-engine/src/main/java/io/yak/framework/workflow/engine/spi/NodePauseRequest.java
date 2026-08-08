package io.yak.framework.workflow.engine.spi;

/** Attempt-scoped pause request sent to an executor. */
public record NodePauseRequest(
        String workflowExecutionId,
        String nodeExecutionId,
        String nodeId,
        String attemptId,
        String reason) {
}
