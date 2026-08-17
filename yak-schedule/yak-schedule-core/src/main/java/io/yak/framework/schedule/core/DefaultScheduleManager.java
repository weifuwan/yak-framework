package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleDefinitionRepository;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleEngineCapabilities;
import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.ScheduleManager;
import io.yak.framework.schedule.api.ScheduleOperationAudit;
import io.yak.framework.schedule.api.ScheduleOperationAuditRepository;
import io.yak.framework.schedule.api.ScheduleSnapshot;
import io.yak.framework.schedule.api.ScheduleStatus;
import io.yak.framework.schedule.api.ScheduleTriggerResult;
import io.yak.framework.schedule.api.TriggerType;
import io.yak.framework.schedule.api.UnsupportedScheduleCapabilityException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/** 只依赖 SPI 的统一调度门面。 */
public final class DefaultScheduleManager implements ScheduleManager {
    private final ScheduleProperties properties;
    private final ScheduleEngineRegistry registry;
    private final ScheduleDefinitionRepository definitionRepository;
    private final ScheduleOperationAuditRepository auditRepository;
    private final CurrentOperatorProvider operatorProvider;

    public DefaultScheduleManager(
            ScheduleProperties properties,
            ScheduleEngineRegistry registry,
            ScheduleDefinitionRepository definitionRepository,
            ScheduleOperationAuditRepository auditRepository,
            CurrentOperatorProvider operatorProvider) {
        this.properties = properties;
        this.registry = registry;
        this.definitionRepository = definitionRepository;
        this.auditRepository = auditRepository;
        this.operatorProvider = operatorProvider;
    }

    @Override
    public ScheduleSnapshot save(ScheduleDefinition definition) {
        ScheduleEngine engine = engine();
        validateCapabilities(engine.capabilities(), definition);
        if (definitionRepository.find(definition.key()).isEmpty()
                && !engine.capabilities().dynamicCreate()) {
            throw unsupported("dynamic schedule creation");
        }
        ScheduleSnapshot snapshot = engine.save(definition);
        definitionRepository.save(definition);
        audit(definition.key(), "SAVE");
        return normalize(snapshot);
    }

    @Override
    public void pause(ScheduleKey key) {
        ScheduleEngine engine = engine();
        requirePauseResume(engine);
        engine.pause(key);
        updateEnabled(key, false);
        audit(key, "PAUSE");
    }

    @Override
    public void resume(ScheduleKey key) {
        ScheduleEngine engine = engine();
        requirePauseResume(engine);
        engine.resume(key);
        updateEnabled(key, true);
        audit(key, "RESUME");
    }

    @Override
    public void delete(ScheduleKey key) {
        engine().delete(key);
        definitionRepository.delete(key);
        audit(key, "DELETE");
    }

    @Override
    public ScheduleTriggerResult runNow(ScheduleKey key) {
        ScheduleEngine engine = engine();
        if (!engine.capabilities().runNow()) {
            throw unsupported("run now");
        }
        ScheduleTriggerResult result = engine.runNow(key);
        audit(key, "RUN_NOW");
        return result;
    }

    @Override
    public Optional<ScheduleSnapshot> get(ScheduleKey key) {
        return engine().get(key).map(this::normalize);
    }

    @Override
    public List<ScheduleSnapshot> list(String namespace) {
        if (namespace == null || namespace.isBlank()) {
            throw new IllegalArgumentException("namespace must not be blank");
        }
        return engine().list(namespace.trim()).stream()
                .map(this::normalize)
                .toList();
    }

    private ScheduleEngine engine() {
        return registry.required(properties.getEngine());
    }

    private void validateCapabilities(
            ScheduleEngineCapabilities capabilities,
            ScheduleDefinition definition) {
        TriggerType triggerType = definition.trigger().type();
        if (!capabilities.supports(triggerType)) {
            throw new UnsupportedScheduleCapabilityException(
                    "Engine '" + properties.getEngine()
                            + "' does not support trigger type " + triggerType);
        }
        if (!capabilities.supports(definition.policy().concurrencyPolicy())) {
            throw new UnsupportedScheduleCapabilityException(
                    "Engine '" + properties.getEngine()
                            + "' does not support concurrency policy "
                            + definition.policy().concurrencyPolicy());
        }
        if (!capabilities.supports(definition.policy().misfirePolicy())) {
            throw new UnsupportedScheduleCapabilityException(
                    "Engine '" + properties.getEngine()
                            + "' does not support misfire policy "
                            + definition.policy().misfirePolicy());
        }
        if (triggerType == TriggerType.CRON
                && !capabilities.timezone()
                && definition.trigger().zoneId() != null
                && !ZoneId.systemDefault().equals(definition.trigger().zoneId())) {
            throw new UnsupportedScheduleCapabilityException(
                    "Engine '" + properties.getEngine()
                            + "' does not support per-task timezone: "
                            + definition.trigger().zoneId());
        }
        if (!definition.enabled() && !capabilities.pauseResume()) {
            throw new UnsupportedScheduleCapabilityException(
                    "Engine '" + properties.getEngine()
                            + "' cannot create a disabled schedule");
        }
    }

    private void requirePauseResume(ScheduleEngine engine) {
        if (!engine.capabilities().pauseResume()) {
            throw unsupported("pause/resume");
        }
    }

    private UnsupportedScheduleCapabilityException unsupported(String capability) {
        return new UnsupportedScheduleCapabilityException(
                "Engine '" + properties.getEngine()
                        + "' does not support " + capability);
    }

    private void updateEnabled(ScheduleKey key, boolean enabled) {
        definitionRepository.find(key)
                .map(definition -> definition.withEnabled(enabled))
                .ifPresent(definitionRepository::save);
    }

    private ScheduleSnapshot normalize(ScheduleSnapshot snapshot) {
        if (snapshot == null || snapshot.definition() == null) {
            return snapshot;
        }
        boolean enabled = snapshot.definition().enabled();
        if (snapshot.status() == ScheduleStatus.PAUSED) {
            enabled = false;
        } else if (snapshot.status() == ScheduleStatus.ENABLED) {
            enabled = true;
        }
        ScheduleDefinition definition = snapshot.definition().withEnabled(enabled);
        return new ScheduleSnapshot(
                definition,
                snapshot.engineType(),
                snapshot.externalId(),
                snapshot.status(),
                snapshot.nextFireTime(),
                snapshot.lastFireTime());
    }

    private void audit(ScheduleKey key, String operation) {
        String operator = operatorProvider.currentOperator();
        if (operator == null || operator.isBlank()) {
            operator = "SYSTEM";
        }
        auditRepository.save(new ScheduleOperationAudit(
                key,
                operation,
                operator,
                Instant.now()));
    }
}
