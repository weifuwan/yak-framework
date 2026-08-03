package io.yak.framework.schedule.api.model;

/** Policy applied when a trigger fire time was missed. */
public enum MisfirePolicy {
    SMART,
    IGNORE,
    FIRE_ONCE_NOW
}
