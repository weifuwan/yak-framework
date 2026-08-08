package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.definition.NodeInputMapping;
import io.yak.framework.workflow.engine.definition.NodeInputReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Resolves a node's explicit input bindings against workflow input and direct predecessor output. */
public final class NodeInputResolver {

    public Map<String, Object> resolve(
            NodeInputMapping inputMapping,
            Map<String, Object> workflowInput,
            Map<String, Map<String, Object>> predecessorOutputs) {
        Objects.requireNonNull(inputMapping, "inputMapping");
        Map<String, Object> workflow = workflowInput == null ? Map.of() : workflowInput;
        Map<String, Map<String, Object>> predecessors =
                predecessorOutputs == null ? Map.of() : predecessorOutputs;
        if (inputMapping.isEmpty()) {
            return Map.of();
        }

        Map<String, Object> resolved = new LinkedHashMap<>();
        inputMapping.bindings().forEach((target, reference) ->
                resolved.put(target, resolveReference(reference, workflow, predecessors)));
        return Collections.unmodifiableMap(resolved);
    }

    private Object resolveReference(
            String reference,
            Map<String, Object> workflowInput,
            Map<String, Map<String, Object>> predecessorOutputs) {
        if (NodeInputReference.isWorkflowReference(reference)) {
            return readPath(workflowInput, NodeInputReference.workflowPath(reference));
        }

        String predecessorId = NodeInputReference.matchPredecessor(
                reference, predecessorOutputs.keySet());
        if (predecessorId == null) {
            return null;
        }
        Map<String, Object> output = predecessorOutputs.get(predecessorId);
        return readPath(output, NodeInputReference.predecessorPath(reference, predecessorId));
    }

    private Object readPath(Object root, String path) {
        if (path == null || path.isBlank()) {
            return root;
        }
        Object current = root;
        for (String segment : split(path)) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(segment);
            } else if (current instanceof List<?> list) {
                Integer index = parseIndex(segment);
                if (index == null || index < 0 || index >= list.size()) {
                    return null;
                }
                current = list.get(index);
            } else {
                return null;
            }
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private List<String> split(String path) {
        List<String> segments = new ArrayList<>();
        for (String segment : path.split("\\.")) {
            if (!segment.isEmpty()) {
                segments.add(segment);
            }
        }
        return segments;
    }

    private Integer parseIndex(String segment) {
        try {
            return Integer.valueOf(segment);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
