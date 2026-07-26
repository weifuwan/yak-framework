package io.yak.framework.schedule;

import io.yak.framework.schedule.model.ConcurrencyPolicy;
import io.yak.framework.schedule.model.ScheduleExecutionLog;
import io.yak.framework.schedule.model.ScheduleOperationAudit;
import io.yak.framework.schedule.model.ScheduleTaskDefinition;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

/**
 * 调度任务服务。
 *
 * <p>负责项目范围内调度任务的保存、暂停、恢复、立即执行、
 * 删除、查询以及操作审计。</p>
 *
 * @author weifuwan
 */
@RequiredArgsConstructor
public class ScheduleTaskService {

    /**
     * 未知操作人。
     */
    private static final String UNKNOWN_OPERATOR = "UNKNOWN";

    /**
     * 任务参数前缀。
     */
    private static final String PARAMETER_PREFIX = "parameter.";

    /**
     * Quartz JobDataMap 数据键。
     */
    private static final String DATA_PROJECT = "project";
    private static final String DATA_TASK = "task";
    private static final String DATA_HANDLER = "handler";
    private static final String DATA_DESCRIPTION = "description";
    private static final String DATA_CONCURRENCY_POLICY =
            "concurrencyPolicy";
    private static final String DATA_MAX_RETRIES = "maxRetries";
    private static final String DATA_OPERATOR = "operator";

    /**
     * 审计操作类型。
     */
    private static final String OPERATION_SAVE = "SAVE";
    private static final String OPERATION_PAUSE = "PAUSE";
    private static final String OPERATION_RESUME = "RESUME";
    private static final String OPERATION_RUN_NOW = "RUN_NOW";
    private static final String OPERATION_DELETE = "DELETE";

    /**
     * Quartz 调度器。
     */
    private final Scheduler scheduler;

    /**
     * 当前操作人提供器。
     */
    private final CurrentOperatorProvider operatorProvider;

    /**
     * 调度执行日志仓库。
     */
    private final ScheduleExecutionLogRepository logRepository;

    /**
     * 调度操作审计仓库。
     */
    private final ScheduleOperationAuditRepository auditRepository;

    /**
     * 构建 Quartz 任务键。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @return Quartz 任务键
     */
    private static JobKey key(
            String project,
            String taskName) {

        validateIdentity(project, taskName);
        return JobKey.jobKey(taskName, project);
    }

    /**
     * 规范化并校验任务定义。
     *
     * <p>由于当前任务定义使用 Lombok {@code @Data}，
     * 参数校验和默认值处理统一放在服务层完成。</p>
     *
     * @param definition 原始任务定义
     * @return 规范化后的任务定义
     */
    private static ScheduleTaskDefinition normalize(
            ScheduleTaskDefinition definition) {

        Objects.requireNonNull(
                definition,
                "definition must not be null");

        requireText(
                definition.getProject(),
                "project");
        requireText(
                definition.getName(),
                "name");
        requireText(
                definition.getHandler(),
                "handler");
        requireText(
                definition.getCron(),
                "cron");

        if (definition.getMaxRetries() < 0) {
            throw new IllegalArgumentException(
                    "maxRetries must not be negative");
        }

        ZoneId zoneId =
                definition.getZoneId() == null
                        ? ZoneId.systemDefault()
                        : definition.getZoneId();

        ConcurrencyPolicy concurrencyPolicy =
                definition.getConcurrencyPolicy() == null
                        ? ConcurrencyPolicy.FORBID
                        : definition.getConcurrencyPolicy();

        Map<String, String> parameters =
                immutableMap(definition.getParameters());

        return new ScheduleTaskDefinition(
                definition.getProject(),
                definition.getName(),
                definition.getDescription(),
                definition.getHandler(),
                definition.getCron(),
                zoneId,
                concurrencyPolicy,
                definition.getMaxRetries(),
                parameters);
    }

    /**
     * 校验项目名称和任务名称。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     */
    private static void validateIdentity(
            String project,
            String taskName) {

        requireText(project, "project");
        requireText(taskName, "taskName");
    }

    /**
     * 校验字符串不能为空。
     *
     * @param value     字符串
     * @param fieldName 字段名称
     */
    private static void requireText(
            String value,
            String fieldName) {

        if (!hasText(value)) {
            throw new IllegalArgumentException(
                    fieldName + " must not be blank");
        }
    }

    /**
     * 判断字符串是否包含有效文本。
     *
     * @param value 字符串
     * @return 是否包含有效文本
     */
    private static boolean hasText(String value) {
        return value != null
                && !value.trim().isEmpty();
    }

    /**
     * 返回非空字符串。
     *
     * @param value 原始字符串
     * @return 非空字符串
     */
    private static String defaultString(
            String value) {

        return value == null ? "" : value;
    }

    /**
     * 创建不可修改的 Map 副本。
     *
     * @param source 原始 Map
     * @return 不可修改的 Map
     */
    private static Map<String, String> immutableMap(
            Map<String, String> source) {

        if (source == null || source.isEmpty()) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(
                new LinkedHashMap<>(source));
    }

    /**
     * 保存或更新调度任务。
     *
     * @param definition 调度任务定义
     * @throws SchedulerException Quartz 调度异常
     */
    public void save(
            ScheduleTaskDefinition definition)
            throws SchedulerException {

        ScheduleTaskDefinition normalized =
                normalize(definition);

        JobKey jobKey = key(
                normalized.getProject(),
                normalized.getName());

        JobDetail jobDetail = buildJobDetail(
                jobKey,
                normalized);

        CronTrigger trigger = buildCronTrigger(
                jobKey,
                normalized);

        saveOrUpdate(jobKey, jobDetail, trigger);

        audit(
                normalized.getProject(),
                normalized.getName(),
                OPERATION_SAVE);
    }

    /**
     * 暂停调度任务。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @throws SchedulerException Quartz 调度异常
     */
    public void pause(
            String project,
            String taskName)
            throws SchedulerException {

        scheduler.pauseJob(required(project, taskName));
        audit(project, taskName, OPERATION_PAUSE);
    }

    /**
     * 恢复调度任务。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @throws SchedulerException Quartz 调度异常
     */
    public void resume(
            String project,
            String taskName)
            throws SchedulerException {

        scheduler.resumeJob(required(project, taskName));
        audit(project, taskName, OPERATION_RESUME);
    }

    /**
     * 立即执行调度任务。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @throws SchedulerException Quartz 调度异常
     */
    public void runNow(
            String project,
            String taskName)
            throws SchedulerException {

        JobDataMap data = new JobDataMap();
        data.put(DATA_OPERATOR, operator());

        scheduler.triggerJob(
                required(project, taskName),
                data);

        audit(project, taskName, OPERATION_RUN_NOW);
    }

    /**
     * 删除调度任务。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @throws SchedulerException Quartz 调度异常
     */
    public void delete(
            String project,
            String taskName)
            throws SchedulerException {

        scheduler.deleteJob(required(project, taskName));
        audit(project, taskName, OPERATION_DELETE);
    }

    /**
     * 查询指定调度任务。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @return 调度任务定义
     * @throws SchedulerException Quartz 调度异常
     */
    public ScheduleTaskDefinition get(
            String project,
            String taskName)
            throws SchedulerException {

        return definition(required(project, taskName));
    }

    /**
     * 查询项目下的全部调度任务。
     *
     * @param project 项目名称
     * @return 调度任务定义列表
     * @throws SchedulerException Quartz 调度异常
     */
    public List<ScheduleTaskDefinition> list(
            String project)
            throws SchedulerException {

        requireText(project, "project");

        List<ScheduleTaskDefinition> definitions =
                new ArrayList<>();

        for (JobKey jobKey : scheduler.getJobKeys(
                GroupMatcher.jobGroupEquals(project))) {

            definitions.add(definition(jobKey));
        }

        /*
         * Quartz 返回的 JobKey 集合无固定顺序，
         * 按任务名称排序以保证接口结果稳定。
         */
        Collections.sort(
                definitions,
                Comparator.comparing(
                        ScheduleTaskDefinition::getName));

        return Collections.unmodifiableList(definitions);
    }

    /**
     * 查询任务执行日志。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @return 执行日志列表
     */
    public List<ScheduleExecutionLog> logs(
            String project,
            String taskName) {

        validateIdentity(project, taskName);
        return logRepository.find(project, taskName);
    }

    /**
     * 查询任务操作审计日志。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @return 操作审计列表
     */
    public List<ScheduleOperationAudit> audits(
            String project,
            String taskName) {

        validateIdentity(project, taskName);
        return auditRepository.find(project, taskName);
    }

    /**
     * 保存或更新 Quartz 任务。
     *
     * <p>更新任务时不先删除原任务，避免新任务保存失败后，
     * 原有任务也被删除。</p>
     *
     * @param jobKey    任务键
     * @param jobDetail 任务详情
     * @param trigger   Cron 触发器
     * @throws SchedulerException Quartz 调度异常
     */
    private void saveOrUpdate(
            JobKey jobKey,
            JobDetail jobDetail,
            CronTrigger trigger)
            throws SchedulerException {

        if (!scheduler.checkExists(jobKey)) {
            scheduler.scheduleJob(jobDetail, trigger);
            return;
        }

        /*
         * 替换已有 JobDetail，保留任务身份。
         */
        scheduler.addJob(jobDetail, true);

        if (scheduler.checkExists(trigger.getKey())) {
            scheduler.rescheduleJob(
                    trigger.getKey(),
                    trigger);
        } else {
            scheduler.scheduleJob(trigger);
        }
    }

    /**
     * 构建 Quartz 任务详情。
     *
     * @param jobKey     任务键
     * @param definition 任务定义
     * @return Quartz 任务详情
     */
    private JobDetail buildJobDetail(
            JobKey jobKey,
            ScheduleTaskDefinition definition) {

        Class<? extends Job> jobClass =
                resolveJobClass(
                        definition.getConcurrencyPolicy());

        return JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .usingJobData(buildJobDataMap(definition))
                .build();
    }

    /**
     * 构建 Cron 触发器。
     *
     * @param jobKey     任务键
     * @param definition 任务定义
     * @return Cron 触发器
     */
    private CronTrigger buildCronTrigger(
            JobKey jobKey,
            ScheduleTaskDefinition definition) {

        CronScheduleBuilder scheduleBuilder =
                CronScheduleBuilder
                        .cronSchedule(definition.getCron())
                        .inTimeZone(TimeZone.getTimeZone(
                                definition.getZoneId()));

        return TriggerBuilder.newTrigger()
                .withIdentity(
                        definition.getName(),
                        definition.getProject())
                .forJob(jobKey)
                .withSchedule(scheduleBuilder)
                .build();
    }

    /**
     * 构建 Quartz 任务数据。
     *
     * @param definition 调度任务定义
     * @return Quartz 任务数据
     */
    private JobDataMap buildJobDataMap(
            ScheduleTaskDefinition definition) {

        JobDataMap data = new JobDataMap();

        data.put(DATA_PROJECT, definition.getProject());
        data.put(DATA_TASK, definition.getName());
        data.put(DATA_HANDLER, definition.getHandler());
        data.put(
                DATA_DESCRIPTION,
                defaultString(definition.getDescription()));
        data.put(
                DATA_CONCURRENCY_POLICY,
                definition.getConcurrencyPolicy().name());
        data.put(
                DATA_MAX_RETRIES,
                definition.getMaxRetries());

        for (Map.Entry<String, String> entry
                : definition.getParameters().entrySet()) {

            data.put(
                    PARAMETER_PREFIX + entry.getKey(),
                    entry.getValue());
        }

        return data;
    }

    /**
     * 根据并发策略获取 Quartz Job 类型。
     *
     * @param policy 并发策略
     * @return Quartz Job 类型
     */
    private Class<? extends Job> resolveJobClass(
            ConcurrencyPolicy policy) {

        if (policy == ConcurrencyPolicy.FORBID) {
            return NonConcurrentQuartzTaskJob.class;
        }

        return QuartzTaskJob.class;
    }

    /**
     * 获取必定存在的任务键。
     *
     * @param project  项目名称
     * @param taskName 任务名称
     * @return Quartz 任务键
     * @throws SchedulerException Quartz 调度异常
     */
    private JobKey required(
            String project,
            String taskName)
            throws SchedulerException {

        JobKey jobKey = key(project, taskName);

        if (!scheduler.checkExists(jobKey)) {
            throw new IllegalArgumentException(
                    "Unknown schedule task: "
                            + project
                            + "/"
                            + taskName);
        }

        return jobKey;
    }

    /**
     * 从 Quartz 中还原任务定义。
     *
     * @param jobKey Quartz 任务键
     * @return 调度任务定义
     * @throws SchedulerException Quartz 调度异常
     */
    private ScheduleTaskDefinition definition(
            JobKey jobKey)
            throws SchedulerException {

        JobDetail jobDetail =
                scheduler.getJobDetail(jobKey);

        if (jobDetail == null) {
            throw new IllegalArgumentException(
                    "Unknown schedule task: "
                            + jobKey.getGroup()
                            + "/"
                            + jobKey.getName());
        }

        CronTrigger trigger =
                findCronTrigger(jobKey);

        JobDataMap data =
                jobDetail.getJobDataMap();

        return new ScheduleTaskDefinition(
                jobKey.getGroup(),
                jobKey.getName(),
                data.getString(DATA_DESCRIPTION),
                data.getString(DATA_HANDLER),
                trigger.getCronExpression(),
                trigger.getTimeZone().toZoneId(),
                resolveConcurrencyPolicy(data),
                data.getInt(DATA_MAX_RETRIES),
                extractParameters(data));
    }

    /**
     * 查询任务对应的 Cron 触发器。
     *
     * @param jobKey Quartz 任务键
     * @return Cron 触发器
     * @throws SchedulerException Quartz 调度异常
     */
    private CronTrigger findCronTrigger(
            JobKey jobKey)
            throws SchedulerException {

        return scheduler.getTriggersOfJob(jobKey)
                .stream()
                .filter(CronTrigger.class::isInstance)
                .map(CronTrigger.class::cast)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Task has no Cron trigger: "
                                        + jobKey));
    }

    /**
     * 从 Quartz 数据中获取并发策略。
     *
     * @param data Quartz 任务数据
     * @return 并发策略
     */
    private ConcurrencyPolicy resolveConcurrencyPolicy(
            JobDataMap data) {

        String policy =
                data.getString(DATA_CONCURRENCY_POLICY);

        if (!hasText(policy)) {
            return ConcurrencyPolicy.FORBID;
        }

        try {
            return ConcurrencyPolicy.valueOf(policy);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "Invalid concurrency policy: " + policy,
                    exception);
        }
    }

    /**
     * 提取任务运行参数。
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

            if (!key.startsWith(PARAMETER_PREFIX)) {
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
     * 保存操作审计日志。
     *
     * @param project   项目名称
     * @param taskName  任务名称
     * @param operation 操作类型
     */
    private void audit(
            String project,
            String taskName,
            String operation) {

        ScheduleOperationAudit audit =
                new ScheduleOperationAudit(
                        project,
                        taskName,
                        operation,
                        operator(),
                        Instant.now());

        auditRepository.save(audit);
    }

    /**
     * 获取当前操作人。
     *
     * @return 当前操作人
     */
    private String operator() {
        String operator =
                operatorProvider.currentOperator();

        if (!hasText(operator)) {
            return UNKNOWN_OPERATOR;
        }

        return operator.trim();
    }
}