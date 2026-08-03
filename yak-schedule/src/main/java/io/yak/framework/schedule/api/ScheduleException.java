package io.yak.framework.schedule.api;

/** Base runtime exception exposed by Yak Schedule. */
public class ScheduleException extends RuntimeException {

    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
