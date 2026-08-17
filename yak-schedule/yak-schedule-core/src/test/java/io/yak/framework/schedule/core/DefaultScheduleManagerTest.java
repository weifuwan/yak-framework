package io.yak.framework.schedule.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.yak.framework.schedule.api.ConcurrencyPolicy;
import io.yak.framework.schedule.api.MisfirePolicy;
import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleEngineCapabilities;
import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.ScheduleOperationAuditRepository;
import io.yak.framework.schedule.api.SchedulePolicy;
import io.yak.framework.schedule.api.ScheduleSnapshot;
import io.yak.framework.schedule.api.ScheduleStatus;
import io.yak.framework.schedule.api.ScheduleTarget;
import io.yak.framework.schedule.api.ScheduleTrigger;
import io.yak.framework.schedule.api.TriggerType;
import io.yak.framework.schedule.api.UnsupportedScheduleCapabilityException;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DefaultScheduleManagerTest {

    private static final ScheduleKey KEY = new ScheduleKey("quality", "daily-check");

    @Test
    void pauseAndResumeShouldKeepDefinitionRepositoryInSync() {
        ScheduleEngine engine = engine(capabilities(true, true));
        InMemoryScheduleDefinitionRepository repository =
                new InMemoryScheduleDefinitionRepository();
        ScheduleDefinition definition = definition(true);
        repository.save(definition);
        DefaultScheduleManager manager = manager(engine, repository);

        manager.pause(KEY);

        verify(engine).pause(KEY);
        assertThat(repository.find(KEY)).get()
                .extracting(ScheduleDefinition::enabled)
                .isEqualTo(false);

        manager.resume(KEY);

        verify(engine).resume(KEY);
        assertThat(repository.find(KEY)).get()
                .extracting(ScheduleDefinition::enabled)
                .isEqualTo(true);
    }

    @Test
    void getShouldNormalizeDefinitionEnabledFromRuntimeStatus() {
        ScheduleEngine engine = engine(capabilities(true, true));
        ScheduleDefinition definition = definition(true);
        when(engine.get(KEY)).thenReturn(Optional.of(new ScheduleSnapshot(
                definition,
                "quartz",
                "quality/daily-check",
                ScheduleStatus.PAUSED,
                null,
                null)));
        DefaultScheduleManager manager = manager(
                engine, new InMemoryScheduleDefinitionRepository());

        ScheduleSnapshot snapshot = manager.get(KEY).orElseThrow();

        assertThat(snapshot.status()).isEqualTo(ScheduleStatus.PAUSED);
        assertThat(snapshot.definition().enabled()).isFalse();
    }

    @Test
    void runNowShouldRejectEngineWithoutCapability() {
        ScheduleEngine engine = engine(capabilities(true, false));
        DefaultScheduleManager manager = manager(
                engine, new InMemoryScheduleDefinitionRepository());

        assertThatThrownBy(() -> manager.runNow(KEY))
                .isInstanceOf(UnsupportedScheduleCapabilityException.class)
                .hasMessageContaining("run now");
    }

    private static DefaultScheduleManager manager(
            ScheduleEngine engine,
            InMemoryScheduleDefinitionRepository repository) {
        ScheduleProperties properties = new ScheduleProperties();
        properties.setEngine("quartz");
        return new DefaultScheduleManager(
                properties,
                new ScheduleEngineRegistry(List.of(engine)),
                repository,
                mock(ScheduleOperationAuditRepository.class),
                () -> "test");
    }

    private static ScheduleEngine engine(ScheduleEngineCapabilities capabilities) {
        ScheduleEngine engine = mock(ScheduleEngine.class);
        when(engine.type()).thenReturn("quartz");
        when(engine.capabilities()).thenReturn(capabilities);
        return engine;
    }

    private static ScheduleEngineCapabilities capabilities(
            boolean pauseResume,
            boolean runNow) {
        return new ScheduleEngineCapabilities(
                EnumSet.allOf(TriggerType.class),
                EnumSet.allOf(ConcurrencyPolicy.class),
                EnumSet.allOf(MisfirePolicy.class),
                pauseResume,
                runNow,
                true,
                true);
    }

    private static ScheduleDefinition definition(boolean enabled) {
        return new ScheduleDefinition(
                KEY,
                "daily quality check",
                ScheduleTrigger.cron("0 0 2 * * ?", ZoneId.of("Asia/Shanghai")),
                new ScheduleTarget("qualityScheduleHandler", Map.of("monitorId", 1L)),
                SchedulePolicy.defaults(),
                enabled,
                Map.of());
    }
}
