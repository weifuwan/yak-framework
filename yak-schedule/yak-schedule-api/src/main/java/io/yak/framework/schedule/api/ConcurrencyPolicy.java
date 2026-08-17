package io.yak.framework.schedule.api;

/** 同一调度计划尚未完成时的并发策略。 */
public enum ConcurrencyPolicy {
    /** 允许同一计划并行执行。 */
    ALLOW,

    /** 不允许并行；后续触发等待前一次完成。 */
    QUEUE,

    /** 不允许并行；前一次仍在执行时丢弃后续触发。 */
    DISCARD,

    /**
     * 旧版非并发策略。语义等同于 {@link #QUEUE}，仅为兼容已有业务代码保留。
     *
     * @deprecated 新代码请使用 {@link #QUEUE}。
     */
    @Deprecated
    FORBID;

    public boolean allowsConcurrentExecution() {
        return this == ALLOW;
    }

    public boolean queuesWhenBusy() {
        return this == QUEUE || this == FORBID;
    }

    public boolean discardsWhenBusy() {
        return this == DISCARD;
    }
}
