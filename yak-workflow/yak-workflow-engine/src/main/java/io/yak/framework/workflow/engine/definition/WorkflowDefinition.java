package io.yak.framework.workflow.engine.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class WorkflowDefinition {

    private final String id;
    private final String name;
    private final WorkflowFailureStrategy failureStrategy;
    private final WorkflowTimeoutPolicy timeoutPolicy;
    private final Map<String, NodeDefinition> nodes;
    private final List<EdgeDefinition> edges;

    public WorkflowDefinition(
            String id,
            String name,
            WorkflowFailureStrategy failureStrategy,
            List<NodeDefinition> nodes,
            List<EdgeDefinition> edges) {
        this(
                id,
                name,
                failureStrategy,
                WorkflowTimeoutPolicy.none(),
                nodes,
                edges);
    }

    public WorkflowDefinition(
            String id,
            String name,
            WorkflowFailureStrategy failureStrategy,
            WorkflowTimeoutPolicy timeoutPolicy,
            List<NodeDefinition> nodes,
            List<EdgeDefinition> edges) {
        this.id = requireText(id, "id");
        this.name = name == null || name.isBlank() ? id : name;
        this.failureStrategy = Objects.requireNonNullElse(
                failureStrategy, WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES);
        this.timeoutPolicy = Objects.requireNonNullElseGet(
                timeoutPolicy, WorkflowTimeoutPolicy::none);
        Objects.requireNonNull(nodes, "nodes");
        Map<String, NodeDefinition> nodeMap = new LinkedHashMap<>();
        for (NodeDefinition node : nodes) {
            NodeDefinition previous = nodeMap.putIfAbsent(node.id(), node);
            if (previous != null) {
                throw new IllegalArgumentException("Duplicate node id: " + node.id());
            }
        }
        this.nodes = Collections.unmodifiableMap(nodeMap);
        this.edges = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNullElse(edges, List.of())));
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public WorkflowFailureStrategy failureStrategy() {
        return failureStrategy;
    }

    public WorkflowTimeoutPolicy timeoutPolicy() {
        return timeoutPolicy;
    }

    public Map<String, NodeDefinition> nodes() {
        return nodes;
    }

    public NodeDefinition node(String nodeId) {
        NodeDefinition node = nodes.get(nodeId);
        if (node == null) {
            throw new IllegalArgumentException("Unknown node: " + nodeId);
        }
        return node;
    }

    public List<EdgeDefinition> edges() {
        return edges;
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
