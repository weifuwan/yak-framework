package io.yak.framework.schedule.api;

import java.time.Instant;

/** 调度引擎返回的计划快照。 */
public record ScheduleSnapshot(
        ScheduleDefinition definition,
        String engineType,
        String externalId,
        ScheduleStatus status,
        Instant nextFireTime,
        Instant lastFireTime) {
}
