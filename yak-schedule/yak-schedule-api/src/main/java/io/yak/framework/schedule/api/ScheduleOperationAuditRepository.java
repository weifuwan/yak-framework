package io.yak.framework.schedule.api;

import java.util.List;

/** 可替换的调度操作审计仓库。 */
public interface ScheduleOperationAuditRepository {
    void save(ScheduleOperationAudit audit);

    List<ScheduleOperationAudit> find(ScheduleKey key);
}
