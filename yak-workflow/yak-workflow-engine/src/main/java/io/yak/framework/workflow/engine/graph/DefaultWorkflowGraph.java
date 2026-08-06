package io.yak.framework.workflow.engine.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class DefaultWorkflowGraph implements WorkflowGraph {

    private final Set<String> nodes;
    private final Map<String, Set<String>> successors;
    private final Map<String, Set<String>> predecessors;

    public DefaultWorkflowGraph(
            Set<String> nodes,
            Map<String, Set<String>> successors,
            Map<String, Set<String>> predecessors) {
        this.nodes = Collections.unmodifiableSet(new LinkedHashSet<>(nodes));
        this.successors = immutableAdjacency(nodes, successors);
        this.predecessors = immutableAdjacency(nodes, predecessors);
    }

    @Override
    public Set<String> nodes() {
        return nodes;
    }

    @Override
    public Set<String> predecessors(String nodeId) {
        requireNode(nodeId);
        return predecessors.get(nodeId);
    }

    @Override
    public Set<String> successors(String nodeId) {
        requireNode(nodeId);
        return successors.get(nodeId);
    }

    @Override
    public Set<String> startNodes() {
        Set<String> result = new LinkedHashSet<>();
        for (String node : nodes) {
            if (predecessors.get(node).isEmpty()) {
                result.add(node);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    @Override
    public Set<String> endNodes() {
        Set<String> result = new LinkedHashSet<>();
        for (String node : nodes) {
            if (successors.get(node).isEmpty()) {
                result.add(node);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    @Override
    public Set<String> ancestors(String nodeId) {
        return traverse(nodeId, predecessors);
    }

    @Override
    public Set<String> descendants(String nodeId) {
        return traverse(nodeId, successors);
    }

    @Override
    public List<String> topologicalSort() {
        Map<String, Integer> indegree = new LinkedHashMap<>();
        Queue<String> ready = new ArrayDeque<>();
        for (String node : nodes) {
            int degree = predecessors.get(node).size();
            indegree.put(node, degree);
            if (degree == 0) {
                ready.offer(node);
            }
        }
        List<String> result = new ArrayList<>(nodes.size());
        while (!ready.isEmpty()) {
            String current = ready.poll();
            result.add(current);
            for (String successor : successors.get(current)) {
                int degree = indegree.computeIfPresent(successor, (key, value) -> value - 1);
                if (degree == 0) {
                    ready.offer(successor);
                }
            }
        }
        if (result.size() != nodes.size()) {
            throw new IllegalStateException("Workflow graph contains a cycle");
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public boolean hasCycle() {
        try {
            topologicalSort();
            return false;
        } catch (IllegalStateException exception) {
            return true;
        }
    }

    private Set<String> traverse(String nodeId, Map<String, Set<String>> adjacency) {
        requireNode(nodeId);
        Set<String> visited = new LinkedHashSet<>();
        Deque<String> stack = new ArrayDeque<>(adjacency.get(nodeId));
        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (visited.add(current)) {
                stack.addAll(adjacency.get(current));
            }
        }
        return Collections.unmodifiableSet(visited);
    }

    private void requireNode(String nodeId) {
        if (!nodes.contains(nodeId)) {
            throw new IllegalArgumentException("Unknown node: " + nodeId);
        }
    }

    private static Map<String, Set<String>> immutableAdjacency(
            Set<String> nodes, Map<String, Set<String>> source) {
        Map<String, Set<String>> result = new LinkedHashMap<>();
        for (String node : nodes) {
            result.put(
                    node,
                    Collections.unmodifiableSet(
                            new LinkedHashSet<>(source.getOrDefault(node, Set.of()))));
        }
        return Collections.unmodifiableMap(result);
    }
}
