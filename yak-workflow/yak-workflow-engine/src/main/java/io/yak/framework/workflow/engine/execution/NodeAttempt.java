package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.state.NodeAttemptFailureReason;
import io.yak.framework.workflow.engine.state.NodeAttemptStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class NodeAttempt {

    private final String id;
    private final int attemptNumber;
    private final Instant availableAt;
    private NodeAttemptStatus status;
    private NodeAttemptStatus resumeTargetStatus;
    private Instant startedAt;
    private Instant pausedAt;
    private Duration pausedDuration = Duration.ZERO;
    private Instant endedAt;
    private String errorMessage;
    private NodeAttemptFailureReason failureReason;

    public NodeAttempt(String id, int attemptNumber, Instant availableAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.attemptNumber = attemptNumber;
        this.availableAt = Objects.requireNonNull(availableAt, "availableAt");
        this.status = NodeAttemptStatus.SUBMITTED;
    }

    private NodeAttempt(NodeAttempt source) {
        this.id = source.id;
        this.attemptNumber = source.attemptNumber;
        this.availableAt = source.availableAt;
        this.status = source.status;
        this.resumeTargetStatus = source.resumeTargetStatus;
        this.startedAt = source.startedAt;
        this.pausedAt = source.pausedAt;
        this.pausedDuration = source.pausedDuration;
        this.endedAt = source.endedAt;
        this.errorMessage = source.errorMessage;
        this.failureReason = source.failureReason;
    }

    public String id() {
        return id;
    }

    public int attemptNumber() {
        return attemptNumber;
    }

    public Instant availableAt() {
        return availableAt;
    }

    public NodeAttemptStatus status() {
        return status;
    }

    public NodeAttemptStatus resumeTargetStatus() {
        return resumeTargetStatus;
    }

    public Instant startedAt() {
        return startedAt;
    }

    public Instant pausedAt() {
        return pausedAt;
    }

    public Duration pausedDuration() {
        return pausedDuration;
    }

    public Instant endedAt() {
        return endedAt;
    }

    public String errorMessage() {
        return errorMessage;
    }

    public NodeAttemptFailureReason failureReason() {
        return failureReason;
    }

    public void markRunning(Instant now) {
        if (status != NodeAttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Only a submitted attempt can start");
        }
        status = NodeAttemptStatus.RUNNING;
        startedAt = now;
    }

    public void markPausing() {
        if (status != NodeAttemptStatus.SUBMITTED && status != NodeAttemptStatus.RUNNING) {
            throw new IllegalStateException("Only submitted or running attempts can pause");
        }
        resumeTargetStatus = status;
        status = NodeAttemptStatus.PAUSING;
    }

    public void markPaused(Instant now) {
        if (status != NodeAttemptStatus.PAUSING) {
            throw new IllegalStateException("Only a pausing attempt can acknowledge pause");
        }
        status = NodeAttemptStatus.PAUSED;
        pausedAt = now;
    }

    public void markResuming() {
        if (status != NodeAttemptStatus.PAUSED) {
            throw new IllegalStateException("Only a paused attempt can resume");
        }
        status = NodeAttemptStatus.RESUMING;
    }

    public NodeAttemptStatus markResumed(Instant now) {
        if (status != NodeAttemptStatus.RESUMING) {
            throw new IllegalStateException("Only a resuming attempt can acknowledge resume");
        }
        if (pausedAt != null) {
            pausedDuration = pausedDuration.plus(Duration.between(pausedAt, now));
            pausedAt = null;
        }
        NodeAttemptStatus target = Objects.requireNonNull(
                resumeTargetStatus, "resumeTargetStatus");
        status = target;
        resumeTargetStatus = null;
        return target;
    }

    public Instant dispatchDeadline(Duration dispatchTimeout) {
        if (dispatchTimeout == null || dispatchTimeout.isZero()) {
            return null;
        }
        return availableAt.plus(dispatchTimeout).plus(pausedDuration);
    }

    public Instant executionDeadline(Duration executionTimeout) {
        if (startedAt == null || executionTimeout == null || executionTimeout.isZero()) {
            return null;
        }
        return startedAt.plus(executionTimeout).plus(pausedDuration);
    }

    public void markSuccess(Instant now) {
        requireCallbackActive();
        status = NodeAttemptStatus.SUCCESS;
        endedAt = now;
    }

    public void markFailure(String errorMessage, Instant now) {
        markFailure(NodeAttemptFailureReason.EXECUTOR_FAILURE, errorMessage, now);
    }

    public void markFailure(
            NodeAttemptFailureReason failureReason,
            String errorMessage,
            Instant now) {
        requireCallbackActive();
        status = NodeAttemptStatus.FAILED;
        this.failureReason = Objects.requireNonNull(failureReason, "failureReason");
        this.errorMessage = errorMessage;
        endedAt = now;
    }

    public void markCanceled(Instant now) {
        requireNonTerminal();
        status = NodeAttemptStatus.CANCELED;
        endedAt = now;
    }

    public NodeAttempt copy() {
        return new NodeAttempt(this);
    }

    private void requireCallbackActive() {
        if (status != NodeAttemptStatus.SUBMITTED
                && status != NodeAttemptStatus.RUNNING
                && status != NodeAttemptStatus.PAUSING
                && status != NodeAttemptStatus.RESUMING) {
            throw new IllegalStateException("Attempt is not callback-active: " + status);
        }
    }

    private void requireNonTerminal() {
        if (status == NodeAttemptStatus.SUCCESS
                || status == NodeAttemptStatus.FAILED
                || status == NodeAttemptStatus.CANCELED) {
            throw new IllegalStateException("Attempt is already terminal: " + status);
        }
    }
}
