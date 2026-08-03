package io.yak.framework.schedule.api;

import java.util.List;
import java.util.Optional;

/** 调度引擎插件 SPI。每个插件以 Spring Bean 形式自动注册。 */
public interface ScheduleEngine {
    String type();

    ScheduleEngineCapabilities capabilities();

    ScheduleSnapshot save(ScheduleDefinition definition);

    void pause(ScheduleKey key);

    void resume(ScheduleKey key);

    void delete(ScheduleKey key);

    ScheduleTriggerResult runNow(ScheduleKey key);

    Optional<ScheduleSnapshot> get(ScheduleKey key);

    List<ScheduleSnapshot> list(String namespace);
}
