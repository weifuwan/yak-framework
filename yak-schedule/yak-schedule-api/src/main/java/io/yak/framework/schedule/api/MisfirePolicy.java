package io.yak.framework.schedule.api;

/** 调度器错过计划时间后的处理策略。 */
public enum MisfirePolicy {
    /** 跳过已经错过的触发时间，等待下一次正常计划。 */
    SKIP,

    /** 恢复后合并补触发一次，然后继续后续正常计划。 */
    FIRE_ONCE,

    /**
     * 旧版跳过策略。
     *
     * @deprecated 新代码请使用 {@link #SKIP}。
     */
    @Deprecated
    IGNORE,

    /**
     * 旧版恢复后补触发一次策略。
     *
     * @deprecated 新代码请使用 {@link #FIRE_ONCE}。
     */
    @Deprecated
    FIRE_ONCE_NOW;

    public boolean skipsMissedFire() {
        return this == SKIP || this == IGNORE;
    }

    public boolean firesOnceAfterRecovery() {
        return this == FIRE_ONCE || this == FIRE_ONCE_NOW;
    }
}
