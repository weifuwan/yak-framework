package io.yak.framework.schedule.api;

import io.yak.framework.schedule.api.model.ScheduleDefinition;
import io.yak.framework.schedule.api.model.ScheduleKey;
import io.yak.framework.schedule.api.model.ScheduleSnapshot;
import io.yak.framework.schedule.api.model.ScheduleTriggerResult;
import java.util.List;
import java.util.Optional;

/** Stable application-facing schedule lifecycle facade. */
public interface ScheduleManager {

    ScheduleSnapshot save(ScheduleDefinition definition);

    void pause(ScheduleKey key);

    void resume(ScheduleKey key);

    void delete(ScheduleKey key);

    ScheduleTriggerResult runNow(ScheduleKey key);

    Optional<ScheduleSnapshot> get(ScheduleKey key);

    List<ScheduleSnapshot> list(String namespace);
}
