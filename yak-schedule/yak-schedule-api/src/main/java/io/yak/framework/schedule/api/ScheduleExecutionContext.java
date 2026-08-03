package io.yak.framework.schedule.api;

import java.time.Instant;
import java.util.Map;

/** 调度引擎调用业务处理器时传递的统一上下文。 */
public record ScheduleExecutionContext(
        String triggerId,
        ScheduleKey key,
        String engineType,
        String handler,
        Map<String, Object> payload,
        Instant scheduledFireTime,
        Instant actualFireTime,
        boolean manual,
        int attempt) {

    public ScheduleExecutionContext {
        if (triggerId == null || triggerId.isBlank()) {
            throw new IllegalArgumentException("triggerId must not be blank");
        }
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (handler == null || handler.isBlank()) {
            throw new IllegalArgumentException("handler must not be blank");
        }
        payload = payload == null ? Map.of() : Map.copyOf(payload);
        actualFireTime = actualFireTime == null ? Instant.now() : actualFireTime;
        attempt = Math.max(1, attempt);
    }

    public Long requiredLong(String name) {
        Object value = payload.get(name);
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String text && !text.isBlank()) {
            return Long.valueOf(text);
        }
        throw new IllegalArgumentException("Missing required long payload: " + name);
    }

    public String requiredString(String name) {
        Object value = payload.get(name);
        if (value == null || String.valueOf(value).isBlank()) {
            throw new IllegalArgumentException("Missing required string payload: " + name);
        }
        return String.valueOf(value);
    }
}
