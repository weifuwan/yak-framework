package io.yak.framework.workflow.engine.execution;

import io.yak.framework.workflow.engine.definition.NodeInputMapping;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Resolves a node's explicit input bindings against workflow input and direct predecessor output. */
public final class NodeInputResolver {

    private static final String WORKFLOW = "$workflow";
    private static final String PREDECESSOR = "$predecessor.";

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

    public static boolean isValidReference(String reference, Set<String> directPredecessors) {
        if (reference == null || reference.isBlank()) {
            return false;
        }
        if (reference.equals(WORKFLOW) || reference.startsWith(WORKFLOW + ".")) {
            return true;
        }
        if (reference.startsWith("$") && !reference.startsWith(PREDECESSOR)) {
            return false;
        }
        String predecessorReference = reference.startsWith(PREDECESSOR)
                ? reference.substring(PREDECESSOR.length())
                : reference;
        return matchPredecessor(predecessorReference, directPredecessors) != null;
    }

    private Object resolveReference(
            String reference,
            Map<String, Object> workflowInput,
            Map<String, Map<String, Object>> predecessorOutputs) {
        if (reference.equals(WORKFLOW)) {
            return workflowInput;
        }
        if (reference.startsWith(WORKFLOW + ".")) {
            return readPath(workflowInput, reference.substring(WORKFLOW.length() + 1));
        }

        String predecessorReference = reference.startsWith(PREDECESSOR)
                ? reference.substring(PREDECESSOR.length())
                : reference;
        String predecessorId = matchPredecessor(predecessorReference, predecessorOutputs.keySet());
        if (predecessorId == null) {
            return null;
        }
        Map<String, Object> output = predecessorOutputs.get(predecessorId);
        if (predecessorReference.equals(predecessorId)) {
            return output;
        }
        return readPath(output, predecessorReference.substring(predecessorId.length() + 1));
    }

    private static String matchPredecessor(String reference, Set<String> predecessorIds) {
        String bestMatch = null;
        for (String predecessorId : predecessorIds) {
            if (reference.equals(predecessorId) || reference.startsWith(predecessorId + ".")) {
                if (bestMatch == null || predecessorId.length() > bestMatch.length()) {
                    bestMatch = predecessorId;
                }
            }
        }
        return bestMatch;
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
