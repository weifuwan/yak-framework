package io.yak.framework.schedule.api;

import java.util.List;
import java.util.Optional;

/** 可替换的调度定义仓库。生产环境应提供数据库实现。 */
public interface ScheduleDefinitionRepository {
    void save(ScheduleDefinition definition);

    Optional<ScheduleDefinition> find(ScheduleKey key);

    List<ScheduleDefinition> findByNamespace(String namespace);

    void delete(ScheduleKey key);
}
