package io.yak.framework.schedule.api;

import java.time.Instant;
import java.time.ZoneId;

/** 通用调度触发器。 */
public record ScheduleTrigger(
        TriggerType type,
        String expression,
        ZoneId zoneId,
        Instant executeAt) {

    public ScheduleTrigger {
        if (type == null) {
            throw new IllegalArgumentException("trigger type must not be null");
        }
        if (type == TriggerType.CRON) {
            if (expression == null || expression.isBlank()) {
                throw new IllegalArgumentException("cron expression must not be blank");
            }
            expression = expression.trim();
            zoneId = zoneId == null ? ZoneId.systemDefault() : zoneId;
            executeAt = null;
        } else {
            if (executeAt == null) {
                throw new IllegalArgumentException("executeAt must not be null");
            }
            expression = null;
            zoneId = null;
        }
    }

    public static ScheduleTrigger cron(String expression, ZoneId zoneId) {
        return new ScheduleTrigger(TriggerType.CRON, expression, zoneId, null);
    }

    public static ScheduleTrigger oneTime(Instant executeAt) {
        return new ScheduleTrigger(TriggerType.ONE_TIME, null, null, executeAt);
    }
}
