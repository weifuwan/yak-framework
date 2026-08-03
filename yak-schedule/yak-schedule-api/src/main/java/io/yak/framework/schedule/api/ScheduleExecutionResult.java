package io.yak.framework.schedule.api;

/** 业务处理器接受本次触发后的结果。 */
public record ScheduleExecutionResult(
        boolean accepted,
        String businessExecutionId,
        String message) {

    public static ScheduleExecutionResult accepted(String businessExecutionId) {
        return new ScheduleExecutionResult(true, businessExecutionId, null);
    }

    public static ScheduleExecutionResult accepted(
            String businessExecutionId,
            String message) {
        return new ScheduleExecutionResult(true, businessExecutionId, message);
    }
}
