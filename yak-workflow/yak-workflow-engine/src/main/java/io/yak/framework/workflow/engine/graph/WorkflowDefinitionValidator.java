package io.yak.framework.workflow.engine.graph;

import io.yak.framework.workflow.engine.definition.EdgeDefinition;
import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.NodeInputReference;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class WorkflowDefinitionValidator {

    private final WorkflowGraphBuilder graphBuilder;

    public WorkflowDefinitionValidator() {
        this(new WorkflowGraphBuilder());
    }

    public WorkflowDefinitionValidator(WorkflowGraphBuilder graphBuilder) {
        this.graphBuilder = graphBuilder;
    }

    public void validate(WorkflowDefinition definition) {
        List<String> errors = new ArrayList<>();
        if (definition.nodes().isEmpty()) {
            errors.add("at least one node is required");
        }
        Set<EdgeDefinition> uniqueEdges = new HashSet<>();
        for (EdgeDefinition edge : definition.edges()) {
            if (!definition.nodes().containsKey(edge.fromNodeId())) {
                errors.add("edge references unknown source node: " + edge.fromNodeId());
            }
            if (!definition.nodes().containsKey(edge.toNodeId())) {
                errors.add("edge references unknown target node: " + edge.toNodeId());
            }
            if (edge.fromNodeId().equals(edge.toNodeId())) {
                errors.add("self-loop is not allowed: " + edge.fromNodeId());
            }
            if (!uniqueEdges.add(edge)) {
                errors.add("duplicate edge: " + edge.fromNodeId() + " -> " + edge.toNodeId());
            }
        }
        if (errors.isEmpty()) {
            WorkflowGraph graph = graphBuilder.build(definition);
            if (graph.hasCycle()) {
                errors.add("workflow graph contains a cycle");
            }
            if (!definition.nodes().isEmpty() && graph.startNodes().isEmpty()) {
                errors.add("workflow has no start node");
            }
            validateInputMappings(definition, graph, errors);
        }
        if (!errors.isEmpty()) {
            throw new WorkflowValidationException(errors);
        }
    }

    private void validateInputMappings(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            List<String> errors) {
        for (NodeDefinition node : definition.nodes().values()) {
            Set<String> directPredecessors = graph.predecessors(node.id());
            node.inputMapping().bindings().forEach((target, reference) -> {
                if (NodeInputReference.isWorkflowReference(reference)) {
                    return;
                }
                if (NodeInputReference.isUnknownReservedReference(reference)
                        || NodeInputReference.matchPredecessor(reference, directPredecessors) == null) {
                    errors.add(
                            "node " + node.id() + " input " + target
                                    + " references non-predecessor source: " + reference);
                }
            });
        }
    }
}
