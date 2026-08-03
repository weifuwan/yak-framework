package io.yak.framework.schedule.plugin.quartz;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleEngineTypes;
import io.yak.framework.schedule.api.ScheduleExecutionContext;
import io.yak.framework.schedule.core.ScheduleExecutionDispatcher;
import java.time.Instant;
import java.util.UUID;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/** Quartz 到 Yak Schedule Handler 的执行桥接器。 */
public class QuartzScheduleJob implements Job {

    @Autowired
    private ScheduleExecutionDispatcher dispatcher;

    @Autowired
    @Qualifier("yakScheduleObjectMapper")
    private ObjectMapper objectMapper;

    @Override
    public void execute(JobExecutionContext quartzContext)
            throws JobExecutionException {
        JobDataMap data = quartzContext.getMergedJobDataMap();
        try {
            ScheduleDefinition definition = objectMapper.readValue(
                    data.getString(QuartzScheduleConstants.DEFINITION_JSON),
                    ScheduleDefinition.class);
            String manualTriggerId = data.getString(
                    QuartzScheduleConstants.MANUAL_TRIGGER_ID);
            boolean manual = manualTriggerId != null && !manualTriggerId.isBlank();
            String triggerId = manual ? manualTriggerId : UUID.randomUUID().toString();

            dispatcher.dispatch(new ScheduleExecutionContext(
                    triggerId,
                    definition.key(),
                    ScheduleEngineTypes.QUARTZ,
                    definition.target().handler(),
                    definition.target().payload(),
                    instant(quartzContext.getScheduledFireTime()),
                    instant(quartzContext.getFireTime()),
                    manual,
                    quartzContext.getRefireCount() + 1));
        } catch (Exception exception) {
            JobExecutionException failure = new JobExecutionException(exception);
            int maxRetries = maxRetries(data);
            failure.setRefireImmediately(
                    quartzContext.getRefireCount() < maxRetries);
            throw failure;
        }
    }

    private int maxRetries(JobDataMap data) {
        try {
            ScheduleDefinition definition = objectMapper.readValue(
                    data.getString(QuartzScheduleConstants.DEFINITION_JSON),
                    ScheduleDefinition.class);
            return definition.policy().triggerRetries();
        } catch (Exception ignored) {
            return 0;
        }
    }

    private static Instant instant(java.util.Date value) {
        return value == null ? Instant.now() : value.toInstant();
    }
}
