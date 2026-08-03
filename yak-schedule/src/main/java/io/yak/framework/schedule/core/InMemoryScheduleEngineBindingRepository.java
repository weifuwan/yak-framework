package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.model.ScheduleKey;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Default lightweight binding store; applications can replace it with JDBC. */
public final class InMemoryScheduleEngineBindingRepository
        implements ScheduleEngineBindingRepository {

    private final Map<ScheduleKey, String> bindings =
            new ConcurrentHashMap<>();

    @Override
    public void save(ScheduleKey key, String engineType) {
        bindings.put(key, engineType);
    }

    @Override
    public Optional<String> find(ScheduleKey key) {
        return Optional.ofNullable(bindings.get(key));
    }

    @Override
    public void delete(ScheduleKey key) {
        bindings.remove(key);
    }
}
