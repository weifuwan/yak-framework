package io.yak.framework.schedule.api.model;

import java.time.Instant;
import java.util.Objects;

/** Current provider-neutral runtime view of a schedule. */
public final class ScheduleSnapshot {

    private final ScheduleDefinition definition;
    private final ScheduleStatus status;
    private final Instant previousFireTime;
    private final Instant nextFireTime;
    private final String providerReference;

    public ScheduleSnapshot(
            ScheduleDefinition definition,
            ScheduleStatus status,
            Instant previousFireTime,
            Instant nextFireTime,
            String providerReference) {

        this.definition = Objects.requireNonNull(
                definition,
                "definition must not be null");
        this.status = status == null ? ScheduleStatus.UNKNOWN : status;
        this.previousFireTime = previousFireTime;
        this.nextFireTime = nextFireTime;
        this.providerReference = providerReference;
    }

    public ScheduleDefinition getDefinition() {
        return definition;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public Instant getPreviousFireTime() {
        return previousFireTime;
    }

    public Instant getNextFireTime() {
        return nextFireTime;
    }

    public String getProviderReference() {
        return providerReference;
    }
}
