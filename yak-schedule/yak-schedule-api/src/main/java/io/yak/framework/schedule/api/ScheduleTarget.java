package io.yak.framework.schedule.api;

import java.util.LinkedHashMap;
import java.util.Map;

/** 调度触发后需要调用的业务处理器及其参数。 */
public record ScheduleTarget(String handler, Map<String, Object> payload) {
    public ScheduleTarget {
        if (handler == null || handler.isBlank()) {
            throw new IllegalArgumentException("handler must not be blank");
        }
        handler = handler.trim();
        payload = payload == null
                ? Map.of()
                : Map.copyOf(new LinkedHashMap<>(payload));
    }
}
