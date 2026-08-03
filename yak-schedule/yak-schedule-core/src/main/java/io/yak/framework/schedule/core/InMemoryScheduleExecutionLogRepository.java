package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ScheduleExecutionLog;
import io.yak.framework.schedule.api.ScheduleExecutionLogRepository;
import io.yak.framework.schedule.api.ScheduleKey;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/** 有界内存调度触发日志仓库。 */
public final class InMemoryScheduleExecutionLogRepository
        implements ScheduleExecutionLogRepository {

    private final int capacity;
    private final ArrayDeque<ScheduleExecutionLog> logs = new ArrayDeque<>();

    public InMemoryScheduleExecutionLogRepository(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
    }

    @Override
    public synchronized void save(ScheduleExecutionLog log) {
        logs.addFirst(log);
        while (logs.size() > capacity) {
            logs.removeLast();
        }
    }

    @Override
    public synchronized List<ScheduleExecutionLog> find(ScheduleKey key) {
        List<ScheduleExecutionLog> result = new ArrayList<>();
        for (ScheduleExecutionLog log : logs) {
            if (log.key().equals(key)) {
                result.add(log);
            }
        }
        return List.copyOf(result);
    }
}
