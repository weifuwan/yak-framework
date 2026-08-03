package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/** Validates and resolves installed schedule providers. */
public final class ScheduleEngineRegistry {

    private final Map<String, ScheduleEngine> engines;

    public ScheduleEngineRegistry(Collection<ScheduleEngine> engines) {
        if (engines == null || engines.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one schedule engine is required");
        }

        Map<String, ScheduleEngine> registered =
                new LinkedHashMap<>();

        for (ScheduleEngine engine : engines) {
            if (engine == null) {
                continue;
            }
            String type = normalize(engine.engineType());
            ScheduleEngine previous = registered.put(type, engine);
            if (previous != null) {
                throw new IllegalStateException(
                        "Duplicate schedule engine: " + type);
            }
        }

        if (registered.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one schedule engine is required");
        }

        this.engines = Collections.unmodifiableMap(registered);
    }

    public ScheduleEngine require(String engineType) {
        String normalized = normalize(engineType);
        ScheduleEngine engine = engines.get(normalized);
        if (engine == null) {
            throw new ScheduleException(
                    "Unknown schedule engine: " + normalized);
        }
        return engine;
    }

    public Map<String, ScheduleEngine> all() {
        return engines;
    }

    private static String normalize(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "engineType must not be blank");
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
