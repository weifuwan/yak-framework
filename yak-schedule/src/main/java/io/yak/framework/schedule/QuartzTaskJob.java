package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ExecutionStatus;
import io.yak.framework.schedule.model.ScheduleExecutionLog;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Quartz 调度任务执行桥接器。
 *
 * <p>负责从 Spring 容器中获取业务任务处理器，解析任务参数，
 * 执行业务逻辑，并记录执行日志以及处理失败重试。</p>
 *
 * <p>该类由 Quartz 创建实例，因此保留 Spring 字段注入方式。
 * 项目需要配置支持自动注入的 Quartz JobFactory。</p>
 *
 * @author weifuwan
 */
@Slf4j
public class QuartzTaskJob implements Job {

    /**
     * 默认系统操作人。
     */
    private static final String SYSTEM_OPERATOR = "SYSTEM";

    /**
     * 任务参数前缀。
     */
    private static final String PARAMETER_PREFIX = "parameter.";

    /**
     * 日志错误消息最大长度。
     */
    private static final int MAX_MESSAGE_LENGTH = 2000;

    /**
     * Quartz JobDataMap 数据键。
     */
    private static final String DATA_PROJECT = "project";
    private static final String DATA_TASK = "task";
    private static final String DATA_HANDLER = "handler";
    private static final String DATA_OPERATOR = "operator";
    private static final String DATA_MAX_RETRIES = "maxRetries";

    /**
     * Spring 应用上下文。
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 调度任务执行日志仓库。
     */
    @Autowired
    private ScheduleExecutionLogRepository logRepository;

    /**
     * 获取安全的异常信息。
     *
     * <p>优先提取最底层异常信息，并限制最大长度，
     * 防止超长异常内容影响数据库字段写入。</p>
     *
     * @param exception 执行异常
     * @return 安全的异常信息
     */
    private static String safeMessage(
            Exception exception) {

        Throwable rootCause = findRootCause(exception);
        String message = rootCause.getMessage();

        String value = hasText(message)
                ? rootCause.getClass().getSimpleName()
                + ": "
                + message
                : rootCause.getClass().getSimpleName();

        return value.length() <= MAX_MESSAGE_LENGTH
                ? value
                : value.substring(0, MAX_MESSAGE_LENGTH);
    }

    /**
     * 查找最底层异常。
     *
     * @param throwable 原始异常
     * @return 最底层异常
     */
    private static Throwable findRootCause(
            Throwable throwable) {

        Throwable current = throwable;

        while (current.getCause() != null
                && current.getCause() != current) {
            current = current.getCause();
        }

        return current;
    }

    /**
     * 判断字符串是否包含有效文本。
     *
     * @param value 字符串
     * @return 是否包含有效文本
     */
    private static boolean hasText(
            String value) {

        return value != null
                && !value.trim().isEmpty();
    }

    /**
     * 执行 Quartz 调度任务。
     *
     * @param context Quartz 任务执行上下文
     * @throws JobExecutionException 任务执行异常
     */
    @Override
    public void execute(
            JobExecutionContext context)
            throws JobExecutionException {

        JobDataMap data = context.getMergedJobDataMap();

        String executionId = UUID.randomUUID().toString();
        String project = data.getString(DATA_PROJECT);
        String taskName = data.getString(DATA_TASK);
        String operator = resolveOperator(data);
        int attempt = context.getRefireCount() + 1;
        int maxRetries = Math.max(
                0,
                data.getInt(DATA_MAX_RETRIES));
        Instant startedAt = Instant.now();

        try {
            executeHandler(data);

            saveLogSafely(
                    new ScheduleExecutionLog(
                            executionId,
                            project,
                            taskName,
                            operator,
                            startedAt,
                            Instant.now(),
                            ExecutionStatus.SUCCEEDED,
                            attempt,
                            null));

        } catch (Exception exception) {
            handleExecutionFailure(
                    context,
                    executionId,
                    project,
                    taskName,
                    operator,
                    startedAt,
                    attempt,
                    maxRetries,
                    exception);
        }
    }

    /**
     * 查找并执行任务处理器。
     *
     * @param data Quartz 任务数据
     * @throws Exception 任务处理异常
     */
    private void executeHandler(
            JobDataMap data)
            throws Exception {

        String handlerName = requiredText(
                data,
                DATA_HANDLER);

        ScheduleTaskHandler handler =
                applicationContext.getBean(
                        handlerName,
                        ScheduleTaskHandler.class);

        handler.execute(extractParameters(data));
    }

    /**
     * 处理任务执行失败。
     *
     * @param context     Quartz 执行上下文
     * @param executionId 执行记录标识
     * @param project     项目名称
     * @param taskName    任务名称
     * @param operator    操作人
     * @param startedAt   开始时间
     * @param attempt     当前执行次数
     * @param maxRetries  最大重试次数
     * @param exception   执行异常
     * @throws JobExecutionException Quartz 任务异常
     */
    private void handleExecutionFailure(
            JobExecutionContext context,
            String executionId,
            String project,
            String taskName,
            String operator,
            Instant startedAt,
            int attempt,
            int maxRetries,
            Exception exception)
            throws JobExecutionException {

        if (exception instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }

        saveLogSafely(
                new ScheduleExecutionLog(
                        executionId,
                        project,
                        taskName,
                        operator,
                        startedAt,
                        Instant.now(),
                        ExecutionStatus.FAILED,
                        attempt,
                        safeMessage(exception)));

        boolean retryImmediately =
                !(exception instanceof InterruptedException)
                        && context.getRefireCount() < maxRetries;

        log.warn(
                "调度任务执行失败，project={}, task={}, attempt={}, "
                        + "maxRetries={}, retryImmediately={}",
                project,
                taskName,
                attempt,
                maxRetries,
                retryImmediately,
                exception);

        JobExecutionException failure =
                new JobExecutionException(exception);

        failure.setRefireImmediately(retryImmediately);

        throw failure;
    }

    /**
     * 从 Quartz 数据中提取业务任务参数。
     *
     * @param data Quartz 任务数据
     * @return 不可修改的任务参数
     */
    private Map<String, String> extractParameters(
            JobDataMap data) {

        Map<String, String> parameters =
                new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry
                : data.entrySet()) {

            String key = entry.getKey();

            if (key == null
                    || !key.startsWith(PARAMETER_PREFIX)) {
                continue;
            }

            String parameterName =
                    key.substring(PARAMETER_PREFIX.length());

            parameters.put(
                    parameterName,
                    String.valueOf(entry.getValue()));
        }

        if (parameters.isEmpty()) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(parameters);
    }

    /**
     * 安全保存执行日志。
     *
     * <p>日志保存失败时只记录系统日志，不重新抛出异常。
     * 避免业务任务已经执行成功，却因日志持久化失败而触发重试，
     * 从而产生重复执行。</p>
     *
     * @param executionLog 执行日志
     */
    private void saveLogSafely(
            ScheduleExecutionLog executionLog) {

        try {
            logRepository.save(executionLog);
        } catch (Exception exception) {
            log.error(
                    "保存调度任务执行日志失败，executionId={}, "
                            + "project={}, task={}",
                    executionLog.getExecutionId(),
                    executionLog.getProject(),
                    executionLog.getTaskName(),
                    exception);
        }
    }

    /**
     * 获取任务操作人。
     *
     * @param data Quartz 任务数据
     * @return 操作人
     */
    private String resolveOperator(
            JobDataMap data) {

        String operator =
                data.getString(DATA_OPERATOR);

        if (!hasText(operator)) {
            return SYSTEM_OPERATOR;
        }

        return operator.trim();
    }

    /**
     * 获取必填的任务数据。
     *
     * @param data Quartz 任务数据
     * @param key  数据键
     * @return 数据内容
     */
    private String requiredText(
            JobDataMap data,
            String key) {

        String value = data.getString(key);

        if (!hasText(value)) {
            throw new IllegalStateException(
                    "Missing required job data: " + key);
        }

        return value.trim();
    }
}