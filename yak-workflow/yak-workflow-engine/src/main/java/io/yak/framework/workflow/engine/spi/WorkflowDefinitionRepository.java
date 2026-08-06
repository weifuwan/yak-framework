package io.yak.framework.workflow.engine.spi;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import java.util.Optional;

public interface WorkflowDefinitionRepository {

    void save(WorkflowDefinition definition);

    Optional<WorkflowDefinition> findById(String definitionId);
}
