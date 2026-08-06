package io.yak.framework.workflow.engine.scheduler;

import io.yak.framework.workflow.engine.definition.NodeFailurePolicy;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import java.util.Optional;

public final class WorkflowCompletionResolver {

    public Optional<WorkflowExecutionStatus> resolve(WorkflowExecution execution) {
        if (execution.nodes().values().stream().anyMatch(node -> !node.status().isTerminal())) {
            return Optional.empty();
        }
        if (execution.status() == WorkflowExecutionStatus.CANCELED) {
            return Optional.of(WorkflowExecutionStatus.CANCELED);
        }
        boolean fatalFailure = execution.nodes().values().stream()
                .anyMatch(this::isFatalFailure);
        if (fatalFailure) {
            return Optional.of(WorkflowExecutionStatus.FAILED);
        }
        boolean warnings = execution.nodes().values().stream()
                .map(NodeExecution::status)
                .anyMatch(status -> status == NodeExecutionStatus.FAILED
                        || status == NodeExecutionStatus.UPSTREAM_FAILED
                        || status == NodeExecutionStatus.SKIPPED
                        || status == NodeExecutionStatus.CANCELED);
        return Optional.of(warnings
                ? WorkflowExecutionStatus.SUCCESS_WITH_WARNINGS
                : WorkflowExecutionStatus.SUCCESS);
    }

    private boolean isFatalFailure(NodeExecution node) {
        return node.status() == NodeExecutionStatus.FAILED
                && node.failurePolicy() == NodeFailurePolicy.FAIL_WORKFLOW
                && !node.failureHandled();
    }
}
