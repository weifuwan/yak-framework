package io.yak.framework.workflow.engine.support;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.spi.WorkflowDefinitionRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryWorkflowDefinitionRepository implements WorkflowDefinitionRepository {

    private final ConcurrentMap<String, WorkflowDefinition> definitions = new ConcurrentHashMap<>();

    @Override
    public void save(WorkflowDefinition definition) {
        definitions.put(definition.id(), definition);
    }

    @Override
    public Optional<WorkflowDefinition> findById(String definitionId) {
        return Optional.ofNullable(definitions.get(definitionId));
    }
}
