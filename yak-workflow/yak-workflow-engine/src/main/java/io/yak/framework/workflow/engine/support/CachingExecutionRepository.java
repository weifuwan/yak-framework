package io.yak.framework.workflow.engine.support;

import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.execution.WorkflowExecutionSnapshot;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Write-through cache for non-terminal workflow executions.
 *
 * <p>The delegate remains the durable source of truth across process restarts. While an execution is
 * active, repeated reads are served from an immutable snapshot and identical saves are suppressed.
 * Terminal executions are evicted immediately so historical reads keep using the delegate.</p>
 */
public final class CachingExecutionRepository implements ExecutionRepository {

    private final ExecutionRepository delegate;
    private final ConcurrentMap<String, WorkflowExecutionSnapshot> activeSnapshots =
            new ConcurrentHashMap<>();

    public CachingExecutionRepository(ExecutionRepository delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public void save(WorkflowExecution execution) {
        Objects.requireNonNull(execution, "execution");
        WorkflowExecutionSnapshot snapshot = execution.snapshot();
        WorkflowExecutionSnapshot cached = activeSnapshots.get(snapshot.id());
        if (snapshot.equals(cached)) {
            return;
        }

        delegate.save(execution);
        if (snapshot.status().isTerminal()) {
            activeSnapshots.remove(snapshot.id());
        } else {
            activeSnapshots.put(snapshot.id(), snapshot);
        }
    }

    @Override
    public Optional<WorkflowExecution> findById(String executionId) {
        Objects.requireNonNull(executionId, "executionId");
        WorkflowExecutionSnapshot cached = activeSnapshots.get(executionId);
        if (cached != null) {
            return Optional.of(WorkflowExecution.restore(cached));
        }

        Optional<WorkflowExecution> loaded = delegate.findById(executionId);
        loaded.ifPresent(execution -> {
            WorkflowExecutionSnapshot snapshot = execution.snapshot();
            if (!snapshot.status().isTerminal()) {
                activeSnapshots.put(executionId, snapshot);
            }
        });
        return loaded.map(WorkflowExecution::copy);
    }

    public void evict(String executionId) {
        if (executionId != null) {
            activeSnapshots.remove(executionId);
        }
    }

    public void clear() {
        activeSnapshots.clear();
    }

    int activeSize() {
        return activeSnapshots.size();
    }
}
