package io.yak.framework.schedule.api;

import java.util.Map;

/** Framework 调度标识与外部调度引擎任务标识的绑定。 */
public record ScheduleEngineBinding(
        ScheduleKey key,
        String engineType,
        String externalId,
        Map<String, String> attributes) {

    public ScheduleEngineBinding {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}
