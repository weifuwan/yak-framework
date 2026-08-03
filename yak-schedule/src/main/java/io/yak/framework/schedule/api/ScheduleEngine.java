package io.yak.framework.schedule.api;

import io.yak.framework.schedule.api.model.ScheduleDefinition;
import io.yak.framework.schedule.api.model.ScheduleEngineCapabilities;
import io.yak.framework.schedule.api.model.ScheduleKey;
import io.yak.framework.schedule.api.model.ScheduleSnapshot;
import io.yak.framework.schedule.api.model.ScheduleTriggerResult;
import java.util.List;
import java.util.Optional;

/** Provider SPI implemented by Quartz, XXL-JOB and future engines. */
public interface ScheduleEngine {

    String engineType();

    ScheduleEngineCapabilities capabilities();

    ScheduleSnapshot save(ScheduleDefinition definition);

    void pause(ScheduleKey key);

    void resume(ScheduleKey key);

    void delete(ScheduleKey key);

    ScheduleTriggerResult runNow(
            ScheduleKey key,
            String operator);

    Optional<ScheduleSnapshot> get(ScheduleKey key);

    List<ScheduleSnapshot> list(String namespace);
}
