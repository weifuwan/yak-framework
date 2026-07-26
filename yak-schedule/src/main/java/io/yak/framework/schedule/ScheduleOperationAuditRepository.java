package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ScheduleOperationAudit;
import java.util.List;

public interface ScheduleOperationAuditRepository {
  void save(ScheduleOperationAudit audit);
  List<ScheduleOperationAudit> find(String project, String taskName);
}
