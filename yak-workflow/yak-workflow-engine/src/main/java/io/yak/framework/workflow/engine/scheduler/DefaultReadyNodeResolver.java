package io.yak.framework.workflow.engine.scheduler;

import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.graph.WorkflowGraph;
import io.yak.framework.workflow.engine.policy.TriggerRuleEvaluator;
import java.util.List;

public final class DefaultReadyNodeResolver implements ReadyNodeResolver {

    private final TriggerRuleEvaluator triggerRuleEvaluator;

    public DefaultReadyNodeResolver(TriggerRuleEvaluator triggerRuleEvaluator) {
        this.triggerRuleEvaluator = triggerRuleEvaluator;
    }

    @Override
    public NodeActivation resolve(
            String nodeId,
            WorkflowDefinition definition,
            WorkflowGraph graph,
            WorkflowExecution execution,
            boolean schedulingStopped) {
        List<NodeExecution> predecessors = graph.predecessors(nodeId).stream()
                .map(execution::node)
                .toList();
        if (predecessors.stream().anyMatch(node -> !node.status().isTerminal())) {
            return NodeActivation.WAIT;
        }
        if (schedulingStopped) {
            return NodeActivation.BLOCK;
        }
        if (triggerRuleEvaluator.isSatisfied(
                definition.node(nodeId).triggerRule(), predecessors)) {
            return NodeActivation.RUN;
        }
        return predecessors.stream().anyMatch(NodeExecution::isFailureLike)
                ? NodeActivation.BLOCK
                : NodeActivation.SKIP;
    }
}
