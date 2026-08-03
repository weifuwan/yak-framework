package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.model.ScheduleKey;
import java.util.Optional;

/** Stores the selected provider for each stable schedule identity. */
public interface ScheduleEngineBindingRepository {

    void save(ScheduleKey key, String engineType);

    Optional<String> find(ScheduleKey key);

    void delete(ScheduleKey key);
}
