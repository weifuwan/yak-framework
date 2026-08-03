package io.yak.framework.schedule;

import io.yak.framework.schedule.provider.quartz.QuartzScheduleJob;

/**
 * Compatibility alias for the original Quartz job class.
 *
 * <p>New provider code lives under {@code provider.quartz}.</p>
 */
@Deprecated
public class QuartzTaskJob extends QuartzScheduleJob {
}
