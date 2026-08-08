package io.yak.framework.workflow.engine.api;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import io.yak.framework.workflow.engine.command.WorkflowCommand;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class WorkflowEngineCommandRoutingTest {

    @Test
    void convenienceApisTranslateCallbacksTimeoutsAndManualControlsIntoCommands() {
        RecordingWorkflowEngine engine = new RecordingWorkflowEngine();

        engine.acknowledgeNodeStarted("exec", "node", "attempt");
        engine.completeNode("exec", "node", "attempt", Map.of("value", 1));
        engine.failNode("exec", "node", "attempt", "failed");
        engine.acknowledgeNodePaused("exec", "node", "attempt");
        engine.acknowledgeNodeResumed("exec", "node", "attempt");
        engine.checkTimeouts("exec");
        engine.pause("exec", "maintenance");
        engine.resume("exec");
        engine.cancel("exec", "stop");
        engine.continueAfterFailure("exec", "node");
        engine.retryFailedNode("exec", "node");
        engine.retryFailedNodes("exec");
        engine.restart("exec");
        engine.rerunFromNode("exec", "node");

        List<WorkflowCommand> commands = engine.commands;
        assertInstanceOf(WorkflowCommand.NodeStarted.class, commands.get(0));
        assertInstanceOf(WorkflowCommand.NodeSucceeded.class, commands.get(1));
        assertInstanceOf(WorkflowCommand.NodeFailed.class, commands.get(2));
        assertInstanceOf(WorkflowCommand.NodePaused.class, commands.get(3));
        assertInstanceOf(WorkflowCommand.NodeResumed.class, commands.get(4));
        assertInstanceOf(WorkflowCommand.CheckTimeouts.class, commands.get(5));
        assertInstanceOf(WorkflowCommand.PauseWorkflow.class, commands.get(6));
        assertInstanceOf(WorkflowCommand.ResumeWorkflow.class, commands.get(7));
        assertInstanceOf(WorkflowCommand.CancelWorkflow.class, commands.get(8));
        assertInstanceOf(WorkflowCommand.ContinueAfterFailure.class, commands.get(9));
        assertInstanceOf(WorkflowCommand.RetryFailedNode.class, commands.get(10));
        assertInstanceOf(WorkflowCommand.RetryFailedNodes.class, commands.get(11));
        assertInstanceOf(WorkflowCommand.RestartWorkflow.class, commands.get(12));
        assertInstanceOf(WorkflowCommand.RerunFromNode.class, commands.get(13));
    }

    private static final class RecordingWorkflowEngine implements WorkflowEngine {

        private final List<WorkflowCommand> commands = new ArrayList<>();

        @Override
        public void registerDefinition(WorkflowDefinition definition) {
            // Not needed for command translation coverage.
        }

        @Override
        public WorkflowExecution start(String definitionId, Map<String, Object> input) {
            return null;
        }

        @Override
        public WorkflowExecution submit(WorkflowCommand command) {
            commands.add(command);
            return null;
        }

        @Override
        public Optional<WorkflowExecution> findExecution(String executionId) {
            return Optional.empty();
        }
    }
}
