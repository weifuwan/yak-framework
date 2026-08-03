package io.yak.framework.schedule.api.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Business handler and serializable payload invoked by a schedule. */
public final class ScheduleTarget {

    private final String handler;
    private final Map<String, String> payload;

    public ScheduleTarget(String handler, Map<String, String> payload) {
        if (handler == null || handler.trim().isEmpty()) {
            throw new IllegalArgumentException("handler must not be blank");
        }
        this.handler = handler.trim();
        this.payload = immutable(payload);
    }

    public String getHandler() {
        return handler;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    private static Map<String, String> immutable(Map<String, String> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<>(source));
    }
}
