package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.definition.NodeFailurePolicy;
import io.yak.framework.workflow.engine.state.NodeAttemptFailureReason;
import io.yak.framework.workflow.engine.state.NodeAttemptStatus;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.NodeStateMachine;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class NodeExecution {

    private final String id;
    private final String workflowExecutionId;
    private final String nodeId;
    private final NodeFailurePolicy failurePolicy;
    private final List<NodeAttempt> attempts;
    private NodeExecutionStatus status;
    private Map<String, Object> output;
    private String errorMessage;
    private boolean failureHandled;
    private boolean downstreamContinuationAllowed;

    public NodeExecution(
            String id,
            String workflowExecutionId,
            String nodeId,
            NodeFailurePolicy failurePolicy) {
        this.id = Objects.requireNonNull(id, "id");
        this.workflowExecutionId = Objects.requireNonNull(workflowExecutionId, "workflowExecutionId");
        this.nodeId = Objects.requireNonNull(nodeId, "nodeId");
        this.failurePolicy = Objects.requireNonNull(failurePolicy, "failurePolicy");
        this.status = NodeExecutionStatus.WAITING;
        this.attempts = new ArrayList<>();
        this.output = Map.of();
    }

    private NodeExecution(NodeExecution source) {
        this.id = source.id;
        this.workflowExecutionId = source.workflowExecutionId;
        this.nodeId = source.nodeId;
        this.failurePolicy = source.failurePolicy;
        this.status = source.status;
        this.attempts = new ArrayList<>(source.attempts.stream().map(NodeAttempt::copy).toList());
        this.output = source.output;
        this.errorMessage = source.errorMessage;
        this.failureHandled = source.failureHandled;
        this.downstreamContinuationAllowed = source.downstreamContinuationAllowed;
    }

    public String id() {
        return id;
    }

    public String workflowExecutionId() {
        return workflowExecutionId;
    }

    public String nodeId() {
        return nodeId;
    }

    public NodeFailurePolicy failurePolicy() {
        return failurePolicy;
    }

    public NodeExecutionStatus status() {
        return status;
    }

    public List<NodeAttempt> attempts() {
        return Collections.unmodifiableList(attempts);
    }

    public Map<String, Object> output() {
        return output;
    }

    public String errorMessage() {
        return errorMessage;
    }

    public boolean failureHandled() {
        return failureHandled;
    }

    public boolean downstreamContinuationAllowed() {
        return downstreamContinuationAllowed;
    }

    public String currentAttemptId() {
        return currentAttempt().id();
    }

    public NodeAttemptStatus currentAttemptStatus() {
        return currentAttempt().status();
    }

    public Instant currentAttemptAvailableAt() {
        return currentAttempt().availableAt();
    }

    public Instant currentAttemptStartedAt() {
        return currentAttempt().startedAt();
    }

    public Instant currentAttemptDispatchDeadline(Duration dispatchTimeout) {
        return attempts.isEmpty() ? null : currentAttempt().dispatchDeadline(dispatchTimeout);
    }

    public Instant currentAttemptExecutionDeadline(Duration executionTimeout) {
        return attempts.isEmpty() ? null : currentAttempt().executionDeadline(executionTimeout);
    }

    public boolean isCurrentAttempt(String attemptId) {
        return !attempts.isEmpty() && Objects.equals(currentAttempt().id(), attemptId);
    }

    public boolean isEffectiveSuccess() {
        return status == NodeExecutionStatus.SUCCESS
                || (status == NodeExecutionStatus.FAILED
                        && (failurePolicy == NodeFailurePolicy.IGNORE_FAILURE
                                || downstreamContinuationAllowed));
    }

    public boolean isFailureLike() {
        return status == NodeExecutionStatus.UPSTREAM_FAILED
                || status == NodeExecutionStatus.CANCELED
                || (status == NodeExecutionStatus.FAILED
                        && failurePolicy != NodeFailurePolicy.IGNORE_FAILURE
                        && !downstreamContinuationAllowed);
    }

    public void transitionTo(NodeExecutionStatus target) {
        NodeStateMachine.requireTransition(status, target);
        status = target;
    }

    public NodeAttempt beginAttempt(String attemptId, Instant availableAt) {
        if (status != NodeExecutionStatus.READY) {
            throw new IllegalStateException("Node must be READY before submission");
        }
        NodeAttempt attempt = new NodeAttempt(attemptId, attempts.size() + 1, availableAt);
        attempts.add(attempt);
        transitionTo(NodeExecutionStatus.SUBMITTED);
        errorMessage = null;
        failureHandled = false;
        downstreamContinuationAllowed = false;
        return attempt;
    }

    public void markRunning(Instant now) {
        currentAttempt().markRunning(now);
        transitionTo(NodeExecutionStatus.RUNNING);
    }

    public void markPausing() {
        currentAttempt().markPausing();
        transitionTo(NodeExecutionStatus.PAUSING);
    }

    public void markPaused(Instant now) {
        currentAttempt().markPaused(now);
        transitionTo(NodeExecutionStatus.PAUSED);
    }

    public void markResuming() {
        currentAttempt().markResuming();
        transitionTo(NodeExecutionStatus.RESUMING);
    }

    public void markResumed(Instant now) {
        NodeAttemptStatus resumedStatus = currentAttempt().markResumed(now);
        transitionTo(resumedStatus == NodeAttemptStatus.SUBMITTED
                ? NodeExecutionStatus.SUBMITTED
                : NodeExecutionStatus.RUNNING);
    }

    public void markSuccess(Map<String, Object> output, Instant now) {
        currentAttempt().markSuccess(now);
        this.output = ExecutionValueSnapshot.immutableMap(output);
        transitionTo(NodeExecutionStatus.SUCCESS);
    }

    public void markFailure(String errorMessage, Instant now) {
        markFailure(NodeAttemptFailureReason.EXECUTOR_FAILURE, errorMessage, now);
    }

    public void markFailure(
            NodeAttemptFailureReason failureReason,
            String errorMessage,
            Instant now) {
        currentAttempt().markFailure(failureReason, errorMessage, now);
        this.errorMessage = errorMessage;
        this.downstreamContinuationAllowed = false;
        transitionTo(NodeExecutionStatus.FAILED);
    }

    public void markCanceled(Instant now) {
        if (status == NodeExecutionStatus.SUBMITTED
                || status == NodeExecutionStatus.RUNNING
                || status == NodeExecutionStatus.PAUSING
                || status == NodeExecutionStatus.PAUSED
                || status == NodeExecutionStatus.RESUMING) {
            currentAttempt().markCanceled(now);
        }
        transitionTo(NodeExecutionStatus.CANCELED);
    }

    public void markFailureHandled() {
        this.failureHandled = true;
    }

    public void allowDownstreamContinuation() {
        if (status != NodeExecutionStatus.FAILED) {
            throw new IllegalStateException("Only a failed node can continue downstream");
        }
        this.failureHandled = true;
        this.downstreamContinuationAllowed = true;
    }

    public void resetForManualRetry() {
        transitionTo(NodeExecutionStatus.WAITING);
        errorMessage = null;
        failureHandled = false;
        downstreamContinuationAllowed = false;
    }

    public void resetSyntheticState() {
        if (status != NodeExecutionStatus.UPSTREAM_FAILED
                && status != NodeExecutionStatus.SKIPPED
                && status != NodeExecutionStatus.CANCELED) {
            throw new IllegalStateException("Node is not in a resettable synthetic state: " + status);
        }
        transitionTo(NodeExecutionStatus.WAITING);
        errorMessage = null;
        failureHandled = false;
        downstreamContinuationAllowed = false;
    }

    public void markCopiedSuccess(Map<String, Object> copiedOutput) {
        this.output = ExecutionValueSnapshot.immutableMap(copiedOutput);
        transitionTo(NodeExecutionStatus.SUCCESS);
    }

    public NodeExecution copy() {
        return new NodeExecution(this);
    }

    private NodeAttempt currentAttempt() {
        if (attempts.isEmpty()) {
            throw new IllegalStateException("Node has no execution attempt");
        }
        return attempts.get(attempts.size() - 1);
    }
}
