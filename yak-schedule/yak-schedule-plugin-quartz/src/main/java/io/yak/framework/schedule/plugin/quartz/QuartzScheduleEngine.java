package io.yak.framework.schedule.plugin.quartz;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yak.framework.schedule.api.ConcurrencyPolicy;
import io.yak.framework.schedule.api.MisfirePolicy;
import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleEngineBinding;
import io.yak.framework.schedule.api.ScheduleEngineBindingRepository;
import io.yak.framework.schedule.api.ScheduleEngineCapabilities;
import io.yak.framework.schedule.api.ScheduleEngineTypes;
import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.ScheduleNotFoundException;
import io.yak.framework.schedule.api.ScheduleProviderException;
import io.yak.framework.schedule.api.ScheduleSnapshot;
import io.yak.framework.schedule.api.ScheduleStatus;
import io.yak.framework.schedule.api.ScheduleTriggerResult;
import io.yak.framework.schedule.api.TriggerType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

/** Quartz 调度引擎插件。 */
public final class QuartzScheduleEngine implements ScheduleEngine {
    private final Scheduler scheduler;
    private final ObjectMapper objectMapper;
    private final ScheduleEngineBindingRepository bindingRepository;
    private final ScheduleEngineCapabilities capabilities =
            new ScheduleEngineCapabilities(
                    EnumSet.of(TriggerType.CRON, TriggerType.ONE_TIME),
                    EnumSet.allOf(ConcurrencyPolicy.class),
                    EnumSet.allOf(MisfirePolicy.class),
                    true,
                    true,
                    true,
                    true);

    public QuartzScheduleEngine(
            Scheduler scheduler,
            ObjectMapper objectMapper,
            ScheduleEngineBindingRepository bindingRepository) {
        this.scheduler = scheduler;
        this.objectMapper = objectMapper;
        this.bindingRepository = bindingRepository;
    }

    @Override
    public String type() {
        return ScheduleEngineTypes.QUARTZ;
    }

    @Override
    public ScheduleEngineCapabilities capabilities() {
        return capabilities;
    }

    @Override
    public ScheduleSnapshot save(ScheduleDefinition definition) {
        try {
            JobKey jobKey = jobKey(definition.key());
            TriggerKey triggerKey = triggerKey(definition.key());
            JobDetail job = buildJob(jobKey, definition);
            Trigger trigger = buildTrigger(triggerKey, jobKey, definition);

            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }
            scheduler.scheduleJob(job, trigger);
            if (!definition.enabled()) {
                scheduler.pauseJob(jobKey);
            }

            bindingRepository.save(new ScheduleEngineBinding(
                    definition.key(),
                    type(),
                    externalId(definition.key()),
                    Map.of()));
            return snapshot(jobKey);
        } catch (SchedulerException exception) {
            throw providerFailure("save", definition.key(), exception);
        }
    }

    @Override
    public void pause(ScheduleKey key) {
        execute("pause", key, () -> scheduler.pauseJob(required(key)));
    }

    @Override
    public void resume(ScheduleKey key) {
        execute("resume", key, () -> scheduler.resumeJob(required(key)));
    }

    @Override
    public void delete(ScheduleKey key) {
        execute("delete", key, () -> {
            scheduler.deleteJob(required(key));
            bindingRepository.delete(key, type());
        });
    }

    @Override
    public ScheduleTriggerResult runNow(ScheduleKey key) {
        String triggerId = UUID.randomUUID().toString();
        execute("runNow", key, () -> {
            JobDataMap data = new JobDataMap();
            data.put(QuartzScheduleConstants.MANUAL_TRIGGER_ID, triggerId);
            scheduler.triggerJob(required(key), data);
        });
        return new ScheduleTriggerResult(
                triggerId,
                externalId(key),
                Instant.now());
    }

    @Override
    public Optional<ScheduleSnapshot> get(ScheduleKey key) {
        try {
            JobKey jobKey = jobKey(key);
            if (!scheduler.checkExists(jobKey)) {
                return Optional.empty();
            }
            return Optional.of(snapshot(jobKey));
        } catch (SchedulerException exception) {
            throw providerFailure("get", key, exception);
        }
    }

    @Override
    public List<ScheduleSnapshot> list(String namespace) {
        try {
            List<ScheduleSnapshot> result = new ArrayList<>();
            for (JobKey key : scheduler.getJobKeys(
                    GroupMatcher.jobGroupEquals(namespace))) {
                result.add(snapshot(key));
            }
            result.sort(Comparator.comparing(
                    item -> item.definition().key().name()));
            return List.copyOf(result);
        } catch (SchedulerException exception) {
            throw new ScheduleProviderException(
                    "Quartz list failed for namespace: " + namespace,
                    exception);
        }
    }

    private JobDetail buildJob(
            JobKey jobKey,
            ScheduleDefinition definition) {
        try {
            Class<? extends org.quartz.Job> jobClass =
                    definition.policy().concurrencyPolicy()
                            == ConcurrencyPolicy.FORBID
                            ? NonConcurrentQuartzScheduleJob.class
                            : QuartzScheduleJob.class;
            JobDataMap data = new JobDataMap();
            data.put(
                    QuartzScheduleConstants.DEFINITION_JSON,
                    objectMapper.writeValueAsString(definition));
            return JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .usingJobData(data)
                    .build();
        } catch (Exception exception) {
            throw new ScheduleProviderException(
                    "Serialize Quartz schedule failed: "
                            + definition.key().value(),
                    exception);
        }
    }

    private Trigger buildTrigger(
            TriggerKey triggerKey,
            JobKey jobKey,
            ScheduleDefinition definition) {
        if (definition.trigger().type() == TriggerType.CRON) {
            CronScheduleBuilder builder = CronScheduleBuilder
                    .cronSchedule(definition.trigger().expression())
                    .inTimeZone(TimeZone.getTimeZone(
                            definition.trigger().zoneId()));
            builder = definition.policy().misfirePolicy()
                    == MisfirePolicy.IGNORE
                    ? builder.withMisfireHandlingInstructionDoNothing()
                    : builder.withMisfireHandlingInstructionFireAndProceed();
            return TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(jobKey)
                    .withSchedule(builder)
                    .build();
        }

        SimpleScheduleBuilder builder = SimpleScheduleBuilder
                .simpleSchedule()
                .withRepeatCount(0);
        builder = definition.policy().misfirePolicy()
                == MisfirePolicy.IGNORE
                ? builder.withMisfireHandlingInstructionNextWithExistingCount()
                : builder.withMisfireHandlingInstructionFireNow();
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .forJob(jobKey)
                .startAt(Date.from(definition.trigger().executeAt()))
                .withSchedule(builder)
                .build();
    }

    private ScheduleSnapshot snapshot(JobKey jobKey)
            throws SchedulerException {
        JobDetail job = scheduler.getJobDetail(jobKey);
        if (job == null) {
            throw new ScheduleNotFoundException(
                    new ScheduleKey(jobKey.getGroup(), jobKey.getName()));
        }
        Trigger trigger = scheduler.getTrigger(
                TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
        ScheduleDefinition definition;
        try {
            definition = objectMapper.readValue(
                    job.getJobDataMap().getString(
                            QuartzScheduleConstants.DEFINITION_JSON),
                    ScheduleDefinition.class);
        } catch (Exception exception) {
            throw new ScheduleProviderException(
                    "Deserialize Quartz schedule failed: " + jobKey,
                    exception);
        }
        return new ScheduleSnapshot(
                definition,
                type(),
                externalId(definition.key()),
                status(trigger),
                instant(trigger == null ? null : trigger.getNextFireTime()),
                instant(trigger == null ? null : trigger.getPreviousFireTime()));
    }

    private ScheduleStatus status(Trigger trigger)
            throws SchedulerException {
        if (trigger == null) {
            return ScheduleStatus.COMPLETED;
        }
        return switch (scheduler.getTriggerState(trigger.getKey())) {
            case PAUSED -> ScheduleStatus.PAUSED;
            case COMPLETE, NONE -> ScheduleStatus.COMPLETED;
            case NORMAL, BLOCKED -> ScheduleStatus.ENABLED;
            default -> ScheduleStatus.UNKNOWN;
        };
    }

    private JobKey required(ScheduleKey key)
            throws SchedulerException {
        JobKey jobKey = jobKey(key);
        if (!scheduler.checkExists(jobKey)) {
            throw new ScheduleNotFoundException(key);
        }
        return jobKey;
    }

    private void execute(
            String operation,
            ScheduleKey key,
            QuartzAction action) {
        try {
            action.run();
        } catch (ScheduleNotFoundException exception) {
            throw exception;
        } catch (SchedulerException exception) {
            throw providerFailure(operation, key, exception);
        }
    }

    private ScheduleProviderException providerFailure(
            String operation,
            ScheduleKey key,
            Exception exception) {
        return new ScheduleProviderException(
                "Quartz " + operation + " failed: " + key.value(),
                exception);
    }

    private static JobKey jobKey(ScheduleKey key) {
        return JobKey.jobKey(key.name(), key.namespace());
    }

    private static TriggerKey triggerKey(ScheduleKey key) {
        return TriggerKey.triggerKey(key.name(), key.namespace());
    }

    private static String externalId(ScheduleKey key) {
        return key.namespace() + "/" + key.name();
    }

    private static Instant instant(Date value) {
        return value == null ? null : value.toInstant();
    }

    @FunctionalInterface
    private interface QuartzAction {
        void run() throws SchedulerException;
    }
}
