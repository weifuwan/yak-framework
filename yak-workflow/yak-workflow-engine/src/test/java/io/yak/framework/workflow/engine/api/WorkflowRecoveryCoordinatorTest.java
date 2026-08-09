package io.yak.framework.workflow.engine.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowFailureStrategy;
import io.yak.framework.workflow.engine.event.WorkflowEventListener;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.spi.ExecutionLock;
import io.yak.framework.workflow.engine.spi.IdGenerator;
import io.yak.framework.workflow.engine.spi.NodeDispatch;
import io.yak.framework.workflow.engine.spi.NodeExecutor;
import io.yak.framework.workflow.engine.spi.NodeRecovery;
import io.yak.framework.workflow.engine.support.InMemoryExecutionRepository;
import io.yak.framework.workflow.engine.support.InMemoryWorkflowDefinitionRepository;
import io.yak.framework.workflow.engine.support.LocalExecutionLock;
import io.yak.framework.workflow.engine.support.UuidIdGenerator;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class WorkflowRecoveryCoordinatorTest {

    @Test
    void snapshotRoundTripRestoresCompleteExecutionState() {
        Fixture fixture = fixture();
        WorkflowExecution started = fixture.engine.start("wf", Map.of("requestId", "r-1"));

        WorkflowExecution restored = WorkflowExecution.restore(started.snapshot());

        assertNotSame(started, restored);
        assertEquals(started.snapshot(), restored.snapshot());
    }

    @Test
    void recoveryResubmitsPersistedSubmittedAttemptWithSameAttemptId() {
        Fixture fixture = fixture();
        WorkflowExecution started = fixture.engine.start("wf", Map.of());
        String originalAttemptId = started.node("a").currentAttemptId();
        int originalAttemptCount = started.node("a").attempts().size();

        List<NodeRecovery> recoveries = new ArrayList<>();
        List<NodeDispatch> resubmits = new ArrayList<>();
        NodeExecutor recoveringExecutor = new NodeExecutor() {
            @Override
            public void submit(NodeDispatch dispatch) {
                resubmits.add(dispatch);
            }

            @Override
            public void recover(NodeRecovery recovery) {
                recoveries.add(recovery);
                NodeExecutor.super.recover(recovery);
            }
        };
        WorkflowRecoveryCoordinator coordinator = new WorkflowRecoveryCoordinator(
                fixture.definitionRepository,
                fixture.executionRepository,
                recoveringExecutor,
                fixture.executionLock,
                fixture.idGenerator,
                fixture.clock);

        WorkflowExecution recovered = coordinator.recover(started.id());

        assertEquals(1, recoveries.size());
        assertEquals(1, resubmits.size());
        assertEquals(originalAttemptId, recoveries.get(0).dispatch().attemptId());
        assertEquals(originalAttemptId, resubmits.get(0).attemptId());
        assertEquals(originalAttemptId, recovered.node("a").currentAttemptId());
        assertEquals(originalAttemptCount, recovered.node("a").attempts().size());
    }

    private Fixture fixture() {
        InMemoryWorkflowDefinitionRepository definitionRepository =
                new InMemoryWorkflowDefinitionRepository();
        InMemoryExecutionRepository executionRepository = new InMemoryExecutionRepository();
        ExecutionLock executionLock = new LocalExecutionLock();
        IdGenerator idGenerator = new UuidIdGenerator();
        Clock clock = Clock.fixed(Instant.parse("2026-08-09T08:00:00Z"), ZoneOffset.UTC);
        NodeExecutor nodeExecutor = dispatch -> { };
        DefaultWorkflowEngine engine = new DefaultWorkflowEngine(
                definitionRepository,
                executionRepository,
                nodeExecutor,
                executionLock,
                idGenerator,
                clock,
                WorkflowEventListener.noop());
        engine.registerDefinition(new WorkflowDefinition(
                "wf",
                "wf",
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                List.of(NodeDefinition.task("a")),
                List.of()));
        return new Fixture(
                engine,
                definitionRepository,
                executionRepository,
                executionLock,
                idGenerator,
                clock);
    }

    private record Fixture(
            DefaultWorkflowEngine engine,
            InMemoryWorkflowDefinitionRepository definitionRepository,
            InMemoryExecutionRepository executionRepository,
            ExecutionLock executionLock,
            IdGenerator idGenerator,
            Clock clock) {
    }
}
