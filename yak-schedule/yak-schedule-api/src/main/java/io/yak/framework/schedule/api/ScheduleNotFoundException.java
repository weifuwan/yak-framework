package io.yak.framework.schedule.api;

/** 调度计划不存在。 */
public final class ScheduleNotFoundException extends ScheduleException {
    public ScheduleNotFoundException(ScheduleKey key) {
        super("Unknown schedule: " + key.value());
    }
}
