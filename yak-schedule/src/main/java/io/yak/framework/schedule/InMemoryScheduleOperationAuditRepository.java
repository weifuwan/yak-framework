package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ScheduleOperationAudit;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于内存的调度操作审计仓库。
 *
 * <p>审计记录按照最新操作优先的顺序保存，并根据指定容量
 * 自动淘汰最早的记录。</p>
 *
 * <p>该实现仅适用于单应用实例，应用重启后审计数据会丢失。
 * 如需持久化或多实例共享，应提供数据库等其他仓库实现。</p>
 *
 * @author weifuwan
 */
public final class InMemoryScheduleOperationAuditRepository
        implements ScheduleOperationAuditRepository {

    /**
     * 默认最大容量。
     */
    private static final int DEFAULT_CAPACITY = 1000;

    /**
     * 审计记录最大容量。
     */
    private final int capacity;

    /**
     * 内存中的审计记录队列。
     *
     * <p>队首保存最新记录，队尾保存最早记录。</p>
     */
    private final ArrayDeque<ScheduleOperationAudit> audits =
            new ArrayDeque<>();

    /**
     * 使用默认容量创建审计仓库。
     */
    public InMemoryScheduleOperationAuditRepository() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * 创建审计仓库。
     *
     * @param capacity 最大记录数量，必须大于 0
     */
    public InMemoryScheduleOperationAuditRepository(
            int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "capacity must be greater than 0");
        }

        this.capacity = capacity;
    }

    /**
     * 保存调度操作审计记录。
     *
     * <p>新记录添加到队首。当记录数量超过最大容量时，
     * 从队尾移除最早的记录。</p>
     *
     * @param audit 操作审计记录
     */
    @Override
    public synchronized void save(
            ScheduleOperationAudit audit) {

        Objects.requireNonNull(
                audit,
                "audit must not be null");

        audits.addFirst(audit);

        while (audits.size() > capacity) {
            audits.removeLast();
        }
    }

    /**
     * 查询指定任务的操作审计记录。
     *
     * <p>返回结果按照操作时间倒序排列，即最新记录优先。</p>
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @return 不可修改的操作审计记录列表
     */
    @Override
    public synchronized List<ScheduleOperationAudit> find(
            String project,
            String taskName) {

        List<ScheduleOperationAudit> result =
                audits.stream()
                        .filter(audit ->
                                Objects.equals(
                                        audit.getProject(),
                                        project)
                                        && Objects.equals(
                                        audit.getTaskName(),
                                        taskName))
                        .collect(Collectors.toList());

        return Collections.unmodifiableList(result);
    }
}
