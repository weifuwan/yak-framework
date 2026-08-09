package io.yak.framework.workflow.engine.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class CachingExecutionRepositoryTest {

    @Test
    void suppressesIdenticalWritesAndServesActiveReadsFromSnapshot() {
        CountingRepository delegate = new CountingRepository();
        CachingExecutionRepository repository = new CachingExecutionRepository(delegate);
        WorkflowExecution execution = runningExecution("execution-1");

        repository.save(execution);
        repository.save(execution.copy());

        assertEquals(1, delegate.saveCount.get());
        WorkflowExecution first = repository.findById(execution.id()).orElseThrow();
        WorkflowExecution second = repository.findById(execution.id()).orElseThrow();
        assertEquals(0, delegate.findCount.get());
        assertNotSame(first, second);
        assertEquals(1, repository.activeSize());

        execution.touch(Instant.parse("2026-08-09T12:00:01Z"));
        repository.save(execution);
        assertEquals(2, delegate.saveCount.get());
    }

    @Test
    void evictsTerminalExecutionsSoHistoryReadsUseDelegate() {
        CountingRepository delegate = new CountingRepository();
        CachingExecutionRepository repository = new CachingExecutionRepository(delegate);
        WorkflowExecution execution = runningExecution("execution-2");
        repository.save(execution);

        execution.transitionTo(
                WorkflowExecutionStatus.SUCCESS,
                Instant.parse("2026-08-09T12:00:02Z"));
        repository.save(execution);

        assertEquals(0, repository.activeSize());
        WorkflowExecution loaded = repository.findById(execution.id()).orElseThrow();
        assertEquals(WorkflowExecutionStatus.SUCCESS, loaded.status());
        assertEquals(1, delegate.findCount.get());
    }

    private WorkflowExecution runningExecution(String id) {
        Instant now = Instant.parse("2026-08-09T12:00:00Z");
        WorkflowExecution execution = new WorkflowExecution(
                id,
                "definition-1",
                null,
                Map.of(),
                Map.of(),
                now);
        execution.transitionTo(WorkflowExecutionStatus.RUNNING, now);
        return execution;
    }

    private static final class CountingRepository implements ExecutionRepository {
        private final Map<String, WorkflowExecution> values = new LinkedHashMap<>();
        private final AtomicInteger saveCount = new AtomicInteger();
        private final AtomicInteger findCount = new AtomicInteger();

        @Override
        public void save(WorkflowExecution execution) {
            saveCount.incrementAndGet();
            values.put(execution.id(), execution.copy());
        }

        @Override
        public Optional<WorkflowExecution> findById(String executionId) {
            findCount.incrementAndGet();
            WorkflowExecution execution = values.get(executionId);
            return execution == null ? Optional.empty() : Optional.of(execution.copy());
        }
    }
}
