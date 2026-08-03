package io.yak.framework.schedule.api;

/** 调度器级别策略，不包含业务任务失败后的重试语义。 */
public record SchedulePolicy(
        ConcurrencyPolicy concurrencyPolicy,
        MisfirePolicy misfirePolicy,
        int triggerRetries) {

    public SchedulePolicy {
        concurrencyPolicy = concurrencyPolicy == null
                ? ConcurrencyPolicy.FORBID
                : concurrencyPolicy;
        misfirePolicy = misfirePolicy == null
                ? MisfirePolicy.FIRE_ONCE_NOW
                : misfirePolicy;
        if (triggerRetries < 0) {
            throw new IllegalArgumentException("triggerRetries must not be negative");
        }
    }

    public static SchedulePolicy defaults() {
        return new SchedulePolicy(
                ConcurrencyPolicy.FORBID,
                MisfirePolicy.FIRE_ONCE_NOW,
                0);
    }
}
