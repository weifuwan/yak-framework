package io.yak.framework.schedule.api;

/** 当前调度引擎不支持定义所需能力。 */
public final class UnsupportedScheduleCapabilityException extends ScheduleException {
    public UnsupportedScheduleCapabilityException(String message) {
        super(message);
    }
}
