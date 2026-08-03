package io.yak.framework.schedule.api;

/** Raised when a definition requires a capability unsupported by its provider. */
public final class UnsupportedScheduleCapabilityException
        extends ScheduleException {

    public UnsupportedScheduleCapabilityException(String message) {
        super(message);
    }
}
