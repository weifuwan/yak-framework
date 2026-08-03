package io.yak.framework.schedule.api;

import java.time.Instant;

/** 调度计划管理操作审计。 */
public record ScheduleOperationAudit(
        ScheduleKey key,
        String operation,
        String operator,
        Instant operatedAt) {
}
