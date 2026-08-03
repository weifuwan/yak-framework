package io.yak.framework.schedule.core;

import io.yak.framework.schedule.CurrentOperatorProvider;
import io.yak.framework.schedule.ScheduleOperationAuditRepository;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleManager;
import io.yak.framework.schedule.api.ScheduleNotFoundException;
import io.yak.framework.schedule.api.UnsupportedScheduleCapabilityException;
import io.yak.framework.schedule.api.model.ScheduleDefinition;
import io.yak.framework.schedule.api.model.ScheduleKey;
import io.yak.framework.schedule.api.model.ScheduleSnapshot;
import io.yak.framework.schedule.api.model.ScheduleTriggerResult;
import io.yak.framework.schedule.model.ScheduleOperationAudit;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** Default engine-routing and audit implementation. */
public final class DefaultScheduleManager implements ScheduleManager {

    private static final String UNKNOWN_OPERATOR = "UNKNOWN";

    private final ScheduleEngineRegistry registry;
    private final ScheduleEngineBindingRepository bindings;
    private final CurrentOperatorProvider operatorProvider;
    private final ScheduleOperationAuditRepository auditRepository;
    private final String defaultEngineType;

    public DefaultScheduleManager(
            ScheduleEngineRegistry registry,
            ScheduleEngineBindingRepository bindings,
            CurrentOperatorProvider operatorProvider,
            ScheduleOperationAuditRepository auditRepository,
            String defaultEngineType) {

        this.registry = registry;
        this.bindings = bindings;
        this.operatorProvider = operatorProvider;
        this.auditRepository = auditRepository;
        this.defaultEngineType = normalize(defaultEngineType);
        registry.require(this.defaultEngineType);
    }

    @Override
    public ScheduleSnapshot save(ScheduleDefinition definition) {
        ScheduleEngine engine = engineFor(definition);
        validateCapabilities(engine, definition);

        ScheduleSnapshot snapshot = engine.save(definition);
        bindings.save(definition.getKey(), engine.engineType());
        audit(definition.getKey(), "SAVE");
        return snapshot;
    }

    @Override
    public void pause(ScheduleKey key) {
        ScheduleEngine engine = requireBoundEngine(key);
        requirePauseResume(engine);
        engine.pause(key);
        audit(key, "PAUSE");
    }

    @Override
    public void resume(ScheduleKey key) {
        ScheduleEngine engine = requireBoundEngine(key);
        requirePauseResume(engine);
        engine.resume(key);
        audit(key, "RESUME");
    }

    @Override
    public void delete(ScheduleKey key) {
        requireBoundEngine(key).delete(key);
        bindings.delete(key);
        audit(key, "DELETE");
    }

    @Override
    public ScheduleTriggerResult runNow(ScheduleKey key) {
        ScheduleEngine engine = requireBoundEngine(key);
        if (!engine.capabilities().isRunNow()) {
            throw new UnsupportedScheduleCapabilityException(
                    "Schedule engine "
                            + engine.engineType()
                            + " does not support run now");
        }
        ScheduleTriggerResult result =
                engine.runNow(key, operator());
        audit(key, "RUN_NOW");
        return result;
    }

    @Override
    public Optional<ScheduleSnapshot> get(ScheduleKey key) {
        Optional<String> binding = bindings.find(key);
        if (binding.isPresent()) {
            return registry.require(binding.get()).get(key);
        }

        ScheduleSnapshot found = null;
        String foundEngine = null;

        for (ScheduleEngine engine : registry.all().values()) {
            Optional<ScheduleSnapshot> candidate = engine.get(key);
            if (!candidate.isPresent()) {
                continue;
            }
            if (found != null) {
                throw new IllegalStateException(
                        "Schedule exists in multiple engines: "
                                + key.value());
            }
            found = candidate.get();
            foundEngine = engine.engineType();
        }

        if (found != null) {
            bindings.save(key, foundEngine);
        }
        return Optional.ofNullable(found);
    }

    @Override
    public List<ScheduleSnapshot> list(String namespace) {
        java.util.List<ScheduleSnapshot> snapshots =
                new java.util.ArrayList<>();

        for (ScheduleEngine engine : registry.all().values()) {
            for (ScheduleSnapshot snapshot : engine.list(namespace)) {
                bindings.save(
                        snapshot.getDefinition().getKey(),
                        engine.engineType());
                snapshots.add(snapshot);
            }
        }

        snapshots.sort(
                java.util.Comparator.comparing(
                        value -> value
                                .getDefinition()
                                .getKey()
                                .value()));

        return java.util.Collections.unmodifiableList(snapshots);
    }

    private ScheduleEngine engineFor(
            ScheduleDefinition definition) {

        String engineType = definition.getEngineType() == null
                ? defaultEngineType
                : definition.getEngineType();

        return registry.require(engineType);
    }

    private ScheduleEngine requireBoundEngine(ScheduleKey key) {
        Optional<String> binding = bindings.find(key);
        if (binding.isPresent()) {
            return registry.require(binding.get());
        }

        ScheduleEngine found = null;

        for (ScheduleEngine engine : registry.all().values()) {
            if (!engine.get(key).isPresent()) {
                continue;
            }
            if (found != null) {
                throw new IllegalStateException(
                        "Schedule exists in multiple engines: "
                                + key.value());
            }
            found = engine;
        }

        if (found == null) {
            throw new ScheduleNotFoundException(key);
        }

        bindings.save(key, found.engineType());
        return found;
    }

    private void requirePauseResume(ScheduleEngine engine) {
        if (!engine.capabilities().isPauseResume()) {
            throw new UnsupportedScheduleCapabilityException(
                    "Schedule engine "
                            + engine.engineType()
                            + " does not support pause and resume");
        }
    }

    private void validateCapabilities(
            ScheduleEngine engine,
            ScheduleDefinition definition) {

        if (!engine.capabilities().supports(
                definition.getTrigger().getType())) {

            throw new UnsupportedScheduleCapabilityException(
                    "Schedule engine "
                            + engine.engineType()
                            + " does not support trigger "
                            + definition.getTrigger().getType());
        }

        if (definition.getTrigger().getZoneId() != null
                && !engine.capabilities().isTimezone()) {

            throw new UnsupportedScheduleCapabilityException(
                    "Schedule engine "
                            + engine.engineType()
                            + " does not support trigger timezones");
        }
    }

    private void audit(ScheduleKey key, String operation) {
        auditRepository.save(
                new ScheduleOperationAudit(
                        key.getNamespace(),
                        key.getName(),
                        operation,
                        operator(),
                        Instant.now()));
    }

    private String operator() {
        String operator = operatorProvider.currentOperator();
        return operator == null || operator.trim().isEmpty()
                ? UNKNOWN_OPERATOR
                : operator.trim();
    }

    private static String normalize(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "defaultEngineType must not be blank");
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
