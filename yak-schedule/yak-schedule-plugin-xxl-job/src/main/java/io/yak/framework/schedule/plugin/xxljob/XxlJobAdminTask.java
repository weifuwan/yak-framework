package io.yak.framework.schedule.plugin.xxljob;

/** XXL-JOB Admin 中的任务运行快照。 */
public record XxlJobAdminTask(
        String id,
        int triggerStatus,
        long triggerLastTime,
        long triggerNextTime) {
}
