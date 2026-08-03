package io.yak.framework.schedule.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.yak.framework.schedule.ScheduleOperationAuditRepository;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.UnsupportedScheduleCapabilityException;
import io.yak.framework.schedule.api.model.MisfirePolicy;
import io.yak.framework.schedule.api.model.ScheduleDefinition;
import io.yak.framework.schedule.api.model.ScheduleEngineCapabilities;
import io.yak.framework.schedule.api.model.ScheduleKey;
import io.yak.framework.schedule.api.model.SchedulePolicy;
import io.yak.framework.schedule.api.model.ScheduleSnapshot;
import io.yak.framework.schedule.api.model.ScheduleStatus;
import io.yak.framework.schedule.api.model.ScheduleTarget;
import io.yak.framework.schedule.api.model.ScheduleTrigger;
import io.yak.framework.schedule.api.model.ScheduleTriggerResult;
import io.yak.framework.schedule.api.model.TriggerType;
import io.yak.framework.schedule.model.ConcurrencyPolicy;
import io.yak.framework.schedule.model.ScheduleOperationAudit;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DefaultScheduleManagerTest {

    @Test
    void routesDefaultEngineAndAuditsLifecycle() {
        FakeEngine quartz = new FakeEngine(
                "quartz",
                EnumSet.of(TriggerType.CRON, TriggerType.ONE_TIME));
        RecordingAuditRepository audits =
                new RecordingAuditRepository();

        DefaultScheduleManager manager =
                new DefaultScheduleManager(
                        new ScheduleEngineRegistry(
                                Collections.singletonList(quartz)),
                        new InMemoryScheduleEngineBindingRepository(),
                        () -> "alice",
                        audits,
                        "quartz");

        ScheduleKey key = new ScheduleKey(
                "workflow",
                "daily-orders");

        manager.save(definition(key, null, ScheduleTrigger.cron(
                "0 0 2 * * ?",
                ZoneId.of("Asia/Shanghai"))));
        manager.runNow(key);
        manager.pause(key);
        manager.resume(key);

        assertThat(quartz.saved).containsKey(key);
        assertThat(audits.operations)
                .containsExactly(
                        "SAVE:alice",
                        "RUN_NOW:alice",
                        "PAUSE:alice",
                        "RESUME:alice");
    }

    @Test
    void rejectsUnsupportedTriggerBeforeProviderCall() {
        FakeEngine limited = new FakeEngine(
                "limited",
                EnumSet.of(TriggerType.CRON));

        DefaultScheduleManager manager =
                new DefaultScheduleManager(
                        new ScheduleEngineRegistry(
                                Collections.singletonList(limited)),
                        new InMemoryScheduleEngineBindingRepository(),
                        () -> "SYSTEM",
                        new RecordingAuditRepository(),
                        "limited");

        assertThatThrownBy(() -> manager.save(
                definition(
                        new ScheduleKey("quality", "once"),
                        "limited",
                        ScheduleTrigger.oneTime(
                                Instant.now().plusSeconds(60)))))
                .isInstanceOf(
                        UnsupportedScheduleCapabilityException.class);
    }

    private ScheduleDefinition definition(
            ScheduleKey key,
            String engineType,
            ScheduleTrigger trigger) {

        return new ScheduleDefinition(
                key,
                key.getName(),
                null,
                engineType,
                trigger,
                new ScheduleTarget(
                        "testHandler",
                        Map.of("definitionId", "1")),
                new SchedulePolicy(
                        ConcurrencyPolicy.FORBID,
                        MisfirePolicy.SMART,
                        0),
                true,
                1L,
                Collections.emptyMap());
    }

    private static final class RecordingAuditRepository
            implements ScheduleOperationAuditRepository {

        private final List<String> operations =
                new ArrayList<>();

        @Override
        public void save(ScheduleOperationAudit audit) {
            operations.add(
                    audit.getOperation()
                            + ":"
                            + audit.getOperator());
        }

        @Override
        public List<ScheduleOperationAudit> find(
                String project,
                String taskName) {

            return Collections.emptyList();
        }
    }

    private static final class FakeEngine
            implements ScheduleEngine {

        private final String type;
        private final ScheduleEngineCapabilities capabilities;
        private final Map<ScheduleKey, ScheduleSnapshot> saved =
                new LinkedHashMap<>();

        private FakeEngine(
                String type,
                EnumSet<TriggerType> triggers) {

            this.type = type;
            this.capabilities =
                    new ScheduleEngineCapabilities(
                            triggers,
                            true,
                            true,
                            true,
                            false);
        }

        @Override
        public String engineType() {
            return type;
        }

        @Override
        public ScheduleEngineCapabilities capabilities() {
            return capabilities;
        }

        @Override
        public ScheduleSnapshot save(
                ScheduleDefinition definition) {

            ScheduleSnapshot snapshot =
                    new ScheduleSnapshot(
                            definition,
                            ScheduleStatus.ACTIVE,
                            null,
                            null,
                            type + ":" + definition.getKey());
            saved.put(definition.getKey(), snapshot);
            return snapshot;
        }

        @Override
        public void pause(ScheduleKey key) {
        }

        @Override
        public void resume(ScheduleKey key) {
        }

        @Override
        public void delete(ScheduleKey key) {
            saved.remove(key);
        }

        @Override
        public ScheduleTriggerResult runNow(
                ScheduleKey key,
                String operator) {

            return new ScheduleTriggerResult(
                    "trigger-1",
                    Instant.now());
        }

        @Override
        public Optional<ScheduleSnapshot> get(ScheduleKey key) {
            return Optional.ofNullable(saved.get(key));
        }

        @Override
        public List<ScheduleSnapshot> list(String namespace) {
            return new ArrayList<>(saved.values());
        }
    }
}
