package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ConcurrencyPolicy;
import io.yak.framework.schedule.model.ScheduleExecutionLog;
import io.yak.framework.schedule.model.ScheduleOperationAudit;
import io.yak.framework.schedule.model.ScheduleTaskDefinition;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;

/** Project-scoped facade for task lifecycle and execution controls. */
public class ScheduleTaskService {
  private final Scheduler scheduler;
  private final CurrentOperatorProvider operatorProvider;
  private final ScheduleExecutionLogRepository logRepository;
  private final ScheduleOperationAuditRepository auditRepository;

  public ScheduleTaskService(Scheduler scheduler, CurrentOperatorProvider operatorProvider,
      ScheduleExecutionLogRepository logRepository, ScheduleOperationAuditRepository auditRepository) {
    this.scheduler = scheduler;
    this.operatorProvider = operatorProvider;
    this.logRepository = logRepository;
    this.auditRepository = auditRepository;
  }

  public void save(ScheduleTaskDefinition definition) throws SchedulerException {
    JobKey key = key(definition.project(), definition.name());
    JobDataMap data = new JobDataMap();
    data.put("project", definition.project());
    data.put("task", definition.name());
    data.put("handler", definition.handler());
    data.put("description", definition.description() == null ? "" : definition.description());
    data.put("concurrencyPolicy", definition.concurrencyPolicy().name());
    data.put("maxRetries", definition.maxRetries());
    definition.parameters().forEach((name, value) -> data.put("parameter." + name, value));
    Class<? extends org.quartz.Job> jobClass = definition.concurrencyPolicy() == ConcurrencyPolicy.FORBID
        ? NonConcurrentQuartzTaskJob.class : QuartzTaskJob.class;
    JobDetail job = JobBuilder.newJob(jobClass).withIdentity(key).usingJobData(data).build();
    CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(definition.name(), definition.project())
        .forJob(job).withSchedule(CronScheduleBuilder.cronSchedule(definition.cron())
            .inTimeZone(java.util.TimeZone.getTimeZone(definition.zoneId()))).build();
    if (scheduler.checkExists(key)) scheduler.deleteJob(key);
    scheduler.scheduleJob(job, trigger);
    audit(definition.project(), definition.name(), "SAVE");
  }

  public void pause(String project, String taskName) throws SchedulerException {
    scheduler.pauseJob(required(project, taskName)); audit(project, taskName, "PAUSE");
  }
  public void resume(String project, String taskName) throws SchedulerException {
    scheduler.resumeJob(required(project, taskName)); audit(project, taskName, "RESUME");
  }
  public void runNow(String project, String taskName) throws SchedulerException {
    JobDataMap data = new JobDataMap(); data.put("operator", operator());
    scheduler.triggerJob(required(project, taskName), data); audit(project, taskName, "RUN_NOW");
  }
  public void delete(String project, String taskName) throws SchedulerException {
    scheduler.deleteJob(required(project, taskName)); audit(project, taskName, "DELETE");
  }
  public ScheduleTaskDefinition get(String project, String taskName) throws SchedulerException {
    return definition(required(project, taskName));
  }
  public List<ScheduleTaskDefinition> list(String project) throws SchedulerException {
    if (project == null || project.isBlank()) throw new IllegalArgumentException("project must not be blank");
    List<ScheduleTaskDefinition> definitions = new java.util.ArrayList<>();
    for (JobKey jobKey : scheduler.getJobKeys(org.quartz.impl.matchers.GroupMatcher.jobGroupEquals(project))) {
      definitions.add(definition(jobKey));
    }
    return List.copyOf(definitions);
  }
  public List<ScheduleExecutionLog> logs(String project, String taskName) { return logRepository.find(project, taskName); }
  public List<ScheduleOperationAudit> audits(String project, String taskName) { return auditRepository.find(project, taskName); }

  private JobKey required(String project, String taskName) throws SchedulerException {
    JobKey key = key(project, taskName);
    if (!scheduler.checkExists(key)) throw new IllegalArgumentException("Unknown schedule task: " + project + "/" + taskName);
    return key;
  }
  private static JobKey key(String project, String taskName) {
    if (project == null || project.isBlank() || taskName == null || taskName.isBlank())
      throw new IllegalArgumentException("project and taskName must not be blank");
    return JobKey.jobKey(taskName, project);
  }
  private ScheduleTaskDefinition definition(JobKey key) throws SchedulerException {
    JobDetail job = scheduler.getJobDetail(key);
    CronTrigger trigger = (CronTrigger) scheduler.getTriggersOfJob(key).stream()
        .filter(CronTrigger.class::isInstance).findFirst()
        .orElseThrow(() -> new IllegalStateException("Task has no Cron trigger: " + key));
    JobDataMap data = job.getJobDataMap();
    Map<String, String> parameters = data.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith("parameter."))
        .collect(Collectors.toUnmodifiableMap(entry -> entry.getKey().substring(10),
            entry -> String.valueOf(entry.getValue())));
    return new ScheduleTaskDefinition(key.getGroup(), key.getName(), data.getString("description"),
        data.getString("handler"), trigger.getCronExpression(), trigger.getTimeZone().toZoneId(),
        ConcurrencyPolicy.valueOf(data.getString("concurrencyPolicy")), data.getInt("maxRetries"), parameters);
  }
  private void audit(String project, String taskName, String operation) {
    auditRepository.save(new ScheduleOperationAudit(project, taskName, operation, operator(), Instant.now()));
  }
  private String operator() {
    String operator = operatorProvider.currentOperator();
    return operator == null || operator.isBlank() ? "UNKNOWN" : operator;
  }
}
