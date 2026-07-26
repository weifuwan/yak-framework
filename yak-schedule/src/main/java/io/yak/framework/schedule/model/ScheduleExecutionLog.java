package io.yak.framework.schedule.model;

import java.time.Instant;

/** Immutable execution audit event. */
public record ScheduleExecutionLog(
    String executionId, String project, String taskName, String operator,
    Instant startedAt, Instant finishedAt, ExecutionStatus status, int attempt, String message) {}
