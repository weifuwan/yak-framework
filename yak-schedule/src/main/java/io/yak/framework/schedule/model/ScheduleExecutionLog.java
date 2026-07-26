package io.yak.framework.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 调度任务执行日志。
 *
 * <p>用于记录调度任务每次执行的时间、状态、重试次数及执行结果。</p>
 *
 * @author weifuwan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ScheduleExecutionLog {

    /**
     * 执行记录唯一标识。
     */
    String executionId;

    /**
     * 所属项目。
     */
    String project;

    /**
     * 任务名称。
     */
    String taskName;

    /**
     * 操作人。
     */
    String operator;

    /**
     * 开始执行时间。
     */
    Instant startedAt;

    /**
     * 执行完成时间。
     */
    Instant finishedAt;

    /**
     * 执行状态。
     */
    ExecutionStatus status;

    /**
     * 当前执行次数。
     */
    int attempt;

    /**
     * 执行结果消息。
     */
    String message;


}