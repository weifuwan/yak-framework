package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleDefinitionRepository;
import io.yak.framework.schedule.api.ScheduleKey;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** 开发环境默认调度定义仓库。 */
public final class InMemoryScheduleDefinitionRepository
        implements ScheduleDefinitionRepository {

    private final ConcurrentMap<ScheduleKey, ScheduleDefinition> definitions =
            new ConcurrentHashMap<>();

    @Override
    public void save(ScheduleDefinition definition) {
        definitions.put(definition.key(), definition);
    }

    @Override
    public Optional<ScheduleDefinition> find(ScheduleKey key) {
        return Optional.ofNullable(definitions.get(key));
    }

    @Override
    public List<ScheduleDefinition> findByNamespace(String namespace) {
        List<ScheduleDefinition> result = new ArrayList<>();
        for (ScheduleDefinition definition : definitions.values()) {
            if (definition.key().namespace().equals(namespace)) {
                result.add(definition);
            }
        }
        result.sort(Comparator.comparing(item -> item.key().name()));
        return List.copyOf(result);
    }

    @Override
    public void delete(ScheduleKey key) {
        definitions.remove(key);
    }
}
