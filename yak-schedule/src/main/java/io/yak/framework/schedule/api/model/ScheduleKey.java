package io.yak.framework.schedule.api.model;

import java.util.Objects;

/** Stable business identity of a schedule. */
public final class ScheduleKey {

    private final String namespace;
    private final String name;

    public ScheduleKey(String namespace, String name) {
        this.namespace = requireText(namespace, "namespace");
        this.name = requireText(name, "name");
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public String value() {
        return namespace + ":" + name;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object value) {
        if (this == value) {
            return true;
        }
        if (!(value instanceof ScheduleKey)) {
            return false;
        }
        ScheduleKey other = (ScheduleKey) value;
        return namespace.equals(other.namespace) && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name);
    }

    @Override
    public String toString() {
        return value();
    }
}
