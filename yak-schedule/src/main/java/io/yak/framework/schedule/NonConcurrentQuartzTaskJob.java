package io.yak.framework.schedule;

import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public final class NonConcurrentQuartzTaskJob extends QuartzTaskJob {
}
