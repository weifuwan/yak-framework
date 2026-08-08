package io.yak.framework.workflow.engine.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.yak.framework.workflow.engine.definition.NodeFailurePolicy;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExecutionValueSnapshotTest {

    @Test
    void workflowInputIsRecursivelySnapshotted() {
        List<Object> items = new ArrayList<>(List.of("before"));
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("items", items);
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("payload", payload);

        WorkflowExecution execution = new WorkflowExecution(
                "execution", "definition", null, input, Map.of(), Instant.EPOCH);

        items.set(0, "after");
        payload.put("newKey", "newValue");

        Map<?, ?> snapshottedPayload = (Map<?, ?>) execution.input().get("payload");
        assertEquals(List.of("before"), snapshottedPayload.get("items"));
        assertThrows(
                UnsupportedOperationException.class,
                () -> ((List<Object>) snapshottedPayload.get("items")).add("mutate"));
    }

    @Test
    void nodeOutputIsRecursivelySnapshotted() {
        NodeExecution node = new NodeExecution(
                "node-execution", "workflow", "node", NodeFailurePolicy.FAIL_WORKFLOW);
        node.transitionTo(NodeExecutionStatus.READY);
        node.beginAttempt("attempt", Instant.EPOCH);

        List<Object> items = new ArrayList<>(List.of("before"));
        Map<String, Object> nested = new LinkedHashMap<>();
        nested.put("items", items);
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("payload", nested);

        node.markSuccess(output, Instant.EPOCH.plusSeconds(1));
        items.set(0, "after");
        nested.put("newKey", "newValue");

        Map<?, ?> snapshottedPayload = (Map<?, ?>) node.output().get("payload");
        assertEquals(List.of("before"), snapshottedPayload.get("items"));
        assertThrows(
                UnsupportedOperationException.class,
                () -> ((Map<Object, Object>) snapshottedPayload).put("x", "y"));
    }

    @Test
    void executorContextExposesReadOnlySnapshots() {
        List<Object> values = new ArrayList<>(List.of(1));
        Map<String, Object> predecessor = new LinkedHashMap<>();
        predecessor.put("values", values);

        NodeExecutionContext context = new NodeExecutionContext(
                "workflow",
                "node-execution",
                "node",
                "attempt",
                1,
                Instant.EPOCH,
                null,
                Duration.ZERO,
                Map.of("request", Map.of("id", "r1")),
                Map.of("upstream", predecessor),
                Map.of("value", values),
                Map.of());

        values.set(0, 2);

        assertEquals(List.of(1), context.nodeInput().get("value"));
        assertEquals(List.of(1), context.predecessorOutputs().get("upstream").get("values"));
        assertThrows(
                UnsupportedOperationException.class,
                () -> context.nodeInput().put("new", "value"));
    }
}
