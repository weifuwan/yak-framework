package io.yak.framework.workflow.engine.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.yak.framework.workflow.engine.api.DefaultWorkflowEngine;
import io.yak.framework.workflow.engine.api.WorkflowEngine;
import io.yak.framework.workflow.engine.api.WorkflowRecoveryCoordinator;
import io.yak.framework.workflow.engine.command.WorkflowCommand;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.event.WorkflowEventListener;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.spi.ExecutionLock;
import io.yak.framework.workflow.engine.spi.ExecutionMailbox;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import io.yak.framework.workflow.engine.spi.IdGenerator;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.spi.WorkflowDefinitionRepository;
import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class WorkflowEngineApiCompatibilityTest {

    @Test
    void defaultEngineKeepsHostConstructorsAndFactories() throws Exception {
        assertNotNull(DefaultWorkflowEngine.class.getConstructor(
                WorkflowDefinitionRepository.class,
                ExecutionRepository.class,
                NodeExecutor.class,
                ExecutionLock.class,
                IdGenerator.class,
                Clock.class,
                WorkflowEventListener.class));
        assertNotNull(DefaultWorkflowEngine.class.getConstructor(
                WorkflowDefinitionRepository.class,
                ExecutionRepository.class,
                NodeExecutor.class,
                ExecutionMailbox.class,
                IdGenerator.class,
                Clock.class,
                WorkflowEventListener.class));
        assertEquals(
                DefaultWorkflowEngine.class,
                DefaultWorkflowEngine.class.getMethod("inMemory", NodeExecutor.class).getReturnType());
        assertEquals(
                DefaultWorkflowEngine.class,
                DefaultWorkflowEngine.class
                        .getMethod(
                                "inMemory",
                                NodeExecutor.class,
                                WorkflowEventListener.class,
                                Clock.class)
                        .getReturnType());
    }

    @Test
    void workflowEngineContractKeepsCoreMethods() throws Exception {
        assertEquals(
                void.class,
                WorkflowEngine.class
                        .getMethod("registerDefinition", WorkflowDefinition.class)
                        .getReturnType());
        assertEquals(
                WorkflowExecution.class,
                WorkflowEngine.class
                        .getMethod("start", String.class, Map.class)
                        .getReturnType());
        assertEquals(
                WorkflowExecution.class,
                WorkflowEngine.class
                        .getMethod("submit", WorkflowCommand.class)
                        .getReturnType());
        assertEquals(
                Optional.class,
                WorkflowEngine.class
                        .getMethod("findExecution", String.class)
                        .getReturnType());
    }

    @Test
    void recoveryFacadeKeepsConstructorAndRecoverMethod() throws Exception {
        assertNotNull(WorkflowRecoveryCoordinator.class.getConstructor(
                WorkflowDefinitionRepository.class,
                ExecutionRepository.class,
                NodeExecutor.class,
                ExecutionLock.class,
                IdGenerator.class,
                Clock.class));
        assertEquals(
                WorkflowExecution.class,
                WorkflowRecoveryCoordinator.class
                        .getMethod("recover", String.class)
                        .getReturnType());
    }
}
