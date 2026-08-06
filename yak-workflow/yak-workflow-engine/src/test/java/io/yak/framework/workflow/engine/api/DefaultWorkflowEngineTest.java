package io.yak.framework.workflow.engine.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.yak.framework.workflow.engine.definition.EdgeDefinition;
import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.NodeFailurePolicy;
import io.yak.framework.workflow.engine.definition.RetryPolicy;
import io.yak.framework.workflow.engine.definition.TriggerRule;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowFailureStrategy;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.spi.NodeCancellation;
import io.yak.framework.workflow.engine.spi.NodeDispatch;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultWorkflowEngineTest {

    private RecordingNodeExecutor executor;
    private DefaultWorkflowEngine engine;

    @BeforeEach
    void setUp() {
        executor = new RecordingNodeExecutor();
        engine = DefaultWorkflowEngine.inMemory(executor);
    }

    @Test
    void continuesIndependentParallelBranchAfterFailure() {
        engine.registerDefinition(parallelDefinition(
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES));
        WorkflowExecution execution = engine.start("parallel", Map.of());
        assertEquals(List.of("a"), executor.submittedNodeIds());

        engine.completeNode(execution.id(), "a", Map.of());
        assertEquals(List.of("a", "b", "c"), executor.submittedNodeIds());

        engine.failNode(execution.id(), "b", "boom");
        WorkflowExecution afterFailure = engine.findExecution(execution.id()).orElseThrow();
        assertEquals(NodeExecutionStatus.UPSTREAM_FAILED, afterFailure.node("d").status());
        assertEquals(NodeExecutionStatus.SUBMITTED, afterFailure.node("c").status());

        engine.completeNode(execution.id(), "c", Map.of());
        assertEquals(List.of("a", "b", "c", "e"), executor.submittedNodeIds());
        WorkflowExecution completed = engine.completeNode(execution.id(), "e", Map.of());

        assertEquals(WorkflowExecutionStatus.FAILED, completed.status());
    }

    @Test
    void ignoreFailureAllowsDownstreamAndFinishesWithWarnings() {
        NodeDefinition ignored = new NodeDefinition(
                "b", "b", TriggerRule.ALL_SUCCESS, RetryPolicy.none(),
                NodeFailurePolicy.IGNORE_FAILURE, Map.of());
        WorkflowDefinition definition = new WorkflowDefinition(
                "ignore",
                "ignore",
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                List.of(NodeDefinition.task("a"), ignored, NodeDefinition.task("c")),
                List.of(new EdgeDefinition("a", "b"), new EdgeDefinition("b", "c")));
        engine.registerDefinition(definition);
        WorkflowExecution execution = engine.start("ignore", Map.of());
        engine.completeNode(execution.id(), "a", Map.of());

        engine.failNode(execution.id(), "b", "ignored");
        assertEquals(List.of("a", "b", "c"), executor.submittedNodeIds());
        WorkflowExecution completed = engine.completeNode(execution.id(), "c", Map.of());

        assertEquals(WorkflowExecutionStatus.SUCCESS_WITH_WARNINGS, completed.status());
    }

    @Test
    void automaticallyRetriesAndPreservesAttemptHistory() {
        NodeDefinition retrying = new NodeDefinition(
                "a", "a", TriggerRule.ALL_SUCCESS,
                RetryPolicy.fixed(2, Duration.ofSeconds(5)),
                NodeFailurePolicy.FAIL_WORKFLOW, Map.of());
        WorkflowDefinition definition = new WorkflowDefinition(
                "retry", "retry", WorkflowFailureStrategy.FAIL_FAST,
                List.of(retrying), List.of());
        engine.registerDefinition(definition);
        WorkflowExecution execution = engine.start("retry", Map.of());

        WorkflowExecution retried = engine.failNode(execution.id(), "a", "first");
        assertEquals(2, retried.node("a").attempts().size());
        assertEquals(2, executor.submissions.size());
        assertTrue(executor.submissions.get(1).availableAt()
                .isAfter(executor.submissions.get(0).availableAt()));

        WorkflowExecution failed = engine.failNode(execution.id(), "a", "second");
        assertEquals(WorkflowExecutionStatus.FAILED, failed.status());
        assertEquals(2, failed.node("a").attempts().size());
    }

    @Test
    void cancelsActiveNodes() {
        engine.registerDefinition(parallelDefinition(
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES));
        WorkflowExecution execution = engine.start("parallel", Map.of());
        engine.completeNode(execution.id(), "a", Map.of());

        WorkflowExecution canceled = engine.cancel(execution.id(), "manual");

        assertEquals(WorkflowExecutionStatus.CANCELED, canceled.status());
        assertEquals(2, executor.cancellations.size());
        assertEquals(NodeExecutionStatus.CANCELED, canceled.node("b").status());
        assertEquals(NodeExecutionStatus.CANCELED, canceled.node("c").status());
    }

    @Test
    void manualRetryReusesExecutionAndCreatesNewAttempt() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "manual-retry", "manual-retry", WorkflowFailureStrategy.FAIL_FAST,
                List.of(NodeDefinition.task("a"), NodeDefinition.task("b")),
                List.of(new EdgeDefinition("a", "b")));
        engine.registerDefinition(definition);
        WorkflowExecution execution = engine.start("manual-retry", Map.of());
        engine.failNode(execution.id(), "a", "failed");

        WorkflowExecution retried = engine.retryFailedNodes(execution.id());
        assertEquals(WorkflowExecutionStatus.RUNNING, retried.status());
        assertEquals(2, retried.node("a").attempts().size());

        engine.completeNode(execution.id(), "a", Map.of());
        WorkflowExecution completed = engine.completeNode(execution.id(), "b", Map.of());
        assertEquals(WorkflowExecutionStatus.SUCCESS, completed.status());
    }

    @Test
    void restartCreatesNewExecution() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "restart", "restart", WorkflowFailureStrategy.FAIL_FAST,
                List.of(NodeDefinition.task("a")), List.of());
        engine.registerDefinition(definition);
        WorkflowExecution first = engine.start("restart", Map.of("key", "value"));
        engine.failNode(first.id(), "a", "failed");

        WorkflowExecution restarted = engine.restart(first.id());

        assertNotEquals(first.id(), restarted.id());
        assertEquals(first.id(), restarted.sourceExecutionId());
        assertEquals(Map.of("key", "value"), restarted.input());
        assertEquals(WorkflowExecutionStatus.RUNNING, restarted.status());
    }

    @Test
    void rerunFromNodeCopiesSuccessfulAncestors() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "rerun", "rerun", WorkflowFailureStrategy.FAIL_FAST,
                List.of(
                        NodeDefinition.task("a"),
                        NodeDefinition.task("b"),
                        NodeDefinition.task("c")),
                List.of(new EdgeDefinition("a", "b"), new EdgeDefinition("b", "c")));
        engine.registerDefinition(definition);
        WorkflowExecution first = engine.start("rerun", Map.of());
        engine.completeNode(first.id(), "a", Map.of("value", 1));
        engine.failNode(first.id(), "b", "failed");

        WorkflowExecution rerun = engine.rerunFromNode(first.id(), "b");

        assertEquals(NodeExecutionStatus.SUCCESS, rerun.node("a").status());
        assertEquals(Map.of("value", 1), rerun.node("a").output());
        assertEquals(NodeExecutionStatus.SUBMITTED, rerun.node("b").status());
        assertEquals(NodeExecutionStatus.WAITING, rerun.node("c").status());
    }

    private WorkflowDefinition parallelDefinition(WorkflowFailureStrategy strategy) {
        return new WorkflowDefinition(
                "parallel",
                "parallel",
                strategy,
                List.of(
                        NodeDefinition.task("a"),
                        NodeDefinition.task("b"),
                        NodeDefinition.task("c"),
                        NodeDefinition.task("d"),
                        NodeDefinition.task("e")),
                List.of(
                        new EdgeDefinition("a", "b"),
                        new EdgeDefinition("a", "c"),
                        new EdgeDefinition("b", "d"),
                        new EdgeDefinition("c", "e")));
    }

    private static final class RecordingNodeExecutor implements NodeExecutor {

        private final List<NodeDispatch> submissions = new ArrayList<>();
        private final List<NodeCancellation> cancellations = new ArrayList<>();

        @Override
        public void submit(NodeDispatch dispatch) {
            submissions.add(dispatch);
        }

        @Override
        public void cancel(NodeCancellation cancellation) {
            cancellations.add(cancellation);
        }

        private List<String> submittedNodeIds() {
            return submissions.stream().map(NodeDispatch::nodeId).toList();
        }
    }
}
