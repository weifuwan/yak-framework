package io.yak.framework.workflow.engine.definition;

/** Node-level failure semantics. */
public enum NodeFailurePolicy {
    /** The failure contributes to the final FAILED workflow state. */
    FAIL_WORKFLOW,
    /** The current branch is blocked, while the workflow may finish with warnings. */
    BLOCK_BRANCH,
    /** The failure is retained for audit but treated as successful for downstream triggers. */
    IGNORE_FAILURE
}
