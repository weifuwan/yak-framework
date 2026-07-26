package io.yak.framework.schedule.model;

import java.time.Instant;

/** Records who changed scheduler state and when. */
public record ScheduleOperationAudit(
    String project, String taskName, String operation, String operator, Instant operatedAt) {}
