package io.yak.framework.workflow.engine.definition;

import java.util.Objects;

public record EdgeDefinition(String fromNodeId, String toNodeId) {

    public EdgeDefinition {
        fromNodeId = requireText(fromNodeId, "fromNodeId");
        toNodeId = requireText(toNodeId, "toNodeId");
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
