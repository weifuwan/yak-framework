package io.yak.framework.schedule.api;

/** 调度器错过计划时间后的处理策略。 */
public enum MisfirePolicy {
    IGNORE,
    FIRE_ONCE_NOW
}
