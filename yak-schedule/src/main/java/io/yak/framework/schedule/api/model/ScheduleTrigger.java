package io.yak.framework.schedule.api.model;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

/** Provider-neutral time trigger. */
public final class ScheduleTrigger {

    private final TriggerType type;
    private final String cron;
    private final ZoneId zoneId;
    private final Instant executeAt;

    private ScheduleTrigger(
            TriggerType type,
            String cron,
            ZoneId zoneId,
            Instant executeAt) {

        this.type = Objects.requireNonNull(type, "type must not be null");
        this.cron = cron;
        this.zoneId = zoneId;
        this.executeAt = executeAt;
        validate();
    }

    public static ScheduleTrigger cron(String expression, ZoneId zoneId) {
        return new ScheduleTrigger(
                TriggerType.CRON,
                requireText(expression, "expression"),
                zoneId == null ? ZoneId.systemDefault() : zoneId,
                null);
    }

    public static ScheduleTrigger oneTime(Instant executeAt) {
        return new ScheduleTrigger(
                TriggerType.ONE_TIME,
                null,
                null,
                Objects.requireNonNull(executeAt, "executeAt must not be null"));
    }

    public TriggerType getType() {
        return type;
    }

    public String getCron() {
        return cron;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public Instant getExecuteAt() {
        return executeAt;
    }

    private void validate() {
        if (type == TriggerType.CRON && (cron == null || cron.trim().isEmpty())) {
            throw new IllegalArgumentException("cron must not be blank");
        }
        if (type == TriggerType.ONE_TIME && executeAt == null) {
            throw new IllegalArgumentException("executeAt must not be null");
        }
    }

    private static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
