package io.yak.framework.workflow.engine.validation;

import io.yak.framework.workflow.engine.model.WorkflowDefinition;
import io.yak.framework.workflow.engine.model.WorkflowEdge;
import io.yak.framework.workflow.engine.model.WorkflowNode;
import io.yak.framework.workflow.engine.model.WorkflowNodeType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default structural validator for workflow DAG definitions.
 */
public final class DefaultDagValidator implements DagValidator {

    @Override
    public DagValidationResult validate(WorkflowDefinition definition) {
        Objects.requireNonNull(definition, "definition must not be null");

        List<DagValidationError> errors = new ArrayList<>();
        validateWorkflowCode(definition, errors);

        NodeValidation nodeValidation = validateNodes(definition.nodes(), errors);
        EdgeValidation edgeValidation =
                validateEdges(definition.edges(), nodeValidation.nodesByCode(), errors);

        validateBoundaryNodes(nodeValidation.nodesByCode().values(), edgeValidation, errors);
        validateGraph(nodeValidation, edgeValidation, errors);

        return new DagValidationResult(errors);
    }

    private void validateWorkflowCode(
            WorkflowDefinition definition, List<DagValidationError> errors) {
        if (isBlank(definition.code())) {
            errors.add(error(
                    DagValidationErrorCode.WORKFLOW_CODE_BLANK,
                    "Workflow code must not be blank",
                    null));
        }
    }

    private NodeValidation validateNodes(
            List<WorkflowNode> nodes, List<DagValidationError> errors) {
        Map<String, WorkflowNode> nodesByCode = new LinkedHashMap<>();
        boolean malformed = false;

        for (int index = 0; index < nodes.size(); index++) {
            WorkflowNode node = nodes.get(index);
            String elementId = "node[" + index + "]";

            if (node == null) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.NODE_NULL,
                        "Workflow node must not be null",
                        elementId));
                continue;
            }

            if (isBlank(node.code())) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.NODE_CODE_BLANK,
                        "Workflow node code must not be blank",
                        elementId));
            }

            if (node.type() == null) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.NODE_TYPE_MISSING,
                        "Workflow node type must not be null",
                        node.code()));
            }

            if (isBlank(node.code())) {
                continue;
            }

            WorkflowNode previous = nodesByCode.putIfAbsent(node.code(), node);
            if (previous != null) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.DUPLICATE_NODE_CODE,
                        "Duplicate workflow node code: " + node.code(),
                        node.code()));
            }
        }

        return new NodeValidation(nodesByCode, malformed);
    }

    private EdgeValidation validateEdges(
            List<WorkflowEdge> edges,
            Map<String, WorkflowNode> nodesByCode,
            List<DagValidationError> errors) {
        Map<String, Set<String>> outgoing = initializeAdjacency(nodesByCode.keySet());
        Map<String, Set<String>> incoming = initializeAdjacency(nodesByCode.keySet());
        Set<EdgeKey> edgeKeys = new HashSet<>();
        boolean malformed = false;

        for (int index = 0; index < edges.size(); index++) {
            WorkflowEdge edge = edges.get(index);
            String elementId = "edge[" + index + "]";

            if (edge == null) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.EDGE_NULL,
                        "Workflow edge must not be null",
                        elementId));
                continue;
            }

            boolean sourceBlank = isBlank(edge.sourceCode());
            boolean targetBlank = isBlank(edge.targetCode());

            if (sourceBlank) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.EDGE_SOURCE_BLANK,
                        "Workflow edge source code must not be blank",
                        elementId));
            }
            if (targetBlank) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.EDGE_TARGET_BLANK,
                        "Workflow edge target code must not be blank",
                        elementId));
            }
            if (sourceBlank || targetBlank) {
                continue;
            }

            String edgeId = edge.sourceCode() + "->" + edge.targetCode();
            boolean sourceKnown = nodesByCode.containsKey(edge.sourceCode());
            boolean targetKnown = nodesByCode.containsKey(edge.targetCode());

            if (!sourceKnown) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.UNKNOWN_EDGE_SOURCE,
                        "Unknown edge source node: " + edge.sourceCode(),
                        edgeId));
            }
            if (!targetKnown) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.UNKNOWN_EDGE_TARGET,
                        "Unknown edge target node: " + edge.targetCode(),
                        edgeId));
            }

            if (edge.sourceCode().equals(edge.targetCode())) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.SELF_LOOP,
                        "Workflow node must not depend on itself: " + edge.sourceCode(),
                        edgeId));
            }

            EdgeKey edgeKey = new EdgeKey(edge.sourceCode(), edge.targetCode());
            if (!edgeKeys.add(edgeKey)) {
                malformed = true;
                errors.add(error(
                        DagValidationErrorCode.DUPLICATE_EDGE,
                        "Duplicate workflow edge: " + edgeId,
                        edgeId));
            }

            if (sourceKnown
                    && targetKnown
                    && !edge.sourceCode().equals(edge.targetCode())) {
                outgoing.get(edge.sourceCode()).add(edge.targetCode());
                incoming.get(edge.targetCode()).add(edge.sourceCode());
            }
        }

        return new EdgeValidation(outgoing, incoming, malformed);
    }

    private void validateBoundaryNodes(
            Collection<WorkflowNode> nodes,
            EdgeValidation graph,
            List<DagValidationError> errors) {
        List<WorkflowNode> startNodes = nodes.stream()
                .filter(node -> node.type() == WorkflowNodeType.START)
                .toList();
        List<WorkflowNode> endNodes = nodes.stream()
                .filter(node -> node.type() == WorkflowNodeType.END)
                .toList();

        if (startNodes.isEmpty()) {
            errors.add(error(
                    DagValidationErrorCode.START_NODE_MISSING,
                    "Workflow must contain exactly one START node",
                    null));
        } else if (startNodes.size() > 1) {
            errors.add(error(
                    DagValidationErrorCode.MULTIPLE_START_NODES,
                    "Workflow must contain exactly one START node",
                    startNodes.stream().map(WorkflowNode::code).collect(Collectors.joining(","))));
        }

        if (endNodes.isEmpty()) {
            errors.add(error(
                    DagValidationErrorCode.END_NODE_MISSING,
                    "Workflow must contain at least one END node",
                    null));
        }

        for (WorkflowNode startNode : startNodes) {
            if (!graph.incoming().getOrDefault(startNode.code(), Set.of()).isEmpty()) {
                errors.add(error(
                        DagValidationErrorCode.START_NODE_HAS_INCOMING_EDGE,
                        "START node must not have incoming edges: " + startNode.code(),
                        startNode.code()));
            }
        }

        for (WorkflowNode endNode : endNodes) {
            if (!graph.outgoing().getOrDefault(endNode.code(), Set.of()).isEmpty()) {
                errors.add(error(
                        DagValidationErrorCode.END_NODE_HAS_OUTGOING_EDGE,
                        "END node must not have outgoing edges: " + endNode.code(),
                        endNode.code()));
            }
        }
    }

    private void validateGraph(
            NodeValidation nodeValidation,
            EdgeValidation edgeValidation,
            List<DagValidationError> errors) {
        Map<String, WorkflowNode> nodesByCode = nodeValidation.nodesByCode();
        if (nodesByCode.isEmpty()) {
            return;
        }

        List<String> startCodes = nodesByCode.values().stream()
                .filter(node -> node.type() == WorkflowNodeType.START)
                .map(WorkflowNode::code)
                .toList();
        Set<String> endCodes = nodesByCode.values().stream()
                .filter(node -> node.type() == WorkflowNodeType.END)
                .map(WorkflowNode::code)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (!nodeValidation.malformed() && !edgeValidation.malformed()) {
            validateAcyclic(nodesByCode.keySet(), edgeValidation, errors);
        }

        if (startCodes.size() == 1) {
            validateReachableFromStart(
                    nodesByCode.keySet(), startCodes.getFirst(), edgeValidation.outgoing(), errors);
        }

        if (!endCodes.isEmpty()) {
            validateCanReachEnd(
                    nodesByCode.keySet(), endCodes, edgeValidation.incoming(), errors);
        }
    }

    private void validateAcyclic(
            Set<String> nodeCodes,
            EdgeValidation graph,
            List<DagValidationError> errors) {
        Map<String, Integer> inDegree = new LinkedHashMap<>();
        for (String nodeCode : nodeCodes) {
            inDegree.put(nodeCode, graph.incoming().getOrDefault(nodeCode, Set.of()).size());
        }

        Deque<String> queue = new ArrayDeque<>();
        inDegree.forEach((nodeCode, degree) -> {
            if (degree == 0) {
                queue.add(nodeCode);
            }
        });

        int visited = 0;
        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            visited++;

            for (String target : graph.outgoing().getOrDefault(current, Set.of())) {
                int remaining = inDegree.computeIfPresent(target, (key, value) -> value - 1);
                if (remaining == 0) {
                    queue.addLast(target);
                }
            }
        }

        if (visited != nodeCodes.size()) {
            String cyclicNodes = inDegree.entrySet().stream()
                    .filter(entry -> entry.getValue() > 0)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.joining(","));
            errors.add(error(
                    DagValidationErrorCode.CYCLE_DETECTED,
                    "Workflow graph contains a cycle involving: " + cyclicNodes,
                    cyclicNodes));
        }
    }

    private void validateReachableFromStart(
            Set<String> nodeCodes,
            String startCode,
            Map<String, Set<String>> outgoing,
            List<DagValidationError> errors) {
        Set<String> reachable = traverse(Set.of(startCode), outgoing);
        for (String nodeCode : nodeCodes) {
            if (!reachable.contains(nodeCode)) {
                errors.add(error(
                        DagValidationErrorCode.NODE_UNREACHABLE_FROM_START,
                        "Node is unreachable from START: " + nodeCode,
                        nodeCode));
            }
        }
    }

    private void validateCanReachEnd(
            Set<String> nodeCodes,
            Set<String> endCodes,
            Map<String, Set<String>> incoming,
            List<DagValidationError> errors) {
        Set<String> canReachEnd = traverse(endCodes, incoming);
        for (String nodeCode : nodeCodes) {
            if (!canReachEnd.contains(nodeCode)) {
                errors.add(error(
                        DagValidationErrorCode.NODE_CANNOT_REACH_END,
                        "Node cannot reach an END node: " + nodeCode,
                        nodeCode));
            }
        }
    }

    private Set<String> traverse(
            Collection<String> initialNodes, Map<String, Set<String>> adjacency) {
        Set<String> visited = new LinkedHashSet<>();
        Deque<String> queue = new ArrayDeque<>(initialNodes);

        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            if (!visited.add(current)) {
                continue;
            }
            queue.addAll(adjacency.getOrDefault(current, Set.of()));
        }

        return visited;
    }

    private Map<String, Set<String>> initializeAdjacency(Set<String> nodeCodes) {
        Map<String, Set<String>> adjacency = new LinkedHashMap<>();
        for (String nodeCode : nodeCodes) {
            adjacency.put(nodeCode, new LinkedHashSet<>());
        }
        return adjacency;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private DagValidationError error(
            DagValidationErrorCode code, String message, String elementId) {
        return new DagValidationError(code, message, elementId);
    }

    private record NodeValidation(Map<String, WorkflowNode> nodesByCode, boolean malformed) {}

    private record EdgeValidation(
            Map<String, Set<String>> outgoing,
            Map<String, Set<String>> incoming,
            boolean malformed) {}

    private record EdgeKey(String sourceCode, String targetCode) {}
}
