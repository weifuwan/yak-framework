package io.yak.framework.workflow.engine.support;

import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.spi.ExecutionRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryExecutionRepository implements ExecutionRepository {

    private final ConcurrentMap<String, WorkflowExecution> executions = new ConcurrentHashMap<>();

    @Override
    public void save(WorkflowExecution execution) {
        executions.put(execution.id(), execution.copy());
    }

    @Override
    public Optional<WorkflowExecution> findById(String executionId) {
        return Optional.ofNullable(executions.get(executionId)).map(WorkflowExecution::copy);
    }
}
