package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.ScheduleOperationAudit;
import io.yak.framework.schedule.api.ScheduleOperationAuditRepository;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/** 有界内存调度操作审计仓库。 */
public final class InMemoryScheduleOperationAuditRepository
        implements ScheduleOperationAuditRepository {

    private final int capacity;
    private final ArrayDeque<ScheduleOperationAudit> audits = new ArrayDeque<>();

    public InMemoryScheduleOperationAuditRepository(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
    }

    @Override
    public synchronized void save(ScheduleOperationAudit audit) {
        audits.addFirst(audit);
        while (audits.size() > capacity) {
            audits.removeLast();
        }
    }

    @Override
    public synchronized List<ScheduleOperationAudit> find(ScheduleKey key) {
        List<ScheduleOperationAudit> result = new ArrayList<>();
        for (ScheduleOperationAudit audit : audits) {
            if (audit.key().equals(key)) {
                result.add(audit);
            }
        }
        return List.copyOf(result);
    }
}
