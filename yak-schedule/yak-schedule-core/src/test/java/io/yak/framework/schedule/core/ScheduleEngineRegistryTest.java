package io.yak.framework.schedule.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleEngineCapabilities;
import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.ScheduleSnapshot;
import io.yak.framework.schedule.api.ScheduleTriggerResult;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ScheduleEngineRegistryTest {

    @Test
    void automaticallyIndexesEnginesByType() {
        ScheduleEngineRegistry registry = new ScheduleEngineRegistry(
                List.of(new StubEngine("quartz"), new StubEngine("xxl-job")));

        assertThat(registry.types()).containsExactly("quartz", "xxl-job");
        assertThat(registry.required("XXL-JOB").type()).isEqualTo("xxl-job");
    }

    @Test
    void rejectsDuplicatePluginTypes() {
        assertThatThrownBy(() -> new ScheduleEngineRegistry(
                List.of(new StubEngine("quartz"), new StubEngine("QUARTZ"))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate schedule engine type");
    }

    private record StubEngine(String type) implements ScheduleEngine {
        @Override public ScheduleEngineCapabilities capabilities() {
            return new ScheduleEngineCapabilities(
                    Set.of(), Set.of(), Set.of(), true, true, true, true);
        }
        @Override public ScheduleSnapshot save(ScheduleDefinition definition) { return null; }
        @Override public void pause(ScheduleKey key) { }
        @Override public void resume(ScheduleKey key) { }
        @Override public void delete(ScheduleKey key) { }
        @Override public ScheduleTriggerResult runNow(ScheduleKey key) { return null; }
        @Override public Optional<ScheduleSnapshot> get(ScheduleKey key) { return Optional.empty(); }
        @Override public List<ScheduleSnapshot> list(String namespace) { return List.of(); }
    }
}
