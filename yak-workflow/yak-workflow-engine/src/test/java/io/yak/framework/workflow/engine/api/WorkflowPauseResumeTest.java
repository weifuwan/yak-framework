package io.yak.framework.workflow.engine.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.yak.framework.workflow.engine.definition.EdgeDefinition;
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
import io.yak.framework.workflow.engine.spi.NodeControlResult;
import io.yak.framework.workflow.engine.spi.NodeDispatch;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.spi.NodePauseRequest;
import io.yak.framework.workflow.engine.spi.NodeResumeRequest;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowPauseResumeTest {

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
    void pausesAndResumesRunningAttemptWithoutCreatingNewAttempt() {
        executor.supportPause("a");
        engine.registerDefinition(serialWorkflow("supported", NodeDefinition.task("a"), NodeDefinition.task("b")));

        WorkflowExecution execution = engine.start("supported", Map.of());
        NodeDispatch a = executor.lastSubmission("a");
        engine.acknowledgeNodeStarted(execution.id(), "a", a.attemptId());

        WorkflowExecution pausing = engine.pause(execution.id(), "maintenance");
        assertEquals(WorkflowExecutionStatus.PAUSING, pausing.status());
        assertEquals(NodeExecutionStatus.PAUSING, pausing.node("a").status());
        assertEquals(a.attemptId(), executor.pauseRequests.get(0).attemptId());
        assertEquals(1, pausing.node("a").attempts().size());

        WorkflowExecution paused = engine.acknowledgeNodePaused(
                execution.id(), "a", a.attemptId());
        assertEquals(WorkflowExecutionStatus.PAUSED, paused.status());
        assertEquals(NodeExecutionStatus.PAUSED, paused.node("a").status());
        assertEquals(NodeExecutionStatus.WAITING, paused.node("b").status());

        // A terminal callback while the attempt is actually paused is ignored.
        WorkflowExecution ignored = engine.completeNode(
                execution.id(), "a", a.attemptId(), Map.of("ignored", true));
        assertEquals(WorkflowExecutionStatus.PAUSED, ignored.status());
        assertEquals(NodeExecutionStatus.PAUSED, ignored.node("a").status());

        WorkflowExecution resuming = engine.resume(execution.id());
        assertEquals(WorkflowExecutionStatus.RESUMING, resuming.status());
        assertEquals(NodeExecutionStatus.RESUMING, resuming.node("a").status());
        assertEquals(a.attemptId(), executor.resumeRequests.get(0).attemptId());

        WorkflowExecution resumed = engine.acknowledgeNodeResumed(
                execution.id(), "a", a.attemptId());
        assertEquals(WorkflowExecutionStatus.RUNNING, resumed.status());
        assertEquals(NodeExecutionStatus.RUNNING, resumed.node("a").status());
        assertEquals(a.attemptId(), resumed.node("a").currentAttemptId());
        assertEquals(1, resumed.node("a").attempts().size());

        WorkflowExecution afterA = engine.completeNode(
                execution.id(), "a", a.attemptId(), Map.of("value", 1));
        assertEquals(NodeExecutionStatus.SUBMITTED, afterA.node("b").status());
        assertEquals(List.of("a", "b"), executor.submittedNodeIds());
        assertEquals(1, eventCount(WorkflowEvent.Type.WORKFLOW_PAUSED));
        assertEquals(1, eventCount(WorkflowEvent.Type.WORKFLOW_RESUMED));
    }

    @Test
    void submittedAttemptResumesBackToSubmittedBeforeStartAck() {
        executor.supportPause("a");
        engine.registerDefinition(singleWorkflow("submitted", NodeDefinition.task("a")));

        WorkflowExecution execution = engine.start("submitted", Map.of());
        NodeDispatch dispatch = executor.lastSubmission("a");
        engine.pause(execution.id(), "pause before start");
        engine.acknowledgeNodePaused(execution.id(), "a", dispatch.attemptId());
        engine.resume(execution.id());

        WorkflowExecution resumed = engine.acknowledgeNodeResumed(
                execution.id(), "a", dispatch.attemptId());
        assertEquals(WorkflowExecutionStatus.RUNNING, resumed.status());
        assertEquals(NodeExecutionStatus.SUBMITTED, resumed.node("a").status());

        WorkflowExecution running = engine.acknowledgeNodeStarted(
                execution.id(), "a", dispatch.attemptId());
        assertEquals(NodeExecutionStatus.RUNNING, running.node("a").status());
    }

    @Test
    void unsupportedExecutorFinishesNaturallyButDownstreamWaitsUntilResume() {
        engine.registerDefinition(serialWorkflow("unsupported", NodeDefinition.task("a"), NodeDefinition.task("b")));

        WorkflowExecution execution = engine.start("unsupported", Map.of());
        NodeDispatch a = executor.lastSubmission("a");
        engine.acknowledgeNodeStarted(execution.id(), "a", a.attemptId());

        WorkflowExecution pausing = engine.pause(execution.id(), "maintenance");
        assertEquals(WorkflowExecutionStatus.PAUSING, pausing.status());
        assertEquals(NodeExecutionStatus.RUNNING, pausing.node("a").status());

        WorkflowExecution completedWhilePausing = engine.completeNode(
                execution.id(), "a", a.attemptId(), Map.of("done", true));
        assertEquals(WorkflowExecutionStatus.PAUSED, completedWhilePausing.status());
        assertEquals(NodeExecutionStatus.SUCCESS, completedWhilePausing.node("a").status());
        assertEquals(NodeExecutionStatus.WAITING, completedWhilePausing.node("b").status());
        assertEquals(List.of("a"), executor.submittedNodeIds());

        WorkflowExecution resumed = engine.resume(execution.id());
        assertEquals(WorkflowExecutionStatus.RUNNING, resumed.status());
        assertEquals(NodeExecutionStatus.SUBMITTED, resumed.node("b").status());
        assertEquals(List.of("a", "b"), executor.submittedNodeIds());
    }

    @Test
    void mixedSupportedAndUnsupportedParallelAttemptsSettleBeforePaused() {
        executor.supportPause("b");
        WorkflowDefinition definition = new WorkflowDefinition(
                "mixed",
                "mixed",
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                List.of(
                        NodeDefinition.task("root"),
                        NodeDefinition.task("b"),
                        NodeDefinition.task("c"),
                        NodeDefinition.task("d"),
                        NodeDefinition.task("e")),
                List.of(
                        new EdgeDefinition("root", "b"),
                        new EdgeDefinition("root", "c"),
                        new EdgeDefinition("b", "d"),
                        new EdgeDefinition("c", "e")));
        engine.registerDefinition(definition);

        WorkflowExecution execution = engine.start("mixed", Map.of());
        complete(execution.id(), "root");
        NodeDispatch b = executor.lastSubmission("b");
        NodeDispatch c = executor.lastSubmission("c");

        WorkflowExecution pausing = engine.pause(execution.id(), "pause parallel");
        assertEquals(NodeExecutionStatus.PAUSING, pausing.node("b").status());
        assertEquals(NodeExecutionStatus.SUBMITTED, pausing.node("c").status());

        WorkflowExecution bPaused = engine.acknowledgeNodePaused(
                execution.id(), "b", b.attemptId());
        assertEquals(WorkflowExecutionStatus.PAUSING, bPaused.status());
        assertEquals(NodeExecutionStatus.PAUSED, bPaused.node("b").status());

        WorkflowExecution cFinished = engine.completeNode(
                execution.id(), "c", c.attemptId(), Map.of("c", true));
        assertEquals(WorkflowExecutionStatus.PAUSED, cFinished.status());
        assertEquals(NodeExecutionStatus.WAITING, cFinished.node("e").status());

        engine.resume(execution.id());
        WorkflowExecution resumed = engine.acknowledgeNodeResumed(
                execution.id(), "b", b.attemptId());
        assertEquals(WorkflowExecutionStatus.RUNNING, resumed.status());
        assertEquals(NodeExecutionStatus.SUBMITTED, resumed.node("e").status());
        assertEquals(NodeExecutionStatus.WAITING, resumed.node("d").status());
    }

    @Test
    void nodeExecutionTimeoutExcludesFullyPausedDuration() {
        executor.supportPause("a");
        NodeDefinition node = nodeWithTimeout(
                "a", NodeTimeoutPolicy.of(Duration.ZERO, Duration.ofSeconds(10)));
        engine.registerDefinition(singleWorkflow("node-timeout-pause", node));

        WorkflowExecution execution = engine.start("node-timeout-pause", Map.of());
        NodeDispatch a = executor.lastSubmission("a");
        engine.acknowledgeNodeStarted(execution.id(), "a", a.attemptId());

        clock.advance(Duration.ofSeconds(4));
        engine.pause(execution.id(), "freeze timeout");
        engine.acknowledgeNodePaused(execution.id(), "a", a.attemptId());
        clock.advance(Duration.ofSeconds(100));
        assertEquals(WorkflowExecutionStatus.PAUSED, engine.checkTimeouts(execution.id()).status());

        engine.resume(execution.id());
        engine.acknowledgeNodeResumed(execution.id(), "a", a.attemptId());
        clock.advance(Duration.ofSeconds(5));
        assertEquals(NodeExecutionStatus.RUNNING, engine.checkTimeouts(execution.id()).node("a").status());

        clock.advance(Duration.ofSeconds(1));
        WorkflowExecution timedOut = engine.checkTimeouts(execution.id());
        assertEquals(WorkflowExecutionStatus.FAILED, timedOut.status());
        assertEquals(NodeExecutionStatus.FAILED, timedOut.node("a").status());
    }

    @Test
    void workflowTimeoutExcludesFullyPausedDuration() {
        executor.supportPause("a");
        WorkflowDefinition definition = new WorkflowDefinition(
                "workflow-timeout-pause",
                "workflow-timeout-pause",
                WorkflowFailureStrategy.FAIL_FAST,
                WorkflowTimeoutPolicy.of(Duration.ofSeconds(10)),
                List.of(NodeDefinition.task("a")),
                List.of());
        engine.registerDefinition(definition);

        WorkflowExecution execution = engine.start("workflow-timeout-pause", Map.of());
        NodeDispatch a = executor.lastSubmission("a");
        engine.acknowledgeNodeStarted(execution.id(), "a", a.attemptId());

        clock.advance(Duration.ofSeconds(4));
        engine.pause(execution.id(), "freeze workflow timeout");
        engine.acknowledgeNodePaused(execution.id(), "a", a.attemptId());
        clock.advance(Duration.ofSeconds(100));
        assertEquals(WorkflowExecutionStatus.PAUSED, engine.checkTimeouts(execution.id()).status());

        engine.resume(execution.id());
        WorkflowExecution resumed = engine.acknowledgeNodeResumed(
                execution.id(), "a", a.attemptId());
        assertEquals(Duration.ofSeconds(100), resumed.pausedDuration());

        clock.advance(Duration.ofSeconds(5));
        assertEquals(WorkflowExecutionStatus.RUNNING, engine.checkTimeouts(execution.id()).status());

        clock.advance(Duration.ofSeconds(1));
        assertEquals(WorkflowExecutionStatus.TIMED_OUT, engine.checkTimeouts(execution.id()).status());
    }

    @Test
    void cancelPausedWorkflowCancelsSameFencedAttempt() {
        executor.supportPause("a");
        engine.registerDefinition(singleWorkflow("cancel-paused", NodeDefinition.task("a")));

        WorkflowExecution execution = engine.start("cancel-paused", Map.of());
        NodeDispatch a = executor.lastSubmission("a");
        engine.pause(execution.id(), "pause");
        engine.acknowledgeNodePaused(execution.id(), "a", a.attemptId());

        WorkflowExecution canceled = engine.cancel(execution.id(), "stop");
        assertEquals(WorkflowExecutionStatus.CANCELED, canceled.status());
        assertEquals(NodeExecutionStatus.CANCELED, canceled.node("a").status());
        assertEquals(a.attemptId(), executor.cancellations.get(0).attemptId());
    }

    @Test
    void pauseResumeCallbacksAreFencedAndIdempotent() {
        executor.supportPause("a");
        engine.registerDefinition(singleWorkflow("idempotent-pause", NodeDefinition.task("a")));

        WorkflowExecution execution = engine.start("idempotent-pause", Map.of());
        NodeDispatch a = executor.lastSubmission("a");
        engine.pause(execution.id(), "pause");

        WorkflowExecution stalePause = engine.acknowledgeNodePaused(
                execution.id(), "a", "stale-attempt");
        assertEquals(WorkflowExecutionStatus.PAUSING, stalePause.status());

        engine.acknowledgeNodePaused(execution.id(), "a", a.attemptId());
        WorkflowExecution duplicatePause = engine.acknowledgeNodePaused(
                execution.id(), "a", a.attemptId());
        assertEquals(WorkflowExecutionStatus.PAUSED, duplicatePause.status());
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_PAUSED));

        engine.resume(execution.id());
        WorkflowExecution staleResume = engine.acknowledgeNodeResumed(
                execution.id(), "a", "stale-attempt");
        assertEquals(WorkflowExecutionStatus.RESUMING, staleResume.status());

        engine.acknowledgeNodeResumed(execution.id(), "a", a.attemptId());
        WorkflowExecution duplicateResume = engine.acknowledgeNodeResumed(
                execution.id(), "a", a.attemptId());
        assertEquals(WorkflowExecutionStatus.RUNNING, duplicateResume.status());
        assertEquals(1, eventCount(WorkflowEvent.Type.NODE_RESUMED));
    }

    @Test
    void resumeBeforePauseSettlesIsRejected() {
        engine.registerDefinition(singleWorkflow("resume-early", NodeDefinition.task("a")));
        WorkflowExecution execution = engine.start("resume-early", Map.of());
        engine.pause(execution.id(), "pause");

        assertThrows(IllegalStateException.class, () -> engine.resume(execution.id()));
    }

    private NodeDefinition nodeWithTimeout(String id, NodeTimeoutPolicy timeoutPolicy) {
        return new NodeDefinition(
                id,
                id,
                TriggerRule.ALL_SUCCESS,
                RetryPolicy.none(),
                NodeFailurePolicy.FAIL_WORKFLOW,
                timeoutPolicy,
                Map.of());
    }

    private WorkflowDefinition singleWorkflow(String id, NodeDefinition node) {
        return new WorkflowDefinition(
                id,
                id,
                WorkflowFailureStrategy.FAIL_FAST,
                List.of(node),
                List.of());
    }

    private WorkflowDefinition serialWorkflow(
            String id, NodeDefinition first, NodeDefinition second) {
        return new WorkflowDefinition(
                id,
                id,
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                List.of(first, second),
                List.of(new EdgeDefinition(first.id(), second.id())));
    }

    private WorkflowExecution complete(String executionId, String nodeId) {
        NodeDispatch dispatch = executor.lastSubmissionForExecution(executionId, nodeId);
        return engine.completeNode(executionId, nodeId, dispatch.attemptId(), Map.of());
    }

    private long eventCount(WorkflowEvent.Type type) {
        return events.stream().filter(event -> event.type() == type).count();
    }

    private static final class RecordingNodeExecutor implements NodeExecutor {

        private final Set<String> pauseSupportedNodes = new HashSet<>();
        private final List<NodeDispatch> submissions = new ArrayList<>();
        private final List<NodePauseRequest> pauseRequests = new ArrayList<>();
        private final List<NodeResumeRequest> resumeRequests = new ArrayList<>();
        private final List<NodeCancellation> cancellations = new ArrayList<>();

        private void supportPause(String nodeId) {
            pauseSupportedNodes.add(nodeId);
        }

        @Override
        public void submit(NodeDispatch dispatch) {
            submissions.add(dispatch);
        }

        @Override
        public NodeControlResult pause(NodePauseRequest request) {
            pauseRequests.add(request);
            return pauseSupportedNodes.contains(request.nodeId())
                    ? NodeControlResult.ACCEPTED
                    : NodeControlResult.UNSUPPORTED;
        }

        @Override
        public void resume(NodeResumeRequest request) {
            resumeRequests.add(request);
        }

        @Override
        public void cancel(NodeCancellation cancellation) {
            cancellations.add(cancellation);
        }

        private NodeDispatch lastSubmission(String nodeId) {
            return submissions.stream()
                    .filter(dispatch -> dispatch.nodeId().equals(nodeId))
                    .reduce((first, second) -> second)
                    .orElseThrow();
        }

        private NodeDispatch lastSubmissionForExecution(String executionId, String nodeId) {
            return submissions.stream()
                    .filter(dispatch -> dispatch.workflowExecutionId().equals(executionId))
                    .filter(dispatch -> dispatch.nodeId().equals(nodeId))
                    .reduce((first, second) -> second)
                    .orElseThrow();
        }

        private List<String> submittedNodeIds() {
            return submissions.stream().map(NodeDispatch::nodeId).toList();
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
