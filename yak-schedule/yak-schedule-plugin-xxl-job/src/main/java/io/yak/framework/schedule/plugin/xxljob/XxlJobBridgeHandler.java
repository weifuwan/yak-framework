package io.yak.framework.schedule.plugin.xxljob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.yak.framework.schedule.api.ScheduleEngineTypes;
import io.yak.framework.schedule.api.ScheduleExecutionContext;
import io.yak.framework.schedule.api.ScheduleExecutionResult;
import io.yak.framework.schedule.core.ScheduleExecutionDispatcher;
import java.time.Instant;
import java.util.UUID;

/** XXL-JOB Executor 中唯一的通用任务处理器。 */
public final class XxlJobBridgeHandler {
    private final ObjectMapper objectMapper;
    private final ScheduleExecutionDispatcher dispatcher;

    public XxlJobBridgeHandler(
            ObjectMapper objectMapper,
            ScheduleExecutionDispatcher dispatcher) {
        this.objectMapper = objectMapper;
        this.dispatcher = dispatcher;
    }

    @XxlJob(XxlJobScheduleEngine.HANDLER_NAME)
    public void execute() throws Exception {
        try {
            XxlJobDispatchPayload payload = objectMapper.readValue(
                    XxlJobHelper.getJobParam(),
                    XxlJobDispatchPayload.class);
            boolean manual = payload.triggerId() != null
                    && !payload.triggerId().isBlank();
            String triggerId = manual
                    ? payload.triggerId()
                    : UUID.randomUUID().toString();
            long logDateTime = XxlJobHelper.getLogDateTime();
            Instant scheduled = logDateTime > 0
                    ? Instant.ofEpochMilli(logDateTime)
                    : Instant.now();

            ScheduleExecutionResult result = dispatcher.dispatch(
                    new ScheduleExecutionContext(
                            triggerId,
                            payload.key(),
                            ScheduleEngineTypes.XXL_JOB,
                            payload.target().handler(),
                            payload.target().payload(),
                            scheduled,
                            Instant.now(),
                            manual,
                            1));
            String message = result.businessExecutionId() == null
                    ? result.message()
                    : "businessExecutionId=" + result.businessExecutionId();
            XxlJobHelper.handleSuccess(message);
        } catch (Exception exception) {
            XxlJobHelper.handleFail(safeMessage(exception));
            throw exception;
        }
    }

    private static String safeMessage(Throwable exception) {
        String value = exception.getMessage();
        if (value == null || value.isBlank()) {
            value = exception.getClass().getSimpleName();
        }
        return value.length() <= 2_000
                ? value
                : value.substring(0, 2_000);
    }
}
