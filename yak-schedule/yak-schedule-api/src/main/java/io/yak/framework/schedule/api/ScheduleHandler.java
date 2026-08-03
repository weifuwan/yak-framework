package io.yak.framework.schedule.api;

/** 业务系统接入调度框架的唯一执行扩展点。 */
@FunctionalInterface
public interface ScheduleHandler {
    ScheduleExecutionResult execute(ScheduleExecutionContext context) throws Exception;
}
