package io.yak.framework.workflow.engine.policy;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;

@FunctionalInterface
public interface FailurePropagationPolicy {

    FailureHandlingResult onFinalFailure(
            WorkflowDefinition definition,
            WorkflowExecution workflowExecution,
            NodeExecution failedNode);
}
