package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ScheduleEngineBinding;
import io.yak.framework.schedule.api.ScheduleEngineBindingRepository;
import io.yak.framework.schedule.api.ScheduleKey;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** 开发环境默认外部任务绑定仓库。 */
public final class InMemoryScheduleEngineBindingRepository
        implements ScheduleEngineBindingRepository {

    private final ConcurrentMap<String, ScheduleEngineBinding> bindings =
            new ConcurrentHashMap<>();

    @Override
    public void save(ScheduleEngineBinding binding) {
        bindings.put(key(binding.key(), binding.engineType()), binding);
    }

    @Override
    public Optional<ScheduleEngineBinding> find(
            ScheduleKey scheduleKey,
            String engineType) {
        return Optional.ofNullable(bindings.get(key(scheduleKey, engineType)));
    }

    @Override
    public void delete(ScheduleKey scheduleKey, String engineType) {
        bindings.remove(key(scheduleKey, engineType));
    }

    private static String key(ScheduleKey scheduleKey, String engineType) {
        return engineType + "|" + scheduleKey.value();
    }
}
