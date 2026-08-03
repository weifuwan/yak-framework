package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ExecutionStatus;
import io.yak.framework.schedule.api.ScheduleExecutionContext;
import io.yak.framework.schedule.api.ScheduleExecutionLog;
import io.yak.framework.schedule.api.ScheduleExecutionLogRepository;
import io.yak.framework.schedule.api.ScheduleExecutionResult;
import io.yak.framework.schedule.api.ScheduleHandler;
import io.yak.framework.schedule.api.ScheduleProviderException;
import java.time.Instant;
import org.springframework.context.ApplicationContext;

/** 将任意调度引擎的触发请求分发给业务 ScheduleHandler。 */
public final class ScheduleExecutionDispatcher {
    private static final int MAX_MESSAGE_LENGTH = 2_000;

    private final ApplicationContext applicationContext;
    private final ScheduleExecutionLogRepository logRepository;

    public ScheduleExecutionDispatcher(
            ApplicationContext applicationContext,
            ScheduleExecutionLogRepository logRepository) {
        this.applicationContext = applicationContext;
        this.logRepository = logRepository;
    }

    public ScheduleExecutionResult dispatch(ScheduleExecutionContext context) {
        Instant startedAt = Instant.now();
        try {
            ScheduleHandler handler = applicationContext.getBean(
                    context.handler(), ScheduleHandler.class);
            ScheduleExecutionResult result = handler.execute(context);
            ScheduleExecutionResult normalized = result == null
                    ? ScheduleExecutionResult.accepted(null)
                    : result;
            logRepository.save(new ScheduleExecutionLog(
                    context.triggerId(),
                    context.key(),
                    context.engineType(),
                    context.handler(),
                    startedAt,
                    Instant.now(),
                    ExecutionStatus.SUCCEEDED,
                    context.attempt(),
                    normalized.businessExecutionId(),
                    normalized.message()));
            return normalized;
        } catch (Exception exception) {
            logRepository.save(new ScheduleExecutionLog(
                    context.triggerId(),
                    context.key(),
                    context.engineType(),
                    context.handler(),
                    startedAt,
                    Instant.now(),
                    ExecutionStatus.FAILED,
                    context.attempt(),
                    null,
                    safeMessage(exception)));
            throw new ScheduleProviderException(
                    "Schedule handler failed: " + context.handler(), exception);
        }
    }

    private static String safeMessage(Throwable exception) {
        Throwable current = exception;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        String message = current.getMessage();
        String value = message == null || message.isBlank()
                ? current.getClass().getSimpleName()
                : current.getClass().getSimpleName() + ": " + message;
        return value.length() <= MAX_MESSAGE_LENGTH
                ? value
                : value.substring(0, MAX_MESSAGE_LENGTH);
    }
}
