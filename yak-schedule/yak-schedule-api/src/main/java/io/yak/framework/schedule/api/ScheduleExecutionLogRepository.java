package io.yak.framework.schedule.api;

import java.util.List;

/** 可替换的调度触发日志仓库。 */
public interface ScheduleExecutionLogRepository {
    void save(ScheduleExecutionLog log);

    List<ScheduleExecutionLog> find(ScheduleKey key);
}
