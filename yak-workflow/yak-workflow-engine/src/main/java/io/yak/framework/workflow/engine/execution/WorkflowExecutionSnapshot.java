package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Durable, transport-neutral snapshot of a workflow execution aggregate. */
public record WorkflowExecutionSnapshot(
        String id,
        String definitionId,
        String sourceExecutionId,
        Map<String, Object> input,
        List<NodeExecutionSnapshot> nodes,
        Instant createdAt,
        WorkflowExecutionStatus status,
        boolean schedulingStopped,
        Instant runStartedAt,
        Instant pausedAt,
        Duration pausedDuration,
        Instant updatedAt,
        Instant endedAt) {

    public WorkflowExecutionSnapshot {
        id = requireText(id, "id");
        definitionId = requireText(definitionId, "definitionId");
        input = ExecutionValueSnapshot.immutableMap(input);
        nodes = nodes == null ? List.of() : List.copyOf(nodes);
        createdAt = Objects.requireNonNull(createdAt, "createdAt");
        status = Objects.requireNonNull(status, "status");
        pausedDuration = pausedDuration == null ? Duration.ZERO : pausedDuration;
        if (pausedDuration.isNegative()) {
            throw new IllegalArgumentException("pausedDuration must not be negative");
        }
        updatedAt = Objects.requireNonNullElse(updatedAt, createdAt);
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
