package io.yak.framework.schedule.api;

/** Business extension point invoked when a schedule fires. */
@FunctionalInterface
public interface ScheduleHandler {

    ScheduleExecutionResult execute(
            ScheduleExecutionContext context)
            throws Exception;
}
