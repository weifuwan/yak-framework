package io.yak.framework.schedule.api;

import java.util.Optional;

/** 可替换的外部调度任务绑定仓库。 */
public interface ScheduleEngineBindingRepository {
    void save(ScheduleEngineBinding binding);

    Optional<ScheduleEngineBinding> find(ScheduleKey key, String engineType);

    void delete(ScheduleKey key, String engineType);
}
