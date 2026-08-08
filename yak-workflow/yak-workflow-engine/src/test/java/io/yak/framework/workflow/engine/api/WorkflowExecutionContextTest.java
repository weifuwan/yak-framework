package io.yak.framework.workflow.engine.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.yak.framework.workflow.engine.definition.EdgeDefinition;
import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.NodeFailurePolicy;
import io.yak.framework.workflow.engine.definition.NodeInputMapping;
import io.yak.framework.workflow.engine.definition.NodeTimeoutPolicy;
import io.yak.framework.workflow.engine.definition.RetryPolicy;
import io.yak.framework.workflow.engine.definition.TriggerRule;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowFailureStrategy;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.graph.WorkflowValidationException;
import io.yak.framework.workflow.engine.spi.NodeDispatch;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowExecutionContextTest {

    private RecordingNodeExecutor executor;
    private DefaultWorkflowEngine engine;

    @BeforeEach
    void setUp() {
        executor = new RecordingNodeExecutor();
        engine = DefaultWorkflowEngine.inMemory(executor);
    }

    @Test
    void resolvesWorkflowInputIntoStartNodeInput() {
        NodeDefinition start = node(
                "start",
                RetryPolicy.none(),
                NodeTimeoutPolicy.none(),
                Map.of(
                        "requestId", "$workflow.request.id",
                        "firstSku", "$workflow.items.0.sku",
                        "wholeRequest", "$workflow"));
        engine.registerDefinition(workflow("workflow-input", List.of(start), List.of()));

        Map<String, Object> input = Map.of(
                "request", Map.of("id", "req-100"),
                "items", List.of(Map.of("sku", "sku-1")));
        engine.start("workflow-input", input);

        NodeDispatch dispatch = executor.last("start");
        assertEquals(Map.of(), dispatch.predecessorOutputs());
        assertEquals("req-100", dispatch.nodeInput().get("requestId"));
        assertEquals("sku-1", dispatch.nodeInput().get("firstSku"));
        assertEquals(input, dispatch.nodeInput().get("wholeRequest"));
        assertEquals(dispatch.nodeInput(), dispatch.context().nodeInput());
        assertEquals(input, dispatch.context().workflowInput());
    }

    @Test
    void propagatesDirectPredecessorOutputAndResolvesNestedPaths() {
        NodeDefinition a = NodeDefinition.task("a");
        NodeDefinition b = node(
                "b",
                RetryPolicy.none(),
                NodeTimeoutPolicy.none(),
                Map.of(
                        "orderId", "a.order.id",
                        "secondItem", "$predecessor.a.items.1",
                        "rawA", "a"));
        engine.registerDefinition(workflow(
                "sequential-output",
                List.of(a, b),
                List.of(new EdgeDefinition("a", "b"))));

        WorkflowExecution execution = engine.start("sequential-output", Map.of());
        Map<String, Object> aOutput = Map.of(
                "order", Map.of("id", 1001),
                "items", List.of("first", "second"));
        complete(execution.id(), "a", aOutput);

        NodeDispatch bDispatch = executor.last("b");
        assertEquals(Map.of("a", aOutput), bDispatch.predecessorOutputs());
        assertEquals(1001, bDispatch.nodeInput().get("orderId"));
        assertEquals("second", bDispatch.nodeInput().get("secondItem"));
        assertEquals(aOutput, bDispatch.nodeInput().get("rawA"));
    }

    @Test
    void joinReceivesAllDirectPredecessorOutputs() {
        NodeDefinition root = NodeDefinition.task("root");
        NodeDefinition left = NodeDefinition.task("left");
        NodeDefinition right = NodeDefinition.task("right");
        NodeDefinition join = node(
                "join",
                RetryPolicy.none(),
                NodeTimeoutPolicy.none(),
                Map.of(
                        "leftValue", "left.value",
                        "rightValue", "right.value",
                        "traceId", "$workflow.traceId"));
        engine.registerDefinition(workflow(
                "join-output",
                List.of(root, left, right, join),
                List.of(
                        new EdgeDefinition("root", "left"),
                        new EdgeDefinition("root", "right"),
                        new EdgeDefinition("left", "join"),
                        new EdgeDefinition("right", "join"))));

        WorkflowExecution execution = engine.start("join-output", Map.of("traceId", "trace-1"));
        complete(execution.id(), "root", Map.of());
        complete(execution.id(), "left", Map.of("value", "L"));
        complete(execution.id(), "right", Map.of("value", "R"));

        NodeDispatch dispatch = executor.last("join");
        assertEquals(Map.of("value", "L"), dispatch.predecessorOutputs().get("left"));
        assertEquals(Map.of("value", "R"), dispatch.predecessorOutputs().get("right"));
        assertEquals(Map.of(
                "leftValue", "L",
                "rightValue", "R",
                "traceId", "trace-1"), dispatch.nodeInput());
    }

    @Test
    void defaultMappingStillExposesRawPredecessorOutputs() {
        NodeDefinition a = NodeDefinition.task("a");
        NodeDefinition b = NodeDefinition.task("b");
        engine.registerDefinition(workflow(
                "raw-output",
                List.of(a, b),
                List.of(new EdgeDefinition("a", "b"))));

        WorkflowExecution execution = engine.start("raw-output", Map.of());
        Map<String, Object> output = Map.of("value", 42);
        complete(execution.id(), "a", output);

        NodeDispatch dispatch = executor.last("b");
        assertEquals(Map.of("a", output), dispatch.predecessorOutputs());
        assertEquals(Map.of(), dispatch.nodeInput());
    }

    @Test
    void retryRebuildsSameInputWithNewAttemptIdentity() {
        NodeDefinition a = NodeDefinition.task("a");
        NodeDefinition b = node(
                "b",
                RetryPolicy.fixed(2, Duration.ZERO),
                NodeTimeoutPolicy.none(),
                Map.of("value", "a.value"));
        engine.registerDefinition(workflow(
                "retry-context",
                List.of(a, b),
                List.of(new EdgeDefinition("a", "b"))));

        WorkflowExecution execution = engine.start("retry-context", Map.of());
        complete(execution.id(), "a", Map.of("value", "stable"));
        NodeDispatch first = executor.last("b");

        engine.failNode(execution.id(), "b", first.attemptId(), "retry me");
        NodeDispatch second = executor.last("b");

        assertNotEquals(first.attemptId(), second.attemptId());
        assertEquals(first.predecessorOutputs(), second.predecessorOutputs());
        assertEquals(first.nodeInput(), second.nodeInput());
        assertEquals(Map.of("value", "stable"), second.nodeInput());
    }

    @Test
    void rerunFromNodeUsesCopiedAncestorOutputAsInput() {
        NodeDefinition a = NodeDefinition.task("a");
        NodeDefinition b = node(
                "b",
                RetryPolicy.none(),
                NodeTimeoutPolicy.none(),
                Map.of("value", "a.value"));
        engine.registerDefinition(workflow(
                "rerun-context",
                List.of(a, b),
                List.of(new EdgeDefinition("a", "b"))));

        WorkflowExecution first = engine.start("rerun-context", Map.of());
        complete(first.id(), "a", Map.of("value", 99));
        NodeDispatch firstB = executor.last("b");
        engine.failNode(first.id(), "b", firstB.attemptId(), "failed");

        WorkflowExecution rerun = engine.rerunFromNode(first.id(), "b");
        NodeDispatch rerunB = executor.lastForExecution(rerun.id(), "b");

        assertEquals(Map.of("a", Map.of("value", 99)), rerunB.predecessorOutputs());
        assertEquals(Map.of("value", 99), rerunB.nodeInput());
    }

    @Test
    void dispatchContextCarriesTimeoutMetadata() {
        NodeTimeoutPolicy timeout = NodeTimeoutPolicy.of(
                Duration.ofSeconds(5), Duration.ofMinutes(2));
        NodeDefinition node = node("a", RetryPolicy.none(), timeout, Map.of());
        engine.registerDefinition(workflow("timeout-context", List.of(node), List.of()));

        engine.start("timeout-context", Map.of());
        NodeDispatch dispatch = executor.last("a");

        assertEquals(dispatch.availableAt().plusSeconds(5), dispatch.dispatchDeadline());
        assertEquals(Duration.ofMinutes(2), dispatch.executionTimeout());
        assertEquals(dispatch.dispatchDeadline(), dispatch.context().dispatchDeadline());
        assertEquals(dispatch.executionTimeout(), dispatch.context().executionTimeout());
    }

    @Test
    void rejectsInputBindingThatBypassesDirectPredecessor() {
        NodeDefinition a = NodeDefinition.task("a");
        NodeDefinition b = NodeDefinition.task("b");
        NodeDefinition c = node(
                "c",
                RetryPolicy.none(),
                NodeTimeoutPolicy.none(),
                Map.of("illegal", "a.value"));
        WorkflowDefinition definition = workflow(
                "invalid-data-dependency",
                List.of(a, b, c),
                List.of(new EdgeDefinition("a", "b"), new EdgeDefinition("b", "c")));

        assertThrows(WorkflowValidationException.class, () -> engine.registerDefinition(definition));
    }

    private NodeDefinition node(
            String id,
            RetryPolicy retryPolicy,
            NodeTimeoutPolicy timeoutPolicy,
            Map<String, String> inputMapping) {
        return new NodeDefinition(
                id,
                id,
                TriggerRule.ALL_SUCCESS,
                retryPolicy,
                NodeFailurePolicy.FAIL_WORKFLOW,
                timeoutPolicy,
                NodeInputMapping.of(inputMapping),
                Map.of());
    }

    private WorkflowDefinition workflow(
            String id,
            List<NodeDefinition> nodes,
            List<EdgeDefinition> edges) {
        return new WorkflowDefinition(
                id,
                id,
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                nodes,
                edges);
    }

    private WorkflowExecution complete(
            String executionId,
            String nodeId,
            Map<String, Object> output) {
        NodeDispatch dispatch = executor.lastForExecution(executionId, nodeId);
        return engine.completeNode(executionId, nodeId, dispatch.attemptId(), output);
    }

    private static final class RecordingNodeExecutor implements NodeExecutor {

        private final List<NodeDispatch> submissions = new ArrayList<>();

        @Override
        public void submit(NodeDispatch dispatch) {
            submissions.add(dispatch);
        }

        private NodeDispatch last(String nodeId) {
            return submissions.stream()
                    .filter(dispatch -> dispatch.nodeId().equals(nodeId))
                    .reduce((first, second) -> second)
                    .orElseThrow();
        }

        private NodeDispatch lastForExecution(String executionId, String nodeId) {
            return submissions.stream()
                    .filter(dispatch -> dispatch.workflowExecutionId().equals(executionId))
                    .filter(dispatch -> dispatch.nodeId().equals(nodeId))
                    .reduce((first, second) -> second)
                    .orElseThrow();
        }
    }
}
