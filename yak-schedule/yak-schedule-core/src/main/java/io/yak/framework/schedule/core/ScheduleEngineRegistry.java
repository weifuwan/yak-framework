package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** 自动收集 Spring 容器中的调度引擎插件。 */
public final class ScheduleEngineRegistry {
    private final Map<String, ScheduleEngine> engines;

    public ScheduleEngineRegistry(List<ScheduleEngine> candidates) {
        Map<String, ScheduleEngine> registered = new LinkedHashMap<>();
        for (ScheduleEngine engine : candidates) {
            String type = normalize(engine.type());
            ScheduleEngine previous = registered.putIfAbsent(type, engine);
            if (previous != null) {
                throw new IllegalStateException(
                        "Duplicate schedule engine type: " + type
                                + " (" + previous.getClass().getName()
                                + ", " + engine.getClass().getName() + ")");
            }
        }
        this.engines = Collections.unmodifiableMap(registered);
    }

    public ScheduleEngine required(String type) {
        String normalized = normalize(type);
        ScheduleEngine engine = engines.get(normalized);
        if (engine == null) {
            throw new ScheduleException(
                    "Schedule engine '" + normalized + "' is not registered. Available: "
                            + engines.keySet());
        }
        return engine;
    }

    public Set<String> types() {
        return engines.keySet();
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("schedule engine type must not be blank");
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
