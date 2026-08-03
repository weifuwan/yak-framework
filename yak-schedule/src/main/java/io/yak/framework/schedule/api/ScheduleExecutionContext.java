package io.yak.framework.schedule.api;

import io.yak.framework.schedule.api.model.ScheduleKey;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Immutable context passed to a business schedule handler. */
public final class ScheduleExecutionContext {

    private final String triggerId;
    private final ScheduleKey scheduleKey;
    private final String handler;
    private final Instant scheduledFireTime;
    private final Instant actualFireTime;
    private final boolean manual;
    private final int attempt;
    private final Map<String, String> payload;
    private final Map<String, String> attributes;

    public ScheduleExecutionContext(
            String triggerId,
            ScheduleKey scheduleKey,
            String handler,
            Instant scheduledFireTime,
            Instant actualFireTime,
            boolean manual,
            int attempt,
            Map<String, String> payload,
            Map<String, String> attributes) {

        this.triggerId = requireText(triggerId, "triggerId");
        this.scheduleKey = Objects.requireNonNull(
                scheduleKey,
                "scheduleKey must not be null");
        this.handler = requireText(handler, "handler");
        this.scheduledFireTime = scheduledFireTime;
        this.actualFireTime = actualFireTime == null
                ? Instant.now()
                : actualFireTime;
        this.manual = manual;
        this.attempt = Math.max(1, attempt);
        this.payload = immutable(payload);
        this.attributes = immutable(attributes);
    }

    public String getTriggerId() {
        return triggerId;
    }

    public ScheduleKey getScheduleKey() {
        return scheduleKey;
    }

    public String getHandler() {
        return handler;
    }

    public Instant getScheduledFireTime() {
        return scheduledFireTime;
    }

    public Instant getActualFireTime() {
        return actualFireTime;
    }

    public boolean isManual() {
        return manual;
    }

    public int getAttempt() {
        return attempt;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getRequired(String name) {
        String value = payload.get(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing schedule payload: " + name);
        }
        return value;
    }

    public Long getRequiredLong(String name) {
        return Long.valueOf(getRequired(name));
    }

    private static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    private static Map<String, String> immutable(Map<String, String> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<>(source));
    }
}
