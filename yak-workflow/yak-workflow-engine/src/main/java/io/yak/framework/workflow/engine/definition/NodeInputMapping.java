package io.yak.framework.workflow.engine.definition;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Maps executor-facing node input fields to workflow input or direct predecessor outputs.
 *
 * <p>Supported references:
 * <ul>
 *     <li>{@code $workflow} or {@code $workflow.request.id}</li>
 *     <li>{@code predecessorNodeId} or {@code predecessorNodeId.order.id}</li>
 *     <li>{@code $predecessor.predecessorNodeId.order.id}</li>
 * </ul>
 */
public final class NodeInputMapping {

    private static final NodeInputMapping NONE = new NodeInputMapping(Map.of());

    private final Map<String, String> bindings;

    private NodeInputMapping(Map<String, String> bindings) {
        Map<String, String> normalized = new LinkedHashMap<>();
        if (bindings != null) {
            bindings.forEach((target, reference) -> {
                String targetName = requireText(target, "target");
                String sourceReference = requireText(reference, "reference");
                normalized.put(targetName, sourceReference);
            });
        }
        this.bindings = Collections.unmodifiableMap(normalized);
    }

    public static NodeInputMapping none() {
        return NONE;
    }

    public static NodeInputMapping of(Map<String, String> bindings) {
        if (bindings == null || bindings.isEmpty()) {
            return NONE;
        }
        return new NodeInputMapping(bindings);
    }

    public Map<String, String> bindings() {
        return bindings;
    }

    public boolean isEmpty() {
        return bindings.isEmpty();
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
