package io.yak.framework.schedule;

import org.quartz.DisallowConcurrentExecution;

/** Compatibility alias retaining the original non-concurrent job identity. */
@Deprecated
@DisallowConcurrentExecution
public final class NonConcurrentQuartzTaskJob extends QuartzTaskJob {
}
