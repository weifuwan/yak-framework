package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.state.NodeAttemptStatus;
import java.time.Instant;
import java.util.Objects;

public final class NodeAttempt {

    private final String id;
    private final int attemptNumber;
    private final Instant availableAt;
    private NodeAttemptStatus status;
    private Instant startedAt;
    private Instant endedAt;
    private String errorMessage;

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
        this.startedAt = source.startedAt;
        this.endedAt = source.endedAt;
        this.errorMessage = source.errorMessage;
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

    public Instant startedAt() {
        return startedAt;
    }

    public Instant endedAt() {
        return endedAt;
    }

    public String errorMessage() {
        return errorMessage;
    }

    public void markRunning(Instant now) {
        if (status != NodeAttemptStatus.SUBMITTED) {
            throw new IllegalStateException("Only a submitted attempt can start");
        }
        status = NodeAttemptStatus.RUNNING;
        startedAt = now;
    }

    public void markSuccess(Instant now) {
        requireActive();
        status = NodeAttemptStatus.SUCCESS;
        endedAt = now;
    }

    public void markFailure(String errorMessage, Instant now) {
        requireActive();
        status = NodeAttemptStatus.FAILED;
        this.errorMessage = errorMessage;
        endedAt = now;
    }

    public void markCanceled(Instant now) {
        requireActive();
        status = NodeAttemptStatus.CANCELED;
        endedAt = now;
    }

    public NodeAttempt copy() {
        return new NodeAttempt(this);
    }

    private void requireActive() {
        if (status != NodeAttemptStatus.SUBMITTED && status != NodeAttemptStatus.RUNNING) {
            throw new IllegalStateException("Attempt is already terminal: " + status);
        }
    }
}
