package io.yak.framework.schedule.api;

/** 调度引擎插件调用失败。 */
public final class ScheduleProviderException extends ScheduleException {
    public ScheduleProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleProviderException(String message) {
        super(message);
    }
}
