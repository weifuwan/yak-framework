package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.state.NodeAttemptFailureReason;
import io.yak.framework.workflow.engine.state.NodeAttemptStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/** Durable, transport-neutral snapshot of one concrete node attempt. */
public record NodeAttemptSnapshot(
        String id,
        int attemptNumber,
        Instant availableAt,
        NodeAttemptStatus status,
        NodeAttemptStatus resumeTargetStatus,
        Instant startedAt,
        Instant pausedAt,
        Duration pausedDuration,
        Instant endedAt,
        String errorMessage,
        NodeAttemptFailureReason failureReason) {

    public NodeAttemptSnapshot {
        Objects.requireNonNull(id, "id");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (attemptNumber < 1) {
            throw new IllegalArgumentException("attemptNumber must be >= 1");
        }
        Objects.requireNonNull(availableAt, "availableAt");
        Objects.requireNonNull(status, "status");
        pausedDuration = pausedDuration == null ? Duration.ZERO : pausedDuration;
        if (pausedDuration.isNegative()) {
            throw new IllegalArgumentException("pausedDuration must not be negative");
        }
    }
}
