package io.yak.framework.workflow.engine.execution;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Immutable executor-facing context for one concrete node attempt. */
public record NodeExecutionContext(
        String workflowExecutionId,
        String nodeExecutionId,
        String nodeId,
        String attemptId,
        int attemptNumber,
        Instant availableAt,
        Instant dispatchDeadline,
        Duration executionTimeout,
        Map<String, Object> workflowInput,
        Map<String, Map<String, Object>> predecessorOutputs,
        Map<String, Object> nodeInput,
        Map<String, Object> nodeConfiguration) {

    public NodeExecutionContext {
        workflowExecutionId = Objects.requireNonNull(workflowExecutionId, "workflowExecutionId");
        nodeExecutionId = Objects.requireNonNull(nodeExecutionId, "nodeExecutionId");
        nodeId = Objects.requireNonNull(nodeId, "nodeId");
        attemptId = Objects.requireNonNull(attemptId, "attemptId");
        availableAt = Objects.requireNonNull(availableAt, "availableAt");
        executionTimeout = Objects.requireNonNullElse(executionTimeout, Duration.ZERO);
        workflowInput = immutableMap(workflowInput);
        predecessorOutputs = immutableNestedMap(predecessorOutputs);
        nodeInput = immutableMap(nodeInput);
        nodeConfiguration = immutableMap(nodeConfiguration);
    }

    private static Map<String, Object> immutableMap(Map<String, Object> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<>(source));
    }

    private static Map<String, Map<String, Object>> immutableNestedMap(
            Map<String, Map<String, Object>> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        Map<String, Map<String, Object>> copy = new LinkedHashMap<>();
        source.forEach((nodeId, output) -> copy.put(nodeId, immutableMap(output)));
        return Collections.unmodifiableMap(copy);
    }
}
