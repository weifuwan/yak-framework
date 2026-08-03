package io.yak.framework.schedule.provider.quartz;

import io.yak.framework.schedule.NonConcurrentQuartzTaskJob;
import io.yak.framework.schedule.QuartzTaskJob;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleNotFoundException;
import io.yak.framework.schedule.api.ScheduleProviderException;
import io.yak.framework.schedule.api.model.MisfirePolicy;
import io.yak.framework.schedule.api.model.ScheduleDefinition;
import io.yak.framework.schedule.api.model.ScheduleEngineCapabilities;
import io.yak.framework.schedule.api.model.ScheduleKey;
import io.yak.framework.schedule.api.model.SchedulePolicy;
import io.yak.framework.schedule.api.model.ScheduleSnapshot;
import io.yak.framework.schedule.api.model.ScheduleStatus;
import io.yak.framework.schedule.api.model.ScheduleTarget;
import io.yak.framework.schedule.api.model.ScheduleTrigger;
import io.yak.framework.schedule.api.model.ScheduleTriggerResult;
import io.yak.framework.schedule.api.model.TriggerType;
import io.yak.framework.schedule.model.ConcurrencyPolicy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
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
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

/** Quartz provider implementation of the stable ScheduleEngine SPI. */
public final class QuartzScheduleEngine implements ScheduleEngine {

    public static final String ENGINE_TYPE = "quartz";

    private final Scheduler scheduler;
    private final ScheduleEngineCapabilities capabilities =
            new ScheduleEngineCapabilities(
                    EnumSet.of(
                            TriggerType.CRON,
                            TriggerType.ONE_TIME),
                    true,
                    true,
                    true,
                    true);

    public QuartzScheduleEngine(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public String engineType() {
        return ENGINE_TYPE;
    }

    @Override
    public ScheduleEngineCapabilities capabilities() {
        return capabilities;
    }

    @Override
    public ScheduleSnapshot save(ScheduleDefinition definition) {
        try {
            JobKey jobKey = jobKey(definition.getKey());
            JobDetail job = buildJob(jobKey, definition);
            Trigger trigger = buildTrigger(jobKey, definition);

            if (scheduler.checkExists(jobKey)) {
                scheduler.scheduleJob(
                        job,
                        Collections.singleton(trigger),
                        true);
            } else {
                scheduler.scheduleJob(job, trigger);
            }

            if (!definition.isEnabled()) {
                scheduler.pauseJob(jobKey);
            }

            return snapshot(
                    job,
                    trigger,
                    scheduler.getTriggerState(trigger.getKey()));
        } catch (SchedulerException exception) {
            throw providerFailure(
                    "save",
                    definition.getKey(),
                    exception);
        }
    }

    @Override
    public void pause(ScheduleKey key) {
        try {
            scheduler.pauseJob(required(key));
        } catch (SchedulerException exception) {
            throw providerFailure("pause", key, exception);
        }
    }

    @Override
    public void resume(ScheduleKey key) {
        try {
            scheduler.resumeJob(required(key));
        } catch (SchedulerException exception) {
            throw providerFailure("resume", key, exception);
        }
    }

    @Override
    public void delete(ScheduleKey key) {
        try {
            scheduler.deleteJob(required(key));
        } catch (SchedulerException exception) {
            throw providerFailure("delete", key, exception);
        }
    }

    @Override
    public ScheduleTriggerResult runNow(
            ScheduleKey key,
            String operator) {

        String triggerId = UUID.randomUUID().toString();
        JobDataMap data = new JobDataMap();
        data.put(QuartzScheduleData.TRIGGER_ID, triggerId);
        data.put(QuartzScheduleData.MANUAL, "true");
        data.put(
                QuartzScheduleData.OPERATOR,
                text(operator, "UNKNOWN"));

        try {
            scheduler.triggerJob(required(key), data);
            return new ScheduleTriggerResult(
                    triggerId,
                    Instant.now());
        } catch (SchedulerException exception) {
            throw providerFailure("run now", key, exception);
        }
    }

    @Override
    public Optional<ScheduleSnapshot> get(ScheduleKey key) {
        try {
            JobKey jobKey = jobKey(key);
            if (!scheduler.checkExists(jobKey)) {
                return Optional.empty();
            }

            JobDetail job = scheduler.getJobDetail(jobKey);
            Trigger trigger = trigger(jobKey);
            return Optional.of(
                    snapshot(
                            job,
                            trigger,
                            scheduler.getTriggerState(
                                    trigger.getKey())));
        } catch (SchedulerException exception) {
            throw providerFailure("get", key, exception);
        }
    }

    @Override
    public List<ScheduleSnapshot> list(String namespace) {
        if (namespace == null || namespace.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "namespace must not be blank");
        }

        try {
            List<ScheduleSnapshot> snapshots =
                    new ArrayList<>();

            for (JobKey jobKey : scheduler.getJobKeys(
                    GroupMatcher.jobGroupEquals(
                            namespace.trim()))) {

                JobDetail job = scheduler.getJobDetail(jobKey);
                Trigger trigger = trigger(jobKey);
                snapshots.add(
                        snapshot(
                                job,
                                trigger,
                                scheduler.getTriggerState(
                                        trigger.getKey())));
            }

            snapshots.sort(
                    Comparator.comparing(
                            value -> value
                                    .getDefinition()
                                    .getKey()
                                    .getName()));

            return Collections.unmodifiableList(snapshots);
        } catch (SchedulerException exception) {
            throw new ScheduleProviderException(
                    "Quartz failed to list namespace "
                            + namespace,
                    exception);
        }
    }

    private JobDetail buildJob(
            JobKey jobKey,
            ScheduleDefinition definition) {

        Class<? extends org.quartz.Job> jobClass =
                definition.getPolicy().getConcurrencyPolicy()
                        == ConcurrencyPolicy.FORBID
                        ? NonConcurrentQuartzTaskJob.class
                        : QuartzTaskJob.class;

        return JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .usingJobData(jobData(definition))
                .build();
    }

    private JobDataMap jobData(ScheduleDefinition definition) {
        JobDataMap data = new JobDataMap();
        data.put(
                QuartzScheduleData.NAMESPACE,
                definition.getKey().getNamespace());
        data.put(
                QuartzScheduleData.NAME,
                definition.getKey().getName());
        data.put(
                QuartzScheduleData.DISPLAY_NAME,
                definition.getDisplayName());
        data.put(
                QuartzScheduleData.DESCRIPTION,
                text(definition.getDescription(), ""));
        data.put(
                QuartzScheduleData.HANDLER,
                definition.getTarget().getHandler());
        data.put(
                QuartzScheduleData.CONCURRENCY,
                definition.getPolicy()
                        .getConcurrencyPolicy()
                        .name());
        data.put(
                QuartzScheduleData.MISFIRE,
                definition.getPolicy()
                        .getMisfirePolicy()
                        .name());
        data.put(
                QuartzScheduleData.MAX_TRIGGER_RETRIES,
                Integer.toString(
                        definition.getPolicy()
                                .getMaxTriggerRetries()));
        data.put(
                QuartzScheduleData.OPERATOR,
                "SYSTEM");
        data.put(
                QuartzScheduleData.MANUAL,
                "false");

        if (definition.getVersion() != null) {
            data.put(
                    QuartzScheduleData.VERSION,
                    definition.getVersion().toString());
        }

        putValues(
                data,
                QuartzScheduleData.PAYLOAD_PREFIX,
                definition.getTarget().getPayload());
        putValues(
                data,
                QuartzScheduleData.METADATA_PREFIX,
                definition.getMetadata());

        return data;
    }

    private Trigger buildTrigger(
            JobKey jobKey,
            ScheduleDefinition definition) {

        ScheduleTrigger trigger = definition.getTrigger();
        TriggerBuilder<Trigger> builder =
                TriggerBuilder.newTrigger()
                        .withIdentity(
                                triggerKey(definition.getKey()))
                        .forJob(jobKey);

        if (trigger.getType() == TriggerType.CRON) {
            CronScheduleBuilder schedule =
                    CronScheduleBuilder
                            .cronSchedule(trigger.getCron())
                            .inTimeZone(
                                    TimeZone.getTimeZone(
                                            trigger.getZoneId()));

            schedule = applyMisfire(
                    schedule,
                    definition.getPolicy().getMisfirePolicy());

            return builder
                    .withSchedule(schedule)
                    .build();
        }

        SimpleScheduleBuilder schedule =
                SimpleScheduleBuilder
                        .simpleSchedule()
                        .withRepeatCount(0);

        schedule = applyMisfire(
                schedule,
                definition.getPolicy().getMisfirePolicy());

        return builder
                .startAt(Date.from(trigger.getExecuteAt()))
                .withSchedule(schedule)
                .build();
    }

    private CronScheduleBuilder applyMisfire(
            CronScheduleBuilder builder,
            MisfirePolicy policy) {

        if (policy == MisfirePolicy.IGNORE) {
            return builder
                    .withMisfireHandlingInstructionDoNothing();
        }
        if (policy == MisfirePolicy.FIRE_ONCE_NOW) {
            return builder
                    .withMisfireHandlingInstructionFireAndProceed();
        }
        return builder;
    }

    private SimpleScheduleBuilder applyMisfire(
            SimpleScheduleBuilder builder,
            MisfirePolicy policy) {

        if (policy == MisfirePolicy.IGNORE) {
            return builder
                    .withMisfireHandlingInstructionNextWithExistingCount();
        }
        if (policy == MisfirePolicy.FIRE_ONCE_NOW) {
            return builder
                    .withMisfireHandlingInstructionFireNow();
        }
        return builder;
    }

    private ScheduleSnapshot snapshot(
            JobDetail job,
            Trigger trigger,
            Trigger.TriggerState triggerState) {

        JobDataMap data = job.getJobDataMap();
        ScheduleKey key = new ScheduleKey(
                data.getString(QuartzScheduleData.NAMESPACE),
                data.getString(QuartzScheduleData.NAME));

        ScheduleTrigger scheduleTrigger;
        if (trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;
            scheduleTrigger = ScheduleTrigger.cron(
                    cronTrigger.getCronExpression(),
                    cronTrigger.getTimeZone().toZoneId());
        } else if (trigger instanceof SimpleTrigger) {
            scheduleTrigger = ScheduleTrigger.oneTime(
                    trigger.getStartTime().toInstant());
        } else {
            throw new IllegalStateException(
                    "Unsupported Quartz trigger: "
                            + trigger.getClass().getName());
        }

        SchedulePolicy policy = new SchedulePolicy(
                enumValue(
                        ConcurrencyPolicy.class,
                        data.getString(
                                QuartzScheduleData.CONCURRENCY),
                        ConcurrencyPolicy.FORBID),
                enumValue(
                        MisfirePolicy.class,
                        data.getString(
                                QuartzScheduleData.MISFIRE),
                        MisfirePolicy.SMART),
                integer(
                        data,
                        QuartzScheduleData.MAX_TRIGGER_RETRIES));

        ScheduleDefinition definition =
                new ScheduleDefinition(
                        key,
                        data.getString(
                                QuartzScheduleData.DISPLAY_NAME),
                        emptyToNull(
                                data.getString(
                                        QuartzScheduleData.DESCRIPTION)),
                        ENGINE_TYPE,
                        scheduleTrigger,
                        new ScheduleTarget(
                                data.getString(
                                        QuartzScheduleData.HANDLER),
                                QuartzScheduleData.values(
                                        data,
                                        QuartzScheduleData.PAYLOAD_PREFIX)),
                        policy,
                        triggerState
                                != Trigger.TriggerState.PAUSED,
                        longValue(
                                data.get(
                                        QuartzScheduleData.VERSION)),
                        QuartzScheduleData.values(
                                data,
                                QuartzScheduleData.METADATA_PREFIX));

        return new ScheduleSnapshot(
                definition,
                status(triggerState),
                instant(trigger.getPreviousFireTime()),
                instant(trigger.getNextFireTime()),
                job.getKey().toString());
    }

    private Trigger trigger(JobKey jobKey)
            throws SchedulerException {

        List<? extends Trigger> triggers =
                scheduler.getTriggersOfJob(jobKey);

        if (triggers == null || triggers.isEmpty()) {
            throw new IllegalStateException(
                    "Schedule has no trigger: " + jobKey);
        }

        return triggers.get(0);
    }

    private JobKey required(ScheduleKey key)
            throws SchedulerException {

        JobKey jobKey = jobKey(key);
        if (!scheduler.checkExists(jobKey)) {
            throw new ScheduleNotFoundException(key);
        }
        return jobKey;
    }

    private static JobKey jobKey(ScheduleKey key) {
        return JobKey.jobKey(
                key.getName(),
                key.getNamespace());
    }

    private static TriggerKey triggerKey(ScheduleKey key) {
        return TriggerKey.triggerKey(
                key.getName(),
                key.getNamespace());
    }

    private static ScheduleStatus status(
            Trigger.TriggerState state) {

        if (state == Trigger.TriggerState.NORMAL
                || state == Trigger.TriggerState.BLOCKED) {
            return ScheduleStatus.ACTIVE;
        }
        if (state == Trigger.TriggerState.PAUSED) {
            return ScheduleStatus.PAUSED;
        }
        if (state == Trigger.TriggerState.COMPLETE
                || state == Trigger.TriggerState.NONE) {
            return ScheduleStatus.COMPLETE;
        }
        if (state == Trigger.TriggerState.ERROR) {
            return ScheduleStatus.ERROR;
        }
        return ScheduleStatus.UNKNOWN;
    }

    private static void putValues(
            JobDataMap target,
            String prefix,
            Map<String, String> values) {

        for (Map.Entry<String, String> entry : values.entrySet()) {
            target.put(
                    prefix + entry.getKey(),
                    entry.getValue());
        }
    }

    private static int integer(
            JobDataMap data,
            String key) {

        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return value == null
                ? 0
                : Integer.parseInt(String.valueOf(value));
    }

    private static Long longValue(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value);
        return text.trim().isEmpty()
                ? null
                : Long.valueOf(text);
    }

    private static Instant instant(Date value) {
        return value == null ? null : value.toInstant();
    }

    private static String emptyToNull(String value) {
        return value == null || value.trim().isEmpty()
                ? null
                : value;
    }

    private static String text(
            String value,
            String fallback) {

        return value == null || value.trim().isEmpty()
                ? fallback
                : value.trim();
    }

    private static <T extends Enum<T>> T enumValue(
            Class<T> type,
            String value,
            T fallback) {

        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return Enum.valueOf(
                type,
                value.trim().toUpperCase(java.util.Locale.ROOT));
    }

    private static ScheduleProviderException providerFailure(
            String operation,
            ScheduleKey key,
            SchedulerException exception) {

        return new ScheduleProviderException(
                "Quartz failed to "
                        + operation
                        + " schedule "
                        + key.value(),
                exception);
    }
}
