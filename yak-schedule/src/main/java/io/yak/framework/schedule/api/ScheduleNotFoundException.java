package io.yak.framework.schedule.api;

import io.yak.framework.schedule.api.model.ScheduleKey;

/** Raised when a schedule cannot be found in the selected provider. */
public final class ScheduleNotFoundException extends ScheduleException {

    public ScheduleNotFoundException(ScheduleKey key) {
        super("Unknown schedule: " + key.value());
    }
}
