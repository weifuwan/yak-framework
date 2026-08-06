package io.yak.framework.workflow.engine.scheduler;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.graph.WorkflowGraph;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class DefaultWorkflowScheduler implements WorkflowScheduler {

    private final ReadyNodeResolver readyNodeResolver;

    public DefaultWorkflowScheduler(ReadyNodeResolver readyNodeResolver) {
        this.readyNodeResolver = readyNodeResolver;
    }

    @Override
    public List<NodeExecution> advance(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            Collection<String> candidateNodeIds) {
        Queue<String> candidates = new ArrayDeque<>(candidateNodeIds);
        Set<String> processed = new HashSet<>();
        List<NodeExecution> ready = new ArrayList<>();
        while (!candidates.isEmpty()) {
            String nodeId = candidates.poll();
            if (processed.contains(nodeId)) {
                continue;
            }
            NodeExecution node = execution.node(nodeId);
            if (node.status() != NodeExecutionStatus.WAITING) {
                continue;
            }
            NodeActivation activation = readyNodeResolver.resolve(
                    nodeId, definition, graph, execution, execution.schedulingStopped());
            switch (activation) {
                case WAIT -> {
                    // A different predecessor completion will evaluate this node again.
                }
                case RUN -> {
                    processed.add(nodeId);
                    node.transitionTo(NodeExecutionStatus.READY);
                    ready.add(node);
                }
                case SKIP -> {
                    processed.add(nodeId);
                    node.transitionTo(NodeExecutionStatus.SKIPPED);
                    candidates.addAll(graph.successors(nodeId));
                }
                case BLOCK -> {
                    processed.add(nodeId);
                    node.transitionTo(NodeExecutionStatus.UPSTREAM_FAILED);
                    candidates.addAll(graph.successors(nodeId));
                }
            }
        }
        return List.copyOf(ready);
    }
}
