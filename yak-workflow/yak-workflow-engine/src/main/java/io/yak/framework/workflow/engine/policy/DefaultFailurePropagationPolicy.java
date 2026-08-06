package io.yak.framework.workflow.engine.policy;

import io.yak.framework.workflow.engine.definition.NodeFailurePolicy;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowFailureStrategy;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;

public final class DefaultFailurePropagationPolicy implements FailurePropagationPolicy {

    @Override
    public FailureHandlingResult onFinalFailure(
            WorkflowDefinition definition,
            WorkflowExecution workflowExecution,
            NodeExecution failedNode) {
        if (failedNode.failurePolicy() == NodeFailurePolicy.IGNORE_FAILURE
                || failedNode.failurePolicy() == NodeFailurePolicy.BLOCK_BRANCH) {
            failedNode.markFailureHandled();
            return FailureHandlingResult.continueExecution();
        }
        WorkflowFailureStrategy strategy = definition.failureStrategy();
        return switch (strategy) {
            case CONTINUE_INDEPENDENT_BRANCHES -> FailureHandlingResult.continueExecution();
            case FAIL_FAST -> new FailureHandlingResult(true, false);
            case TERMINATE_ALL -> new FailureHandlingResult(true, true);
        };
    }
}
