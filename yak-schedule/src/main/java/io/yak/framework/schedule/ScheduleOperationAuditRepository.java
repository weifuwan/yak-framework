package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ScheduleOperationAudit;

import java.util.List;

/**
 * 调度任务操作审计仓库。
 *
 * <p>用于保存和查询调度任务的操作审计记录。业务系统可以提供
 * 内存、数据库或其他持久化实现。</p>
 *
 * @author weifuwan
 */
public interface ScheduleOperationAuditRepository {

    /**
     * 保存调度任务操作审计记录。
     *
     * @param audit 操作审计记录
     */
    void save(ScheduleOperationAudit audit);

    /**
     * 查询指定调度任务的操作审计记录。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @return 操作审计记录列表
     */
    List<ScheduleOperationAudit> find(
            String project,
            String taskName);
}
