package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ScheduleExecutionLog;
import io.yak.framework.schedule.model.ScheduleOperationAudit;
import io.yak.framework.schedule.model.ScheduleTaskDefinition;
import java.util.List;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yak-schedule/api/v1/projects/{project}/tasks")
public class ScheduleTaskController {
  private final ScheduleTaskService service;
  public ScheduleTaskController(ScheduleTaskService service) { this.service = service; }

  @PutMapping("/{taskName}")
  public void save(@PathVariable String project, @PathVariable String taskName,
      @RequestBody ScheduleTaskDefinition request) throws SchedulerException {
    if (!project.equals(request.project()) || !taskName.equals(request.name()))
      throw new IllegalArgumentException("Path identity must match the task definition");
    service.save(request);
  }
  @GetMapping public List<ScheduleTaskDefinition> list(@PathVariable String project) throws SchedulerException {
    return service.list(project);
  }
  @GetMapping("/{taskName}") public ScheduleTaskDefinition get(@PathVariable String project,
      @PathVariable String taskName) throws SchedulerException { return service.get(project, taskName); }
  @PostMapping("/{taskName}/pause") public void pause(@PathVariable String project,
      @PathVariable String taskName) throws SchedulerException { service.pause(project, taskName); }
  @PostMapping("/{taskName}/resume") public void resume(@PathVariable String project,
      @PathVariable String taskName) throws SchedulerException { service.resume(project, taskName); }
  @PostMapping("/{taskName}/run-now") public void runNow(@PathVariable String project,
      @PathVariable String taskName) throws SchedulerException { service.runNow(project, taskName); }
  @DeleteMapping("/{taskName}") public void delete(@PathVariable String project,
      @PathVariable String taskName) throws SchedulerException { service.delete(project, taskName); }
  @GetMapping("/{taskName}/logs") public List<ScheduleExecutionLog> logs(@PathVariable String project,
      @PathVariable String taskName) { return service.logs(project, taskName); }
  @GetMapping("/{taskName}/audits") public List<ScheduleOperationAudit> audits(@PathVariable String project,
      @PathVariable String taskName) { return service.audits(project, taskName); }
}
