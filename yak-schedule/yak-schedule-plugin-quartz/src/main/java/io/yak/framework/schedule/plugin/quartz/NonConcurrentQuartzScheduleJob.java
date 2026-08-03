package io.yak.framework.schedule.plugin.quartz;

import org.quartz.DisallowConcurrentExecution;

/** 禁止同一 Quartz JobDetail 并发执行。 */
@DisallowConcurrentExecution
public final class NonConcurrentQuartzScheduleJob extends QuartzScheduleJob {
}
