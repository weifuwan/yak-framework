package io.yak.framework.schedule.api;

/** 跨业务稳定的调度计划标识。 */
public record ScheduleKey(String namespace, String name) {
    public ScheduleKey {
        namespace = requireText(namespace, "namespace");
        name = requireText(name, "name");
    }

    public String value() {
        return namespace + ":" + name;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
