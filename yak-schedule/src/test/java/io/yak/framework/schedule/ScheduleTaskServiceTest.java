package io.yak.framework.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import io.yak.framework.schedule.model.ConcurrencyPolicy;
import io.yak.framework.schedule.model.ScheduleTaskDefinition;
import java.time.ZoneId;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

class ScheduleTaskServiceTest {
  private Scheduler scheduler;

  @AfterEach void stop() throws Exception { if (scheduler != null) scheduler.shutdown(true); }

  @Test void scopesSameTaskNameByProjectAndAuditsOperator() throws Exception {
    scheduler = StdSchedulerFactory.getDefaultScheduler();
    var logs = new InMemoryScheduleExecutionLogRepository(20);
    var audits = new InMemoryScheduleOperationAuditRepository(20);
    var service = new ScheduleTaskService(scheduler, () -> "alice", logs, audits);
    service.save(definition("project-a"));
    service.save(definition("project-b"));
    service.pause("project-a", "settle");
    assertThat(scheduler.checkExists(JobKey.jobKey("settle", "project-a"))).isTrue();
    assertThat(scheduler.checkExists(JobKey.jobKey("settle", "project-b"))).isTrue();
    assertThat(audits.find("project-a", "settle")).extracting("operation", "operator")
        .containsExactly(org.assertj.core.groups.Tuple.tuple("PAUSE", "alice"),
            org.assertj.core.groups.Tuple.tuple("SAVE", "alice"));
  }

  @Test void selectsNonConcurrentJobAndCronTimeZone() throws Exception {
    scheduler = StdSchedulerFactory.getDefaultScheduler();
    var service = new ScheduleTaskService(scheduler, () -> "alice",
        new InMemoryScheduleExecutionLogRepository(20), new InMemoryScheduleOperationAuditRepository(20));
    service.save(definition("project-a"));
    assertThat(scheduler.getJobDetail(JobKey.jobKey("settle", "project-a")).getJobClass())
        .isEqualTo(NonConcurrentQuartzTaskJob.class);
    var trigger = (org.quartz.CronTrigger) scheduler.getTrigger(org.quartz.TriggerKey.triggerKey("settle", "project-a"));
    assertThat(trigger.getCronExpression()).isEqualTo("0 0/5 * * * ?");
    assertThat(trigger.getTimeZone().toZoneId()).isEqualTo(ZoneId.of("Asia/Shanghai"));
    assertThat(service.get("project-a", "settle")).isEqualTo(definition("project-a"));
  }

  private static ScheduleTaskDefinition definition(String project) {
    return new ScheduleTaskDefinition(project, "settle", "settlement", "billing",
        "0 0/5 * * * ?", ZoneId.of("Asia/Shanghai"), ConcurrencyPolicy.FORBID, 2, Map.of("tenant", "1"));
  }
}
