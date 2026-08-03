package io.yak.framework.schedule.api;

import java.time.Instant;

/** 一次调度触发调用业务入口的执行日志。 */
public record ScheduleExecutionLog(
        String triggerId,
        ScheduleKey key,
        String engineType,
        String handler,
        Instant startedAt,
        Instant finishedAt,
        ExecutionStatus status,
        int attempt,
        String businessExecutionId,
        String message) {
}
