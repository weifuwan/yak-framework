package io.yak.framework.workflow.engine.event;

import java.time.Instant;

public record WorkflowEvent(
        Type type,
        String workflowExecutionId,
        String nodeId,
        String message,
        Instant occurredAt) {

    public enum Type {
        WORKFLOW_STARTED,
        NODE_SUBMITTED,
        NODE_STARTED,
        NODE_SUCCEEDED,
        NODE_FAILED,
        NODE_RETRY_SCHEDULED,
        WORKFLOW_COMPLETED,
        WORKFLOW_CANCELED
    }
}
