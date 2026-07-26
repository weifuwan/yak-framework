package io.yak.framework.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.Map;

/**
 * 调度任务定义。
 *
 * <p>描述一个项目范围内的完整调度任务，包括处理器、Cron 表达式、
 * 时区、并发策略、重试次数以及运行参数。</p>
 *
 * @author weifuwan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ScheduleTaskDefinition {

    /**
     * 所属项目。
     */
    private String project;

    /**
     * 任务名称。
     */
    private String name;

    /**
     * 任务描述。
     */
    private String description;

    /**
     * 任务处理器名称。
     */
    private String handler;

    /**
     * Cron 表达式。
     */
    private String cron;

    /**
     * 调度时区。
     */
    private ZoneId zoneId;

    /**
     * 并发执行策略。
     */
    private ConcurrencyPolicy concurrencyPolicy;

    /**
     * 最大重试次数。
     */
    private int maxRetries;

    /**
     * 任务运行参数。
     */
    private Map<String, String> parameters;


}