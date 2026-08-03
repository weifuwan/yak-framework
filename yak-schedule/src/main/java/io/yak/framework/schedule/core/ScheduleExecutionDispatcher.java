package io.yak.framework.schedule.core;

import io.yak.framework.schedule.ScheduleTaskHandler;
import io.yak.framework.schedule.api.ScheduleExecutionContext;
import io.yak.framework.schedule.api.ScheduleExecutionResult;
import io.yak.framework.schedule.api.ScheduleHandler;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;

/** Resolves new handlers while retaining the legacy handler contract. */
public final class ScheduleExecutionDispatcher {

    private final ApplicationContext applicationContext;

    public ScheduleExecutionDispatcher(
            ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    public ScheduleExecutionResult dispatch(
            ScheduleExecutionContext context)
            throws Exception {

        String handlerName = context.getHandler();

        if (applicationContext.containsBean(handlerName)) {
            Object handler = applicationContext.getBean(handlerName);

            if (handler instanceof ScheduleHandler) {
                ScheduleExecutionResult result =
                        ((ScheduleHandler) handler).execute(context);
                return result == null
                        ? ScheduleExecutionResult.accepted(null)
                        : result;
            }

            if (handler instanceof ScheduleTaskHandler) {
                ((ScheduleTaskHandler) handler).execute(
                        legacyParameters(context.getPayload()));
                return ScheduleExecutionResult.accepted(null);
            }
        }

        throw new IllegalStateException(
                "Unknown or unsupported schedule handler: "
                        + handlerName);
    }

    private Map<String, String> legacyParameters(
            Map<String, String> payload) {

        return java.util.Collections.unmodifiableMap(
                new LinkedHashMap<>(payload));
    }
}
