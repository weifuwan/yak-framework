package io.yak.framework.workflow.engine.scheduler;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.graph.WorkflowGraph;
import java.util.Collection;
import java.util.List;

@FunctionalInterface
public interface WorkflowScheduler {

    List<NodeExecution> advance(
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            Collection<String> candidateNodeIds);
}
