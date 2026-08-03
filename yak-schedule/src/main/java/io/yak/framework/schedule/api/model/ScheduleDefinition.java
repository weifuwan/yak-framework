package io.yak.framework.schedule.api.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Complete provider-neutral schedule definition. */
public final class ScheduleDefinition {

    private final ScheduleKey key;
    private final String displayName;
    private final String description;
    private final String engineType;
    private final ScheduleTrigger trigger;
    private final ScheduleTarget target;
    private final SchedulePolicy policy;
    private final boolean enabled;
    private final Long version;
    private final Map<String, String> metadata;

    public ScheduleDefinition(
            ScheduleKey key,
            String displayName,
            String description,
            String engineType,
            ScheduleTrigger trigger,
            ScheduleTarget target,
            SchedulePolicy policy,
            boolean enabled,
            Long version,
            Map<String, String> metadata) {

        this.key = Objects.requireNonNull(key, "key must not be null");
        this.displayName = textOrDefault(displayName, key.getName());
        this.description = description;
        this.engineType = normalize(engineType);
        this.trigger = Objects.requireNonNull(trigger, "trigger must not be null");
        this.target = Objects.requireNonNull(target, "target must not be null");
        this.policy = policy == null ? SchedulePolicy.defaults() : policy;
        this.enabled = enabled;
        this.version = version;
        this.metadata = immutable(metadata);
    }

    public ScheduleKey getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getEngineType() {
        return engineType;
    }

    public ScheduleTrigger getTrigger() {
        return trigger;
    }

    public ScheduleTarget getTarget() {
        return target;
    }

    public SchedulePolicy getPolicy() {
        return policy;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Long getVersion() {
        return version;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    private static String normalize(String value) {
        return value == null || value.trim().isEmpty()
                ? null
                : value.trim().toLowerCase(java.util.Locale.ROOT);
    }

    private static String textOrDefault(String value, String fallback) {
        return value == null || value.trim().isEmpty()
                ? fallback
                : value.trim();
    }

    private static Map<String, String> immutable(Map<String, String> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<>(source));
    }
}
