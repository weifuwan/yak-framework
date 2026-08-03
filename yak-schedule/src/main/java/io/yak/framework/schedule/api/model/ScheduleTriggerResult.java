package io.yak.framework.schedule.api.model;

import java.time.Instant;

/** Result returned after a manual trigger was accepted by a provider. */
public final class ScheduleTriggerResult {

    private final String triggerId;
    private final Instant acceptedAt;

    public ScheduleTriggerResult(String triggerId, Instant acceptedAt) {
        this.triggerId = triggerId;
        this.acceptedAt = acceptedAt;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }
}
