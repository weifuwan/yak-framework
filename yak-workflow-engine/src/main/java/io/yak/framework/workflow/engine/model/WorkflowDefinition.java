package io.yak.framework.workflow.engine.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable workflow graph definition consumed by the engine.
 *
 * @param code workflow identifier
 * @param nodes workflow nodes
 * @param edges directed dependencies between nodes
 */
public record WorkflowDefinition(
        String code, List<WorkflowNode> nodes, List<WorkflowEdge> edges) {

    public WorkflowDefinition {
        nodes = immutableCopy(Objects.requireNonNull(nodes, "nodes must not be null"));
        edges = immutableCopy(Objects.requireNonNull(edges, "edges must not be null"));
    }

    private static <T> List<T> immutableCopy(List<T> source) {
        return Collections.unmodifiableList(new ArrayList<>(source));
    }
}
