package io.yak.framework.schedule.api;

/** Wraps provider-specific failures without leaking provider types. */
public final class ScheduleProviderException extends ScheduleException {

    public ScheduleProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
