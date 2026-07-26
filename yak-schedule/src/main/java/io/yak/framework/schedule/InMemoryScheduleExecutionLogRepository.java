package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ScheduleExecutionLog;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public final class InMemoryScheduleExecutionLogRepository implements ScheduleExecutionLogRepository {
  private final int capacity;
  private final ArrayDeque<ScheduleExecutionLog> logs = new ArrayDeque<>();

  public InMemoryScheduleExecutionLogRepository(int capacity) {
    if (capacity < 1) throw new IllegalArgumentException("capacity must be positive");
    this.capacity = capacity;
  }

  @Override public synchronized void save(ScheduleExecutionLog log) {
    logs.addFirst(log);
    while (logs.size() > capacity) logs.removeLast();
  }

  @Override public synchronized List<ScheduleExecutionLog> find(String project, String taskName) {
    return logs.stream().filter(log -> log.project().equals(project) && log.taskName().equals(taskName)).toList();
  }
}
