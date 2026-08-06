package io.yak.framework.workflow.engine.definition;

/** Workflow-level behavior after an unhandled node failure. */
public enum WorkflowFailureStrategy {
    /** Stop creating new work, while already running sibling nodes are allowed to finish. */
    FAIL_FAST,
    /** Continue independent branches and only block branches whose trigger rules cannot be met. */
    CONTINUE_INDEPENDENT_BRANCHES,
    /** Cancel active work immediately and finish the workflow as failed. */
    TERMINATE_ALL
}
