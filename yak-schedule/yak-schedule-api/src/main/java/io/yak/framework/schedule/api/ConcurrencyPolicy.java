package io.yak.framework.schedule.api;

/** 同一调度计划尚未完成时的并发策略。 */
public enum ConcurrencyPolicy {
    ALLOW,
    FORBID
}
