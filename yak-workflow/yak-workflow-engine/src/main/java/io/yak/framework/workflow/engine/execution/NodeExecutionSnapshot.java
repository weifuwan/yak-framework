package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.definition.NodeFailurePolicy;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Durable, transport-neutral snapshot of one node execution aggregate. */
public record NodeExecutionSnapshot(
        String id,
        String workflowExecutionId,
        String nodeId,
        NodeFailurePolicy failurePolicy,
        NodeExecutionStatus status,
        List<NodeAttemptSnapshot> attempts,
        Map<String, Object> output,
        String errorMessage,
        boolean failureHandled,
        boolean downstreamContinuationAllowed) {

    public NodeExecutionSnapshot {
        id = requireText(id, "id");
        workflowExecutionId = requireText(workflowExecutionId, "workflowExecutionId");
        nodeId = requireText(nodeId, "nodeId");
        failurePolicy = Objects.requireNonNull(failurePolicy, "failurePolicy");
        status = Objects.requireNonNull(status, "status");
        attempts = attempts == null ? List.of() : List.copyOf(attempts);
        output = ExecutionValueSnapshot.immutableMap(output);
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
