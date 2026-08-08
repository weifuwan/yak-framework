package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowStateMachine;
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
        this.input = input == null
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(input));
        this.nodes = new LinkedHashMap<>(Objects.requireNonNull(nodes, "nodes"));
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = createdAt;
        this.status = WorkflowExecutionStatus.CREATED;
    }

    private WorkflowExecution(WorkflowExecution source) {
        this.id = source.id;
        this.definitionId = source.definitionId;
        this.sourceExecutionId = source.sourceExecutionId;
        this.input = Collections.unmodifiableMap(new LinkedHashMap<>(source.input));
        this.nodes = new LinkedHashMap<>();
        source.nodes.forEach((nodeId, execution) -> this.nodes.put(nodeId, execution.copy()));
        this.createdAt = source.createdAt;
        this.status = source.status;
        this.schedulingStopped = source.schedulingStopped;
        this.runStartedAt = source.runStartedAt;
        this.updatedAt = source.updatedAt;
        this.endedAt = source.endedAt;
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

    /** Start of the current active RUNNING segment, used as the workflow timeout anchor. */
    public Instant runStartedAt() {
        return runStartedAt;
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
            runStartedAt = now;
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

    public WorkflowExecution copy() {
        return new WorkflowExecution(this);
    }
}
