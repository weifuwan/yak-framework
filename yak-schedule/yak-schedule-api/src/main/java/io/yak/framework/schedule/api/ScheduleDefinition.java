package io.yak.framework.schedule.api;

import java.util.LinkedHashMap;
import java.util.Map;

/** 与具体调度引擎无关的完整调度定义。 */
public record ScheduleDefinition(
        ScheduleKey key,
        String description,
        ScheduleTrigger trigger,
        ScheduleTarget target,
        SchedulePolicy policy,
        boolean enabled,
        Map<String, String> metadata) {

    public ScheduleDefinition {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (trigger == null) {
            throw new IllegalArgumentException("trigger must not be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }
        policy = policy == null ? SchedulePolicy.defaults() : policy;
        metadata = metadata == null
                ? Map.of()
                : Map.copyOf(new LinkedHashMap<>(metadata));
    }

    /** 返回只变更启停状态的新定义，供统一生命周期操作保持定义仓库与引擎状态一致。 */
    public ScheduleDefinition withEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return this;
        }
        return new ScheduleDefinition(
                key,
                description,
                trigger,
                target,
                policy,
                enabled,
                metadata);
    }
}
