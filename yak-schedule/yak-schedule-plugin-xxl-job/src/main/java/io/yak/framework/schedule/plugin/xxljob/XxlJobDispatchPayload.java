package io.yak.framework.schedule.plugin.xxljob;

import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.ScheduleTarget;

/** 发送给通用 XXL-JOB Handler 的参数。 */
public record XxlJobDispatchPayload(
        String triggerId,
        ScheduleKey key,
        ScheduleTarget target) {
}
