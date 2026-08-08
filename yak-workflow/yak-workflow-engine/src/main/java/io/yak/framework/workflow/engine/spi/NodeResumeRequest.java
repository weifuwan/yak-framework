package io.yak.framework.workflow.engine.spi;

/** Attempt-scoped resume request sent to an executor that previously accepted pause. */
public record NodeResumeRequest(
        String workflowExecutionId,
        String nodeExecutionId,
        String nodeId,
        String attemptId) {
}
