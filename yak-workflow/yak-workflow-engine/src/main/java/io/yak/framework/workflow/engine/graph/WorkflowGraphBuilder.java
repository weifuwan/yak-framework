package io.yak.framework.workflow.engine.graph;

import io.yak.framework.workflow.engine.definition.EdgeDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class WorkflowGraphBuilder {

    public WorkflowGraph build(WorkflowDefinition definition) {
        Set<String> nodes = new LinkedHashSet<>(definition.nodes().keySet());
        Map<String, Set<String>> successors = new LinkedHashMap<>();
        Map<String, Set<String>> predecessors = new LinkedHashMap<>();
        for (String node : nodes) {
            successors.put(node, new LinkedHashSet<>());
            predecessors.put(node, new LinkedHashSet<>());
        }
        for (EdgeDefinition edge : definition.edges()) {
            successors.computeIfAbsent(edge.fromNodeId(), ignored -> new LinkedHashSet<>())
                    .add(edge.toNodeId());
            predecessors.computeIfAbsent(edge.toNodeId(), ignored -> new LinkedHashSet<>())
                    .add(edge.fromNodeId());
        }
        return new DefaultWorkflowGraph(nodes, successors, predecessors);
    }
}
