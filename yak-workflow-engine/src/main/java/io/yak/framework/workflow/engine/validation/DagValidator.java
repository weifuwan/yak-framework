package io.yak.framework.workflow.engine.validation;

import io.yak.framework.workflow.engine.model.WorkflowDefinition;

/**
 * Validates whether a workflow definition is a runnable directed acyclic graph.
 */
@FunctionalInterface
public interface DagValidator {

    DagValidationResult validate(WorkflowDefinition definition);
}
