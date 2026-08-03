package io.yak.framework.schedule.api.model;

import io.yak.framework.schedule.model.ConcurrencyPolicy;

/** Trigger-level behavior; business execution retries remain in the business module. */
public final class SchedulePolicy {

    private final ConcurrencyPolicy concurrencyPolicy;
    private final MisfirePolicy misfirePolicy;
    private final int maxTriggerRetries;

    public SchedulePolicy(
            ConcurrencyPolicy concurrencyPolicy,
            MisfirePolicy misfirePolicy,
            int maxTriggerRetries) {

        if (maxTriggerRetries < 0) {
            throw new IllegalArgumentException(
                    "maxTriggerRetries must not be negative");
        }
        this.concurrencyPolicy = concurrencyPolicy == null
                ? ConcurrencyPolicy.FORBID
                : concurrencyPolicy;
        this.misfirePolicy = misfirePolicy == null
                ? MisfirePolicy.SMART
                : misfirePolicy;
        this.maxTriggerRetries = maxTriggerRetries;
    }

    public static SchedulePolicy defaults() {
        return new SchedulePolicy(
                ConcurrencyPolicy.FORBID,
                MisfirePolicy.SMART,
                0);
    }

    public ConcurrencyPolicy getConcurrencyPolicy() {
        return concurrencyPolicy;
    }

    public MisfirePolicy getMisfirePolicy() {
        return misfirePolicy;
    }

    public int getMaxTriggerRetries() {
        return maxTriggerRetries;
    }
}
