package io.yak.framework.workflow.engine.command;

import io.yak.framework.workflow.engine.execution.ExecutionValueSnapshot;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable input messages for one existing workflow execution.
 *
 * <p>All executor callbacks, timeout ticks, recovery, and manual execution controls are represented as
 * commands before they are allowed to mutate workflow state.</p>
 */
public sealed interface WorkflowCommand
        permits WorkflowCommand.NodeStarted,
                WorkflowCommand.NodeSucceeded,
                WorkflowCommand.NodeFailed,
                WorkflowCommand.NodePaused,
                WorkflowCommand.NodeResumed,
                WorkflowCommand.CheckTimeouts,
                WorkflowCommand.RecoverWorkflow,
                WorkflowCommand.PauseWorkflow,
                WorkflowCommand.ResumeWorkflow,
                WorkflowCommand.CancelWorkflow,
                WorkflowCommand.ContinueAfterFailure,
                WorkflowCommand.RetryFailedNode,
                WorkflowCommand.RetryFailedNodes,
                WorkflowCommand.RestartWorkflow,
                WorkflowCommand.RerunFromNode {

    String executionId();

    record NodeStarted(String executionId, String nodeId, String attemptId)
            implements WorkflowCommand {
        public NodeStarted {
            executionId = requireText(executionId, "executionId");
            nodeId = requireText(nodeId, "nodeId");
            attemptId = requireText(attemptId, "attemptId");
        }
    }

    record NodeSucceeded(
            String executionId,
            String nodeId,
            String attemptId,
            Map<String, Object> output)
            implements WorkflowCommand {
        public NodeSucceeded {
            executionId = requireText(executionId, "executionId");
            nodeId = requireText(nodeId, "nodeId");
            attemptId = requireText(attemptId, "attemptId");
            output = ExecutionValueSnapshot.immutableMap(output);
        }
    }

    record NodeFailed(
            String executionId,
            String nodeId,
            String attemptId,
            String errorMessage)
            implements WorkflowCommand {
        public NodeFailed {
            executionId = requireText(executionId, "executionId");
            nodeId = requireText(nodeId, "nodeId");
            attemptId = requireText(attemptId, "attemptId");
        }
    }

    record NodePaused(String executionId, String nodeId, String attemptId)
            implements WorkflowCommand {
        public NodePaused {
            executionId = requireText(executionId, "executionId");
            nodeId = requireText(nodeId, "nodeId");
            attemptId = requireText(attemptId, "attemptId");
        }
    }

    record NodeResumed(String executionId, String nodeId, String attemptId)
            implements WorkflowCommand {
        public NodeResumed {
            executionId = requireText(executionId, "executionId");
            nodeId = requireText(nodeId, "nodeId");
            attemptId = requireText(attemptId, "attemptId");
        }
    }

    record CheckTimeouts(String executionId) implements WorkflowCommand {
        public CheckTimeouts {
            executionId = requireText(executionId, "executionId");
        }
    }

    /** Reconcile one already persisted non-terminal workflow after host restart. */
    record RecoverWorkflow(String executionId) implements WorkflowCommand {
        public RecoverWorkflow {
            executionId = requireText(executionId, "executionId");
        }
    }

    record PauseWorkflow(String executionId, String reason) implements WorkflowCommand {
        public PauseWorkflow {
            executionId = requireText(executionId, "executionId");
        }
    }

    record ResumeWorkflow(String executionId) implements WorkflowCommand {
        public ResumeWorkflow {
            executionId = requireText(executionId, "executionId");
        }
    }

    record CancelWorkflow(String executionId, String reason) implements WorkflowCommand {
        public CancelWorkflow {
            executionId = requireText(executionId, "executionId");
        }
    }

    record ContinueAfterFailure(String executionId, String nodeId) implements WorkflowCommand {
        public ContinueAfterFailure {
            executionId = requireText(executionId, "executionId");
            nodeId = requireText(nodeId, "nodeId");
        }
    }

    record RetryFailedNode(String executionId, String nodeId) implements WorkflowCommand {
        public RetryFailedNode {
            executionId = requireText(executionId, "executionId");
            nodeId = requireText(nodeId, "nodeId");
        }
    }

    record RetryFailedNodes(String executionId) implements WorkflowCommand {
        public RetryFailedNodes {
            executionId = requireText(executionId, "executionId");
        }
    }

    record RestartWorkflow(String executionId) implements WorkflowCommand {
        public RestartWorkflow {
            executionId = requireText(executionId, "executionId");
        }
    }

    record RerunFromNode(String executionId, String nodeId) implements WorkflowCommand {
        public RerunFromNode {
            executionId = requireText(executionId, "executionId");
            nodeId = requireText(nodeId, "nodeId");
        }
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
