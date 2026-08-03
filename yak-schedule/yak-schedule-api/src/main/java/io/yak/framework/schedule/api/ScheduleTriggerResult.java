package io.yak.framework.schedule.api;

import java.time.Instant;

/** 立即触发请求被调度引擎接收后的结果。 */
public record ScheduleTriggerResult(
        String triggerId,
        String externalId,
        Instant acceptedAt) {
}
