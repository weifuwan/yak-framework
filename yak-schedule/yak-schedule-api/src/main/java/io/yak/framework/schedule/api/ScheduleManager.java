package io.yak.framework.schedule.api;

import java.util.List;
import java.util.Optional;

/** 业务模块使用的统一调度门面。 */
public interface ScheduleManager {
    ScheduleSnapshot save(ScheduleDefinition definition);

    void pause(ScheduleKey key);

    void resume(ScheduleKey key);

    void delete(ScheduleKey key);

    ScheduleTriggerResult runNow(ScheduleKey key);

    Optional<ScheduleSnapshot> get(ScheduleKey key);

    List<ScheduleSnapshot> list(String namespace);
}
