package io.yak.framework.workflow.engine.spi;

import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import java.util.Optional;

public interface ExecutionRepository {

    void save(WorkflowExecution execution);

    Optional<WorkflowExecution> findById(String executionId);
}
