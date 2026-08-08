package io.yak.framework.workflow.engine.definition;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public record NodeDefinition(
        String id,
        String name,
        TriggerRule triggerRule,
        RetryPolicy retryPolicy,
        NodeFailurePolicy failurePolicy,
        NodeTimeoutPolicy timeoutPolicy,
        Map<String, Object> configuration) {

    public NodeDefinition {
        id = requireText(id, "id");
        name = name == null || name.isBlank() ? id : name;
        triggerRule = Objects.requireNonNullElse(triggerRule, TriggerRule.ALL_SUCCESS);
        retryPolicy = Objects.requireNonNullElseGet(retryPolicy, RetryPolicy::none);
        failurePolicy = Objects.requireNonNullElse(failurePolicy, NodeFailurePolicy.FAIL_WORKFLOW);
        timeoutPolicy = Objects.requireNonNullElseGet(timeoutPolicy, NodeTimeoutPolicy::none);
        configuration = configuration == null
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(configuration));
    }

    /** Backward-compatible constructor for nodes without timeout configuration. */
    public NodeDefinition(
            String id,
            String name,
            TriggerRule triggerRule,
            RetryPolicy retryPolicy,
            NodeFailurePolicy failurePolicy,
            Map<String, Object> configuration) {
        this(
                id,
                name,
                triggerRule,
                retryPolicy,
                failurePolicy,
                NodeTimeoutPolicy.none(),
                configuration);
    }

    public static NodeDefinition task(String id) {
        return new NodeDefinition(
                id,
                id,
                TriggerRule.ALL_SUCCESS,
                RetryPolicy.none(),
                NodeFailurePolicy.FAIL_WORKFLOW,
                NodeTimeoutPolicy.none(),
                Map.of());
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
