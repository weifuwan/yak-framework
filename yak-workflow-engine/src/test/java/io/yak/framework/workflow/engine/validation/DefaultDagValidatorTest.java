package io.yak.framework.workflow.engine.validation;

import static io.yak.framework.workflow.engine.model.WorkflowNodeType.END;
import static io.yak.framework.workflow.engine.model.WorkflowNodeType.START;
import static io.yak.framework.workflow.engine.model.WorkflowNodeType.TASK;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.yak.framework.workflow.engine.model.WorkflowDefinition;
import io.yak.framework.workflow.engine.model.WorkflowEdge;
import io.yak.framework.workflow.engine.model.WorkflowNode;
import io.yak.framework.workflow.engine.model.WorkflowNodeType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class DefaultDagValidatorTest {

    private final DagValidator validator = new DefaultDagValidator();

    @Test
    void shouldAcceptLinearDag() {
        WorkflowDefinition definition = workflow(
                List.of(node("start", START), node("extract", TASK), node("end", END)),
                List.of(edge("start", "extract"), edge("extract", "end")));

        DagValidationResult result = validator.validate(definition);

        assertTrue(result.isValid());
    }

    @Test
    void shouldAcceptParallelDag() {
        WorkflowDefinition definition = workflow(
                List.of(
                        node("start", START),
                        node("left", TASK),
                        node("right", TASK),
                        node("merge", TASK),
                        node("end", END)),
                List.of(
                        edge("start", "left"),
                        edge("start", "right"),
                        edge("left", "merge"),
                        edge("right", "merge"),
                        edge("merge", "end")));

        DagValidationResult result = validator.validate(definition);

        assertTrue(result.isValid());
    }

    @Test
    void shouldRejectBlankWorkflowCode() {
        DagValidationResult result = validator.validate(new WorkflowDefinition(
                " ",
                List.of(node("start", START), node("end", END)),
                List.of(edge("start", "end"))));

        assertHasError(result, DagValidationErrorCode.WORKFLOW_CODE_BLANK);
    }

    @Test
    void shouldRejectBlankNodeCodeAndMissingNodeType() {
        WorkflowDefinition definition = workflow(
                List.of(node("start", START), node(" ", TASK), node("task", null), node("end", END)),
                List.of(edge("start", "task"), edge("task", "end")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.NODE_CODE_BLANK);
        assertHasError(result, DagValidationErrorCode.NODE_TYPE_MISSING);
    }

    @Test
    void shouldRejectNullNodeAndNullEdge() {
        WorkflowDefinition definition = workflow(
                listAllowingNull(node("start", START), null, node("end", END)),
                listAllowingNull(edge("start", "end"), null));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.NODE_NULL);
        assertHasError(result, DagValidationErrorCode.EDGE_NULL);
    }

    @Test
    void shouldRejectDuplicateNodeCode() {
        WorkflowDefinition definition = workflow(
                List.of(
                        node("start", START),
                        node("task", TASK),
                        node("task", TASK),
                        node("end", END)),
                List.of(edge("start", "task"), edge("task", "end")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.DUPLICATE_NODE_CODE);
    }

    @Test
    void shouldRequireExactlyOneStartNode() {
        DagValidationResult missing = validator.validate(workflow(
                List.of(node("task", TASK), node("end", END)),
                List.of(edge("task", "end"))));
        DagValidationResult multiple = validator.validate(workflow(
                List.of(node("start1", START), node("start2", START), node("end", END)),
                List.of(edge("start1", "end"), edge("start2", "end"))));

        assertHasError(missing, DagValidationErrorCode.START_NODE_MISSING);
        assertHasError(multiple, DagValidationErrorCode.MULTIPLE_START_NODES);
    }

    @Test
    void shouldRequireAtLeastOneEndNode() {
        WorkflowDefinition definition = workflow(
                List.of(node("start", START), node("task", TASK)),
                List.of(edge("start", "task")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.END_NODE_MISSING);
    }

    @Test
    void shouldRejectBlankAndUnknownEdgeEndpoints() {
        WorkflowDefinition definition = workflow(
                List.of(node("start", START), node("end", END)),
                List.of(
                        edge(" ", "end"),
                        edge("start", " "),
                        edge("missing", "end"),
                        edge("start", "unknown")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.EDGE_SOURCE_BLANK);
        assertHasError(result, DagValidationErrorCode.EDGE_TARGET_BLANK);
        assertHasError(result, DagValidationErrorCode.UNKNOWN_EDGE_SOURCE);
        assertHasError(result, DagValidationErrorCode.UNKNOWN_EDGE_TARGET);
    }

    @Test
    void shouldRejectSelfLoopAndDuplicateEdge() {
        WorkflowDefinition definition = workflow(
                List.of(node("start", START), node("task", TASK), node("end", END)),
                List.of(
                        edge("start", "task"),
                        edge("start", "task"),
                        edge("task", "task"),
                        edge("task", "end")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.DUPLICATE_EDGE);
        assertHasError(result, DagValidationErrorCode.SELF_LOOP);
    }

    @Test
    void shouldRejectIncomingEdgeToStartAndOutgoingEdgeFromEnd() {
        WorkflowDefinition definition = workflow(
                List.of(node("start", START), node("task", TASK), node("end", END)),
                List.of(
                        edge("start", "task"),
                        edge("task", "end"),
                        edge("end", "start")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.START_NODE_HAS_INCOMING_EDGE);
        assertHasError(result, DagValidationErrorCode.END_NODE_HAS_OUTGOING_EDGE);
    }

    @Test
    void shouldRejectCycle() {
        WorkflowDefinition definition = workflow(
                List.of(
                        node("start", START),
                        node("a", TASK),
                        node("b", TASK),
                        node("end", END)),
                List.of(
                        edge("start", "a"),
                        edge("a", "b"),
                        edge("b", "a"),
                        edge("b", "end")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.CYCLE_DETECTED);
    }

    @Test
    void shouldRejectNodeUnreachableFromStart() {
        WorkflowDefinition definition = workflow(
                List.of(
                        node("start", START),
                        node("reachable", TASK),
                        node("orphan", TASK),
                        node("end", END)),
                List.of(
                        edge("start", "reachable"),
                        edge("reachable", "end"),
                        edge("orphan", "end")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.NODE_UNREACHABLE_FROM_START);
    }

    @Test
    void shouldRejectNodeThatCannotReachEnd() {
        WorkflowDefinition definition = workflow(
                List.of(
                        node("start", START),
                        node("completed", TASK),
                        node("deadEnd", TASK),
                        node("end", END)),
                List.of(
                        edge("start", "completed"),
                        edge("completed", "end"),
                        edge("start", "deadEnd")));

        DagValidationResult result = validator.validate(definition);

        assertHasError(result, DagValidationErrorCode.NODE_CANNOT_REACH_END);
    }

    private WorkflowDefinition workflow(List<WorkflowNode> nodes, List<WorkflowEdge> edges) {
        return new WorkflowDefinition("demo-workflow", nodes, edges);
    }

    private WorkflowNode node(String code, WorkflowNodeType type) {
        return new WorkflowNode(code, type);
    }

    private WorkflowEdge edge(String source, String target) {
        return new WorkflowEdge(source, target);
    }

    @SafeVarargs
    private final <T> List<T> listAllowingNull(T... values) {
        return Arrays.asList(values);
    }

    private void assertHasError(DagValidationResult result, DagValidationErrorCode errorCode) {
        assertFalse(result.isValid());
        assertTrue(
                result.hasError(errorCode),
                () -> "Expected error " + errorCode + ", got " + result.errors());
    }
}
