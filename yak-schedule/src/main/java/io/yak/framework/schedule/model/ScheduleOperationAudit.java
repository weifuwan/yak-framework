package io.yak.framework.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 调度任务操作审计记录。
 *
 * <p>用于记录操作人对调度任务执行的操作及操作时间。</p>
 *
 * @author weifuwan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ScheduleOperationAudit {

    /**
     * 所属项目。
     */
    private String project;

    /**
     * 任务名称。
     */
    private String taskName;

    /**
     * 操作类型。
     */
    private String operation;

    /**
     * 操作人。
     */
    private String operator;

    /**
     * 操作时间。
     */
    private Instant operatedAt;
}