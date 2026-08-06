package io.yak.framework.workflow.engine.scheduler;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.graph.WorkflowGraph;

@FunctionalInterface
public interface ReadyNodeResolver {

    NodeActivation resolve(
            String nodeId,
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            boolean schedulingStopped);
}
