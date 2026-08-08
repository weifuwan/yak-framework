package io.yak.framework.workflow.engine.definition;

import java.util.Set;

/** Parser helpers for the lightweight node input reference syntax. */
public final class NodeInputReference {

    public static final String WORKFLOW = "$workflow";
    public static final String PREDECESSOR_PREFIX = "$predecessor.";

    private NodeInputReference() {
    }

    public static boolean isWorkflowReference(String reference) {
        return WORKFLOW.equals(reference) || reference.startsWith(WORKFLOW + ".");
    }

    public static boolean isUnknownReservedReference(String reference) {
        return reference.startsWith("$")
                && !isWorkflowReference(reference)
                && !reference.startsWith(PREDECESSOR_PREFIX);
    }

    public static String predecessorReference(String reference) {
        return reference.startsWith(PREDECESSOR_PREFIX)
                ? reference.substring(PREDECESSOR_PREFIX.length())
                : reference;
    }

    public static String matchPredecessor(String reference, Set<String> predecessorIds) {
        String candidate = predecessorReference(reference);
        String bestMatch = null;
        for (String predecessorId : predecessorIds) {
            if (candidate.equals(predecessorId) || candidate.startsWith(predecessorId + ".")) {
                if (bestMatch == null || predecessorId.length() > bestMatch.length()) {
                    bestMatch = predecessorId;
                }
            }
        }
        return bestMatch;
    }

    public static String workflowPath(String reference) {
        return WORKFLOW.equals(reference)
                ? ""
                : reference.substring(WORKFLOW.length() + 1);
    }

    public static String predecessorPath(String reference, String predecessorId) {
        String candidate = predecessorReference(reference);
        return candidate.equals(predecessorId)
                ? ""
                : candidate.substring(predecessorId.length() + 1);
    }
}
