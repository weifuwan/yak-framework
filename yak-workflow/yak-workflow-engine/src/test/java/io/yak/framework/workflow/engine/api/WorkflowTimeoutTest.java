package io.yak.framework.workflow.engine.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.NodeFailurePolicy;
import io.yak.framework.workflow.engine.definition.NodeTimeoutPolicy;
import io.yak.framework.workflow.engine.definition.RetryPolicy;
import io.yak.framework.workflow.engine.definition.TriggerRule;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowFailureStrategy;
import io.yak.framework.workflow.engine.definition.WorkflowTimeoutPolicy;
import io.yak.framework.workflow.engine.event.WorkflowEvent;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.spi.NodeCancellation;
import io.yak.framework.workflow.engine.spi.NodeDispatch;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.state.NodeAttemptFailureReason;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowTimeoutTest {

    private RecordingNodeExecutor executor;
    private List<WorkflowEvent> events;
    private MutableClock clock;
    private DefaultWorkflowEngine engine;

    @BeforeEach
    void setUp() {
        executor = new RecordingNodeExecutor();
        events = new ArrayList<>();
        clock = new MutableClock(Instant.parse("2026-08-08T00:00:00Z"));
        engine = DefaultWorkflowEngine.inMemory(executor, events::add, clock);
    }

    @Test
    void dispatchTimeoutFailsCurrentAttemptAndUsesRetryPolicy() {
        NodeDefinition node = node(
                "a",
                RetryPolicy.fixed(2, Duration.ofSeconds(5)),
                NodeTimeoutPolicy.of(Duration.ofSeconds(10), Duration.ZERO));
        engine.registerDefinition(workflow("dispatch-timeout", node));

        WorkflowExecution execution = engine.start("dispatch-timeout", Map.of());
        NodeDispatch first = executor.submissions.get(0);

        clock.advance(Duration.ofSeconds(9));
        WorkflowExecution beforeDeadline = engine.checkTimeouts(execution.id());
        assertEquals(NodeExecutionStatus.SUBMITTED, beforeDeadline.node("a").status());

        clock.advance(Duration.ofSeconds(1));
        WorkflowExecution retried = engine.checkTimeouts(execution.id());
        NodeDispatch second = executor.submissions.get(1);

        assertEquals(WorkflowExecutionStatus.RUNNING, retried.status());
        assertEquals(NodeExecutionStatus.SUBMITTED, retried.node("a").status());
        assertEquals(2, retried.node("a").attempts().size());
        assertEquals(
                NodeAttemptFailureReason.DISPATCH_TIMEOUT,
                retried.node("a").attempts().get(0).failureReason());
        assertNotEquals(first.attemptId(), second.attemptId());
        assertEquals(clock.instant().plusSeconds(5), second.availableAt());
        assertEquals(first.attemptId(), executor.cancellations.get(0).attemptId());
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_DISPATCH_TIMED_OUT));
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_RETRY_SCHEDULED));

        // Retry delay is not counted as dispatch waiting time.
        clock.advance(Duration.ofSeconds(14));
        WorkflowExecution retryStillWaiting = engine.checkTimeouts(execution.id());
        assertEquals(NodeExecutionStatus.SUBMITTED, retryStillWaiting.node("a").status());

        clock.advance(Duration.ofSeconds(1));
        WorkflowExecution failed = engine.checkTimeouts(execution.id());
        assertEquals(WorkflowExecutionStatus.FAILED, failed.status());
        assertEquals(NodeExecutionStatus.FAILED, failed.node("a").status());
        assertEquals(
                NodeAttemptFailureReason.DISPATCH_TIMEOUT,
                failed.node("a").attempts().get(1).failureReason());
        assertEquals(2, eventCount(WorkflowEvent.Type.NODE_DISPATCH_TIMED_OUT));
    }

    @Test
    void executionTimeoutCancelsAttemptAndFencesLateSuccess() {
        NodeDefinition node = node(
                "a",
                RetryPolicy.none(),
                NodeTimeoutPolicy.of(Duration.ofSeconds(30), Duration.ofSeconds(20)));
        engine.registerDefinition(workflow("execution-timeout", node));

        WorkflowExecution execution = engine.start("execution-timeout", Map.of());
        NodeDispatch dispatch = executor.submissions.get(0);
        clock.advance(Duration.ofSeconds(5));
        engine.acknowledgeNodeStarted(execution.id(), "a", dispatch.attemptId());

        clock.advance(Duration.ofSeconds(19));
        assertEquals(
                NodeExecutionStatus.RUNNING,
                engine.checkTimeouts(execution.id()).node("a").status());

        clock.advance(Duration.ofSeconds(1));
        WorkflowExecution timedOut = engine.checkTimeouts(execution.id());
        assertEquals(WorkflowExecutionStatus.FAILED, timedOut.status());
        assertEquals(NodeExecutionStatus.FAILED, timedOut.node("a").status());
        assertEquals(
                NodeAttemptFailureReason.EXECUTION_TIMEOUT,
                timedOut.node("a").attempts().get(0).failureReason());
        assertEquals(dispatch.attemptId(), executor.cancellations.get(0).attemptId());
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_EXECUTION_TIMED_OUT));

        WorkflowExecution lateSuccess = engine.completeNode(
                execution.id(), "a", dispatch.attemptId(), Map.of("late", true));
        assertEquals(WorkflowExecutionStatus.FAILED, lateSuccess.status());
        assertEquals(NodeExecutionStatus.FAILED, lateSuccess.node("a").status());
        assertEquals(Map.of(), lateSuccess.node("a").output());
    }

    @Test
    void workflowTimeoutCancelsActiveAttemptsAndWinsOverNodeTimeouts() {
        NodeDefinition node = node(
                "a",
                RetryPolicy.none(),
                NodeTimeoutPolicy.of(Duration.ofSeconds(30), Duration.ofSeconds(30)));
        WorkflowDefinition definition = new WorkflowDefinition(
                "workflow-timeout",
                "workflow-timeout",
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                WorkflowTimeoutPolicy.of(Duration.ofSeconds(30)),
                List.of(node),
                List.of());
        engine.registerDefinition(definition);

        WorkflowExecution execution = engine.start("workflow-timeout", Map.of());
        NodeDispatch dispatch = executor.submissions.get(0);
        engine.acknowledgeNodeStarted(execution.id(), "a", dispatch.attemptId());

        clock.advance(Duration.ofSeconds(30));
        WorkflowExecution timedOut = engine.checkTimeouts(execution.id());

        assertEquals(WorkflowExecutionStatus.TIMED_OUT, timedOut.status());
        assertEquals(NodeExecutionStatus.CANCELED, timedOut.node("a").status());
        assertEquals(dispatch.attemptId(), executor.cancellations.get(0).attemptId());
        assertEquals(1, eventCount(WorkflowEvent.Type.WORKFLOW_TIMED_OUT));
        assertEquals(0, eventCount(WorkflowEvent.Type.NODE_EXECUTION_TIMED_OUT));

        WorkflowExecution duplicateCheck = engine.checkTimeouts(execution.id());
        assertEquals(WorkflowExecutionStatus.TIMED_OUT, duplicateCheck.status());
        assertEquals(1, eventCount(WorkflowEvent.Type.WORKFLOW_TIMED_OUT));
    }

    @Test
    void workflowTimeoutWindowRestartsWhenTimedOutExecutionIsRetried() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "workflow-retry-timeout",
                "workflow-retry-timeout",
                WorkflowFailureStrategy.FAIL_FAST,
                WorkflowTimeoutPolicy.of(Duration.ofSeconds(30)),
                List.of(NodeDefinition.task("a")),
                List.of());
        engine.registerDefinition(definition);

        WorkflowExecution execution = engine.start("workflow-retry-timeout", Map.of());
        clock.advance(Duration.ofSeconds(30));
        WorkflowExecution timedOut = engine.checkTimeouts(execution.id());
        assertEquals(WorkflowExecutionStatus.TIMED_OUT, timedOut.status());

        WorkflowExecution retried = engine.retryFailedNodes(execution.id());
        assertEquals(WorkflowExecutionStatus.RUNNING, retried.status());
        assertEquals(clock.instant(), retried.runStartedAt());
        assertEquals(2, retried.node("a").attempts().size());

        clock.advance(Duration.ofSeconds(29));
        assertEquals(WorkflowExecutionStatus.RUNNING, engine.checkTimeouts(execution.id()).status());

        clock.advance(Duration.ofSeconds(1));
        assertEquals(WorkflowExecutionStatus.TIMED_OUT, engine.checkTimeouts(execution.id()).status());
        assertEquals(2, eventCount(WorkflowEvent.Type.WORKFLOW_TIMED_OUT));
    }

    private NodeDefinition node(
            String id,
            RetryPolicy retryPolicy,
            NodeTimeoutPolicy timeoutPolicy) {
        return new NodeDefinition(
                id,
                id,
                TriggerRule.ALL_SUCCESS,
                retryPolicy,
                NodeFailurePolicy.FAIL_WORKFLOW,
                timeoutPolicy,
                Map.of());
    }

    private WorkflowDefinition workflow(String id, NodeDefinition node) {
        return new WorkflowDefinition(
                id,
                id,
                WorkflowFailureStrategy.FAIL_FAST,
                List.of(node),
                List.of());
    }

    private long eventCount(WorkflowEvent.Type type) {
        return events.stream().filter(event -> event.type() == type).count();
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
    }

    private static final class MutableClock extends Clock {

        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void advance(Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            if (ZoneOffset.UTC.equals(zone)) {
                return this;
            }
            return Clock.fixed(instant, zone);
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
