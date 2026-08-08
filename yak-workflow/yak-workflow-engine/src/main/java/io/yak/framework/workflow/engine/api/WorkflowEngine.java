package io.yak.framework.workflow.engine.api;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import java.util.Map;
import java.util.Optional;

public interface WorkflowEngine {

    void registerDefinition(WorkflowDefinition definition);

    WorkflowExecution start(String definitionId, Map<String, Object> input);

    WorkflowExecution acknowledgeNodeStarted(
            String executionId, String nodeId, String attemptId);

    WorkflowExecution completeNode(
            String executionId,
            String nodeId,
            String attemptId,
            Map<String, Object> output);

    WorkflowExecution failNode(
            String executionId,
            String nodeId,
            String attemptId,
            String errorMessage);

    WorkflowExecution continueAfterFailure(String executionId, String nodeId);

    WorkflowExecution retryFailedNode(String executionId, String nodeId);

    WorkflowExecution cancel(String executionId, String reason);

    WorkflowExecution retryFailedNodes(String executionId);

    WorkflowExecution restart(String sourceExecutionId);

    WorkflowExecution rerunFromNode(String sourceExecutionId, String nodeId);

    Optional<WorkflowExecution> findExecution(String executionId);
}
