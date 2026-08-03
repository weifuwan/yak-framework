package io.yak.framework.schedule.provider.quartz;

import io.yak.framework.schedule.ScheduleExecutionLogRepository;
import io.yak.framework.schedule.api.ScheduleExecutionContext;
import io.yak.framework.schedule.api.ScheduleExecutionResult;
import io.yak.framework.schedule.api.model.ScheduleKey;
import io.yak.framework.schedule.core.ScheduleExecutionDispatcher;
import io.yak.framework.schedule.model.ExecutionStatus;
import io.yak.framework.schedule.model.ScheduleExecutionLog;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/** Quartz execution bridge for provider-neutral schedule handlers. */
public class QuartzScheduleJob implements Job {

    private static final int MAX_MESSAGE_LENGTH = 2000;
    private static final String SYSTEM_OPERATOR = "SYSTEM";

    @Autowired
    private ScheduleExecutionDispatcher dispatcher;

    @Autowired
    private ScheduleExecutionLogRepository logRepository;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        JobDataMap data = context.getMergedJobDataMap();
        String triggerId = text(
                data.getString(QuartzScheduleData.TRIGGER_ID),
                context.getFireInstanceId(),
                UUID.randomUUID().toString());
        ScheduleKey key = new ScheduleKey(
                required(data, QuartzScheduleData.NAMESPACE),
                required(data, QuartzScheduleData.NAME));
        String operator = text(
                data.getString(QuartzScheduleData.OPERATOR),
                SYSTEM_OPERATOR);
        int attempt = context.getRefireCount() + 1;
        int maxRetries = Math.max(
                0,
                integer(data, QuartzScheduleData.MAX_TRIGGER_RETRIES));
        Instant startedAt = Instant.now();

        try {
            ScheduleExecutionResult result = dispatcher.dispatch(
                    new ScheduleExecutionContext(
                            triggerId,
                            key,
                            required(data, QuartzScheduleData.HANDLER),
                            instant(context.getScheduledFireTime()),
                            instant(context.getFireTime()),
                            booleanValue(
                                    data,
                                    QuartzScheduleData.MANUAL),
                            attempt,
                            QuartzScheduleData.values(
                                    data,
                                    QuartzScheduleData.PAYLOAD_PREFIX),
                            attributes(data, operator)));

            saveLogSafely(
                    new ScheduleExecutionLog(
                            triggerId,
                            key.getNamespace(),
                            key.getName(),
                            operator,
                            startedAt,
                            Instant.now(),
                            ExecutionStatus.SUCCEEDED,
                            attempt,
                            resultMessage(result)));
        } catch (Exception exception) {
            saveLogSafely(
                    new ScheduleExecutionLog(
                            triggerId,
                            key.getNamespace(),
                            key.getName(),
                            operator,
                            startedAt,
                            Instant.now(),
                            ExecutionStatus.FAILED,
                            attempt,
                            safeMessage(exception)));

            JobExecutionException failure =
                    new JobExecutionException(exception);
            failure.setRefireImmediately(
                    !(exception instanceof InterruptedException)
                            && context.getRefireCount() < maxRetries);

            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw failure;
        }
    }

    private Map<String, String> attributes(
            JobDataMap data,
            String operator) {

        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("engineType", QuartzScheduleEngine.ENGINE_TYPE);
        attributes.put("operator", operator);
        attributes.putAll(
                QuartzScheduleData.values(
                        data,
                        QuartzScheduleData.METADATA_PREFIX));
        return Collections.unmodifiableMap(attributes);
    }

    private void saveLogSafely(ScheduleExecutionLog log) {
        try {
            logRepository.save(log);
        } catch (Exception ignored) {
            // A logging failure must not cause a successful business trigger
            // to be repeated.
        }
    }

    private static String resultMessage(
            ScheduleExecutionResult result) {

        if (result == null) {
            return null;
        }
        if (!result.isAccepted()) {
            throw new IllegalStateException(
                    text(
                            result.getMessage(),
                            "Schedule handler rejected the trigger"));
        }

        String executionId = result.getBusinessExecutionId();
        String message = result.getMessage();

        if (executionId == null || executionId.trim().isEmpty()) {
            return message;
        }
        if (message == null || message.trim().isEmpty()) {
            return "businessExecutionId=" + executionId;
        }
        return "businessExecutionId="
                + executionId
                + "; "
                + message;
    }

    private static String safeMessage(Exception exception) {
        Throwable root = exception;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }

        String message = root.getMessage();
        String value = message == null || message.trim().isEmpty()
                ? root.getClass().getSimpleName()
                : root.getClass().getSimpleName() + ": " + message;

        return value.length() <= MAX_MESSAGE_LENGTH
                ? value
                : value.substring(0, MAX_MESSAGE_LENGTH);
    }

    private static String required(
            JobDataMap data,
            String key) {

        String value = data.getString(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Missing required Quartz schedule data: " + key);
        }
        return value.trim();
    }

    private static int integer(JobDataMap data, String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return value == null
                ? 0
                : Integer.parseInt(String.valueOf(value));
    }

    private static boolean booleanValue(
            JobDataMap data,
            String key) {

        Object value = data.get(key);
        return value != null
                && Boolean.parseBoolean(String.valueOf(value));
    }

    private static Instant instant(java.util.Date value) {
        return value == null ? null : value.toInstant();
    }

    private static String text(
            String value,
            String... fallbacks) {

        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        if (fallbacks != null) {
            for (String fallback : fallbacks) {
                if (fallback != null
                        && !fallback.trim().isEmpty()) {
                    return fallback.trim();
                }
            }
        }
        return null;
    }
}
