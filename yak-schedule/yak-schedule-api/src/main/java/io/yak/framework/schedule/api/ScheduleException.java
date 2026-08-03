package io.yak.framework.schedule.api;

/** 调度框架统一异常基类。 */
public class ScheduleException extends RuntimeException {
    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
