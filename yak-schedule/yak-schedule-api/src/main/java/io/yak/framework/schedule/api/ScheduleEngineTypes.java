package io.yak.framework.schedule.api;

/** 内置调度引擎类型。第三方插件可以使用自己的唯一字符串。 */
public final class ScheduleEngineTypes {
    public static final String QUARTZ = "quartz";
    public static final String XXL_JOB = "xxl-job";

    private ScheduleEngineTypes() {
    }
}
