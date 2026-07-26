package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ScheduleExecutionLog;

import java.util.List;

/**
 * Replaceable execution log store; production applications can persist logs in a database.
 */
public interface ScheduleExecutionLogRepository {
    void save(ScheduleExecutionLog log);

    List<ScheduleExecutionLog> find(String project, String taskName);
}
