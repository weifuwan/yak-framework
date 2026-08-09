package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowStateMachine;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class WorkflowExecution {

    private final String id;
    private final String definitionId;
    private final String sourceExecutionId;
    private final Map<String, Object> input;
    private final Map<String, NodeExecution> nodes;
    private final Instant createdAt;
    private WorkflowExecutionStatus status;
    private boolean schedulingStopped;
    private Instant runStartedAt;
    private Instant pausedAt;
    private Duration pausedDuration = Duration.ZERO;
    private Instant updatedAt;
    private Instant endedAt;

    public WorkflowExecution(
            String id,
            String definitionId,
            String sourceExecutionId,
            Map<String, Object> input,
            Map<String, NodeExecution> nodes,
            Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.definitionId = Objects.requireNonNull(definitionId, "definitionId");
        this.sourceExecutionId = sourceExecutionId;
        this.input = ExecutionValueSnapshot.immutableMap(input);
        this.nodes = new LinkedHashMap<>(Objects.requireNonNull(nodes, "nodes"));
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = createdAt;
        this.status = WorkflowExecutionStatus.CREATED;
    }

    private WorkflowExecution(WorkflowExecution source) {
        this.id = source.id;
        this.definitionId = source.definitionId;
        this.sourceExecutionId = source.sourceExecutionId;
        this.input = source.input;
        this.nodes = new LinkedHashMap<>();
        source.nodes.forEach((nodeId, execution) -> this.nodes.put(nodeId, execution.copy()));
        this.createdAt = source.createdAt;
        this.status = source.status;
        this.schedulingStopped = source.schedulingStopped;
        this.runStartedAt = source.runStartedAt;
        this.pausedAt = source.pausedAt;
        this.pausedDuration = source.pausedDuration;
        this.updatedAt = source.updatedAt;
        this.endedAt = source.endedAt;
    }

    public static WorkflowExecution restore(WorkflowExecutionSnapshot snapshot) {
        Objects.requireNonNull(snapshot, "snapshot");
        Map<String, NodeExecution> nodes = new LinkedHashMap<>();
        for (NodeExecutionSnapshot nodeSnapshot : snapshot.nodes()) {
            if (!snapshot.id().equals(nodeSnapshot.workflowExecutionId())) {
                throw new IllegalArgumentException(
                        "Node execution belongs to a different workflow execution: "
                                + nodeSnapshot.nodeId());
            }
            NodeExecution node = NodeExecution.restore(nodeSnapshot);
            NodeExecution previous = nodes.putIfAbsent(node.nodeId(), node);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate node execution in snapshot: " + node.nodeId());
            }
        }
        WorkflowExecution execution = new WorkflowExecution(
                snapshot.id(),
                snapshot.definitionId(),
                snapshot.sourceExecutionId(),
                snapshot.input(),
                nodes,
                snapshot.createdAt());
        execution.status = snapshot.status();
        execution.schedulingStopped = snapshot.schedulingStopped();
        execution.runStartedAt = snapshot.runStartedAt();
        execution.pausedAt = snapshot.pausedAt();
        execution.pausedDuration = snapshot.pausedDuration();
        execution.updatedAt = snapshot.updatedAt();
        execution.endedAt = snapshot.endedAt();
        return execution;
    }

    public String id() {
        return id;
    }

    public String definitionId() {
        return definitionId;
    }

    public String sourceExecutionId() {
        return sourceExecutionId;
    }

    public Map<String, Object> input() {
        return input;
    }

    public Map<String, NodeExecution> nodes() {
        return Collections.unmodifiableMap(nodes);
    }

    public NodeExecution node(String nodeId) {
        NodeExecution node = nodes.get(nodeId);
        if (node == null) {
            throw new IllegalArgumentException("Unknown node execution: " + nodeId);
        }
        return node;
    }

    public WorkflowExecutionStatus status() {
        return status;
    }

    public boolean schedulingStopped() {
        return schedulingStopped;
    }

    public Instant createdAt() {
        return createdAt;
    }

    /** Start of the current active run segment, used as the workflow timeout anchor. */
    public Instant runStartedAt() {
        return runStartedAt;
    }

    public Instant pausedAt() {
        return pausedAt;
    }

    public Duration pausedDuration() {
        return pausedDuration;
    }

    public Instant workflowDeadline(Duration timeout) {
        if (runStartedAt == null || timeout == null || timeout.isZero()) {
            return null;
        }
        return runStartedAt.plus(timeout).plus(pausedDuration);
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant endedAt() {
        return endedAt;
    }

    public void transitionTo(WorkflowExecutionStatus target, Instant now) {
        WorkflowExecutionStatus previous = status;
        WorkflowStateMachine.requireTransition(previous, target);
        status = target;
        updatedAt = now;

        if (target == WorkflowExecutionStatus.RUNNING
                && previous != WorkflowExecutionStatus.RUNNING) {
            if (previous == WorkflowExecutionStatus.RESUMING) {
                closePausedInterval(now);
            } else {
                runStartedAt = now;
                pausedAt = null;
                pausedDuration = Duration.ZERO;
            }
        }
        if (target == WorkflowExecutionStatus.PAUSED && pausedAt == null) {
            pausedAt = now;
        }
        if (target.isTerminal()) {
            endedAt = now;
        } else {
            endedAt = null;
        }
    }

    public void touch(Instant now) {
        updatedAt = now;
    }

    public void stopScheduling() {
        schedulingStopped = true;
    }

    public void resumeScheduling() {
        schedulingStopped = false;
    }

    public WorkflowExecutionSnapshot snapshot() {
        return new WorkflowExecutionSnapshot(
                id,
                definitionId,
                sourceExecutionId,
                input,
                nodes.values().stream().map(NodeExecution::snapshot).toList(),
                createdAt,
                status,
                schedulingStopped,
                runStartedAt,
                pausedAt,
                pausedDuration,
                updatedAt,
                endedAt);
    }

    public WorkflowExecution copy() {
        return new WorkflowExecution(this);
    }

    private void closePausedInterval(Instant now) {
        if (pausedAt != null) {
            pausedDuration = pausedDuration.plus(Duration.between(pausedAt, now));
            pausedAt = null;
        }
    }
}
