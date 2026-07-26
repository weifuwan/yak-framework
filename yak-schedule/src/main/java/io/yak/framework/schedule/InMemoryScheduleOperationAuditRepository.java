package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ScheduleOperationAudit;
import java.util.ArrayDeque;
import java.util.List;

public final class InMemoryScheduleOperationAuditRepository implements ScheduleOperationAuditRepository {
  private final int capacity;
  private final ArrayDeque<ScheduleOperationAudit> audits = new ArrayDeque<>();
  public InMemoryScheduleOperationAuditRepository(int capacity) { this.capacity = capacity; }
  @Override public synchronized void save(ScheduleOperationAudit audit) {
    audits.addFirst(audit);
    while (audits.size() > capacity) audits.removeLast();
  }
  @Override public synchronized List<ScheduleOperationAudit> find(String project, String taskName) {
    return audits.stream().filter(it -> it.project().equals(project) && it.taskName().equals(taskName)).toList();
  }
}
