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
import io.yak.framework.workflow.engine.event.WorkflowEvent;
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
    private List<WorkflowEvent> events;
    private DefaultWorkflowEngine engine;

    @BeforeEach
    void setUp() {
        executor = new RecordingNodeExecutor();
        events = new ArrayList<>();
        engine = DefaultWorkflowEngine.inMemory(executor, events::add);
    }

    @Test
    void dispatchCarriesAttemptIdentity() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "attempt-dispatch",
                "attempt-dispatch",
                WorkflowFailureStrategy.FAIL_FAST,
                List.of(NodeDefinition.task("a")),
                List.of());
        engine.registerDefinition(definition);

        WorkflowExecution execution = engine.start("attempt-dispatch", Map.of());
        NodeDispatch dispatch = executor.submissions.get(0);

        assertEquals(execution.id(), dispatch.workflowExecutionId());
        assertEquals("a", dispatch.nodeId());
        assertEquals(execution.node("a").currentAttemptId(), dispatch.attemptId());
        assertEquals(1, dispatch.attemptNumber());
        assertEquals(dispatch.attemptId(), firstEvent(WorkflowEvent.Type.NODE_SUBMITTED).attemptId());
    }

    @Test
    void duplicateCallbacksAreIdempotentAndFirstTerminalResultWins() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "idempotent-callback",
                "idempotent-callback",
                WorkflowFailureStrategy.FAIL_FAST,
                List.of(NodeDefinition.task("a")),
                List.of());
        engine.registerDefinition(definition);
        WorkflowExecution execution = engine.start("idempotent-callback", Map.of());
        String attemptId = currentAttemptId(execution.id(), "a");

        engine.acknowledgeNodeStarted(execution.id(), "a", attemptId);
        engine.acknowledgeNodeStarted(execution.id(), "a", attemptId);
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_STARTED));

        WorkflowExecution completed = engine.completeNode(
                execution.id(), "a", attemptId, Map.of("value", 1));
        WorkflowExecution duplicateSuccess = engine.completeNode(
                execution.id(), "a", attemptId, Map.of("value", 2));
        WorkflowExecution lateFailure = engine.failNode(
                execution.id(), "a", attemptId, "late failure");

        assertEquals(WorkflowExecutionStatus.SUCCESS, completed.status());
        assertEquals(WorkflowExecutionStatus.SUCCESS, duplicateSuccess.status());
        assertEquals(WorkflowExecutionStatus.SUCCESS, lateFailure.status());
        assertEquals(Map.of("value", 1), lateFailure.node("a").output());
        assertEquals(1, lateFailure.node("a").attempts().size());
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_SUCCEEDED));
        assertEquals(0, eventCount(WorkflowEvent.Type.NODE_FAILED));
        assertEquals(1, eventCount(WorkflowEvent.Type.WORKFLOW_COMPLETED));
    }

    @Test
    void staleAttemptCallbacksCannotCorruptNewerRetryAttempt() {
        NodeDefinition retrying = new NodeDefinition(
                "a",
                "a",
                TriggerRule.ALL_SUCCESS,
                RetryPolicy.fixed(2, Duration.ZERO),
                NodeFailurePolicy.FAIL_WORKFLOW,
                Map.of());
        WorkflowDefinition definition = new WorkflowDefinition(
                "stale-callback",
                "stale-callback",
                WorkflowFailureStrategy.FAIL_FAST,
                List.of(retrying),
                List.of());
        engine.registerDefinition(definition);
        WorkflowExecution execution = engine.start("stale-callback", Map.of());
        NodeDispatch firstDispatch = executor.submissions.get(0);

        engine.failNode(
                execution.id(), "a", firstDispatch.attemptId(), "first attempt failed");
        NodeDispatch secondDispatch = executor.submissions.get(1);
        assertNotEquals(firstDispatch.attemptId(), secondDispatch.attemptId());

        WorkflowExecution staleSuccess = engine.completeNode(
                execution.id(),
                "a",
                firstDispatch.attemptId(),
                Map.of("stale", true));
        WorkflowExecution staleFailure = engine.failNode(
                execution.id(),
                "a",
                firstDispatch.attemptId(),
                "stale failure");

        assertEquals(NodeExecutionStatus.SUBMITTED, staleSuccess.node("a").status());
        assertEquals(NodeExecutionStatus.SUBMITTED, staleFailure.node("a").status());
        assertEquals(secondDispatch.attemptId(), staleFailure.node("a").currentAttemptId());
        assertEquals(Map.of(), staleFailure.node("a").output());
        assertEquals(2, staleFailure.node("a").attempts().size());
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_FAILED));
        assertEquals(0, eventCount(WorkflowEvent.Type.NODE_SUCCEEDED));

        engine.acknowledgeNodeStarted(execution.id(), "a", secondDispatch.attemptId());
        WorkflowExecution completed = engine.completeNode(
                execution.id(), "a", secondDispatch.attemptId(), Map.of("fresh", true));

        assertEquals(WorkflowExecutionStatus.SUCCESS, completed.status());
        assertEquals(Map.of("fresh", true), completed.node("a").output());
    }

    @Test
    void duplicateFailureCallbackIsIdempotentAfterWorkflowFinished() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "duplicate-failure",
                "duplicate-failure",
                WorkflowFailureStrategy.FAIL_FAST,
                List.of(NodeDefinition.task("a")),
                List.of());
        engine.registerDefinition(definition);
        WorkflowExecution execution = engine.start("duplicate-failure", Map.of());
        String attemptId = currentAttemptId(execution.id(), "a");

        WorkflowExecution failed = engine.failNode(
                execution.id(), "a", attemptId, "boom");
        WorkflowExecution duplicate = engine.failNode(
                execution.id(), "a", attemptId, "boom again");

        assertEquals(WorkflowExecutionStatus.FAILED, failed.status());
        assertEquals(WorkflowExecutionStatus.FAILED, duplicate.status());
        assertEquals("boom", duplicate.node("a").errorMessage());
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_FAILED));
        assertEquals(1, eventCount(WorkflowEvent.Type.WORKFLOW_COMPLETED));
    }

    @Test
    void continuesIndependentParallelBranchAfterFailure() {
        engine.registerDefinition(parallelDefinition(
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES));
        WorkflowExecution execution = engine.start("parallel", Map.of());
        assertEquals(List.of("a"), executor.submittedNodeIds());

        complete(execution.id(), "a");
        assertEquals(List.of("a", "b", "c"), executor.submittedNodeIds());

        fail(execution.id(), "b", "boom");
        WorkflowExecution afterFailure = engine.findExecution(execution.id()).orElseThrow();
        assertEquals(NodeExecutionStatus.UPSTREAM_FAILED, afterFailure.node("d").status());
        assertEquals(NodeExecutionStatus.SUBMITTED, afterFailure.node("c").status());

        complete(execution.id(), "c");
        assertEquals(List.of("a", "b", "c", "e"), executor.submittedNodeIds());
        WorkflowExecution completed = complete(execution.id(), "e");

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
        complete(execution.id(), "a");

        fail(execution.id(), "b", "ignored");
        assertEquals(List.of("a", "b", "c"), executor.submittedNodeIds());
        WorkflowExecution completed = complete(execution.id(), "c");

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

        WorkflowExecution retried = fail(execution.id(), "a", "first");
        assertEquals(2, retried.node("a").attempts().size());
        assertEquals(2, executor.submissions.size());
        assertTrue(executor.submissions.get(1).availableAt()
                .isAfter(executor.submissions.get(0).availableAt()));
        assertNotEquals(
                executor.submissions.get(0).attemptId(),
                executor.submissions.get(1).attemptId());

        WorkflowExecution failed = fail(execution.id(), "a", "second");
        assertEquals(WorkflowExecutionStatus.FAILED, failed.status());
        assertEquals(2, failed.node("a").attempts().size());
    }

    @Test
    void cancelsActiveNodes() {
        engine.registerDefinition(parallelDefinition(
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES));
        WorkflowExecution execution = engine.start("parallel", Map.of());
        complete(execution.id(), "a");

        WorkflowExecution canceled = engine.cancel(execution.id(), "manual");

        assertEquals(WorkflowExecutionStatus.CANCELED, canceled.status());
        assertEquals(2, executor.cancellations.size());
        assertEquals(NodeExecutionStatus.CANCELED, canceled.node("b").status());
        assertEquals(NodeExecutionStatus.CANCELED, canceled.node("c").status());
        assertEquals(
                canceled.node("b").currentAttemptId(),
                executor.cancellationFor("b").attemptId());
    }

    @Test
    void manualRetryReusesExecutionAndCreatesNewAttempt() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "manual-retry", "manual-retry", WorkflowFailureStrategy.FAIL_FAST,
                List.of(NodeDefinition.task("a"), NodeDefinition.task("b")),
                List.of(new EdgeDefinition("a", "b")));
        engine.registerDefinition(definition);
        WorkflowExecution execution = engine.start("manual-retry", Map.of());
        fail(execution.id(), "a", "failed");

        WorkflowExecution retried = engine.retryFailedNodes(execution.id());
        assertEquals(WorkflowExecutionStatus.RUNNING, retried.status());
        assertEquals(2, retried.node("a").attempts().size());

        complete(execution.id(), "a");
        WorkflowExecution completed = complete(execution.id(), "b");
        assertEquals(WorkflowExecutionStatus.SUCCESS, completed.status());
    }

    @Test
    void retrySelectedFailedNodeDoesNotRerunIndependentBranch() {
        engine.registerDefinition(parallelDefinition(
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES));
        WorkflowExecution execution = engine.start("parallel", Map.of());
        complete(execution.id(), "a");
        fail(execution.id(), "b", "boom");
        complete(execution.id(), "c");
        WorkflowExecution failed = complete(execution.id(), "e");

        assertEquals(WorkflowExecutionStatus.FAILED, failed.status());
        assertEquals(NodeExecutionStatus.UPSTREAM_FAILED, failed.node("d").status());
        assertEquals(1, failed.node("b").attempts().size());
        assertEquals(1, failed.node("e").attempts().size());

        WorkflowExecution retried = engine.retryFailedNode(execution.id(), "b");

        assertEquals(WorkflowExecutionStatus.RUNNING, retried.status());
        assertEquals(NodeExecutionStatus.SUBMITTED, retried.node("b").status());
        assertEquals(NodeExecutionStatus.WAITING, retried.node("d").status());
        assertEquals(2, retried.node("b").attempts().size());
        assertEquals(1, retried.node("e").attempts().size());
        assertEquals(List.of("a", "b", "c", "e", "b"), executor.submittedNodeIds());

        WorkflowExecution afterRetrySuccess = complete(execution.id(), "b");
        assertEquals(NodeExecutionStatus.SUBMITTED, afterRetrySuccess.node("d").status());
        assertEquals(List.of("a", "b", "c", "e", "b", "d"), executor.submittedNodeIds());

        WorkflowExecution completed = complete(execution.id(), "d");
        assertEquals(WorkflowExecutionStatus.SUCCESS, completed.status());
    }

    @Test
    void manualContinueReleasesBlockedDescendantsWithoutRetryingFailedNode() {
        engine.registerDefinition(parallelDefinition(
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES));
        WorkflowExecution execution = engine.start("parallel", Map.of());
        complete(execution.id(), "a");
        fail(execution.id(), "b", "boom");
        complete(execution.id(), "c");
        WorkflowExecution failed = complete(execution.id(), "e");

        assertEquals(WorkflowExecutionStatus.FAILED, failed.status());
        assertEquals(NodeExecutionStatus.UPSTREAM_FAILED, failed.node("d").status());
        assertEquals(1, failed.node("b").attempts().size());

        WorkflowExecution continued = engine.continueAfterFailure(execution.id(), "b");

        assertEquals(WorkflowExecutionStatus.RUNNING, continued.status());
        assertEquals(NodeExecutionStatus.FAILED, continued.node("b").status());
        assertTrue(continued.node("b").downstreamContinuationAllowed());
        assertEquals(1, continued.node("b").attempts().size());
        assertEquals(NodeExecutionStatus.SUBMITTED, continued.node("d").status());
        assertEquals(List.of("a", "b", "c", "e", "d"), executor.submittedNodeIds());

        WorkflowExecution completed = complete(execution.id(), "d");
        assertEquals(WorkflowExecutionStatus.SUCCESS_WITH_WARNINGS, completed.status());
        assertEquals(NodeExecutionStatus.FAILED, completed.node("b").status());
        assertEquals(NodeExecutionStatus.SUCCESS, completed.node("d").status());
    }

    @Test
    void manualContinueReleasesJoinAfterOneFailedPredecessorWasBlocked() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "join-continue",
                "join-continue",
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                List.of(
                        NodeDefinition.task("a"),
                        NodeDefinition.task("b"),
                        NodeDefinition.task("c"),
                        NodeDefinition.task("join")),
                List.of(
                        new EdgeDefinition("a", "b"),
                        new EdgeDefinition("a", "c"),
                        new EdgeDefinition("b", "join"),
                        new EdgeDefinition("c", "join")));
        engine.registerDefinition(definition);
        WorkflowExecution execution = engine.start("join-continue", Map.of());
        complete(execution.id(), "a");
        fail(execution.id(), "b", "boom");
        WorkflowExecution failed = complete(execution.id(), "c");

        assertEquals(WorkflowExecutionStatus.FAILED, failed.status());
        assertEquals(NodeExecutionStatus.UPSTREAM_FAILED, failed.node("join").status());

        WorkflowExecution continued = engine.continueAfterFailure(execution.id(), "b");
        assertEquals(NodeExecutionStatus.SUBMITTED, continued.node("join").status());

        WorkflowExecution completed = complete(execution.id(), "join");
        assertEquals(WorkflowExecutionStatus.SUCCESS_WITH_WARNINGS, completed.status());
    }

    @Test
    void restartCreatesNewExecution() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "restart", "restart", WorkflowFailureStrategy.FAIL_FAST,
                List.of(NodeDefinition.task("a")), List.of());
        engine.registerDefinition(definition);
        WorkflowExecution first = engine.start("restart", Map.of("key", "value"));
        fail(first.id(), "a", "failed");

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
        complete(first.id(), "a", Map.of("value", 1));
        fail(first.id(), "b", "failed");

        WorkflowExecution rerun = engine.rerunFromNode(first.id(), "b");

        assertEquals(NodeExecutionStatus.SUCCESS, rerun.node("a").status());
        assertEquals(Map.of("value", 1), rerun.node("a").output());
        assertEquals(NodeExecutionStatus.SUBMITTED, rerun.node("b").status());
        assertEquals(NodeExecutionStatus.WAITING, rerun.node("c").status());
    }

    private WorkflowExecution complete(String executionId, String nodeId) {
        return complete(executionId, nodeId, Map.of());
    }

    private WorkflowExecution complete(
            String executionId,
            String nodeId,
            Map<String, Object> output) {
        return engine.completeNode(
                executionId,
                nodeId,
                currentAttemptId(executionId, nodeId),
                output);
    }

    private WorkflowExecution fail(String executionId, String nodeId, String errorMessage) {
        return engine.failNode(
                executionId,
                nodeId,
                currentAttemptId(executionId, nodeId),
                errorMessage);
    }

    private String currentAttemptId(String executionId, String nodeId) {
        return engine.findExecution(executionId)
                .orElseThrow()
                .node(nodeId)
                .currentAttemptId();
    }

    private long eventCount(WorkflowEvent.Type type) {
        return events.stream().filter(event -> event.type() == type).count();
    }

    private WorkflowEvent firstEvent(WorkflowEvent.Type type) {
        return events.stream()
                .filter(event -> event.type() == type)
                .findFirst()
                .orElseThrow();
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

        private NodeCancellation cancellationFor(String nodeId) {
            return cancellations.stream()
                    .filter(cancellation -> cancellation.nodeId().equals(nodeId))
                    .findFirst()
                    .orElseThrow();
        }
    }
}
