package io.yak.framework.workflow.engine.spi;

public record NodeCancellation(
        String workflowExecutionId,
        String nodeExecutionId,
        String nodeId,
        String reason) {
}
