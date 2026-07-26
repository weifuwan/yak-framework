package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ExecutionStatus;
import io.yak.framework.schedule.model.ScheduleExecutionLog;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/** Quartz bridge which resolves application handlers and applies retry/logging policy. */
public class QuartzTaskJob implements Job {
  @Autowired private ApplicationContext applicationContext;
  @Autowired private ScheduleExecutionLogRepository logRepository;

  @Override public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap data = context.getMergedJobDataMap();
    String project = data.getString("project");
    String task = data.getString("task");
    String operator = data.getString("operator");
    if (operator == null) operator = "SYSTEM";
    int attempt = context.getRefireCount() + 1;
    int maxRetries = data.getInt("maxRetries");
    String executionId = UUID.randomUUID().toString();
    Instant started = Instant.now();
    try {
      ScheduleTaskHandler handler = applicationContext.getBean(data.getString("handler"), ScheduleTaskHandler.class);
      Map<String, String> parameters = new HashMap<>();
      data.forEach((key, value) -> {
        if (key.startsWith("parameter.")) parameters.put(key.substring(10), String.valueOf(value));
      });
      handler.execute(Map.copyOf(parameters));
      logRepository.save(new ScheduleExecutionLog(executionId, project, task, operator, started,
          Instant.now(), ExecutionStatus.SUCCEEDED, attempt, null));
    } catch (Exception exception) {
      logRepository.save(new ScheduleExecutionLog(executionId, project, task, operator, started,
          Instant.now(), ExecutionStatus.FAILED, attempt, safeMessage(exception)));
      JobExecutionException failure = new JobExecutionException(exception);
      failure.setRefireImmediately(context.getRefireCount() < maxRetries);
      throw failure;
    }
  }

  private static String safeMessage(Exception exception) {
    String value = exception.getMessage();
    return value == null ? exception.getClass().getSimpleName() : value.substring(0, Math.min(value.length(), 2_000));
  }
}
