package io.yak.framework.workflow.engine.policy;

import io.yak.framework.workflow.engine.definition.TriggerRule;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import java.util.Collection;

public final class DefaultTriggerRuleEvaluator implements TriggerRuleEvaluator {

    @Override
    public boolean isSatisfied(
            TriggerRule triggerRule, Collection<NodeExecution> predecessors) {
        return switch (triggerRule) {
            case ALL_SUCCESS -> predecessors.stream().allMatch(NodeExecution::isEffectiveSuccess);
            case ALL_DONE -> predecessors.stream().allMatch(node -> node.status().isTerminal());
            case NONE_FAILED -> predecessors.stream().noneMatch(NodeExecution::isFailureLike);
            case ONE_SUCCESS -> predecessors.stream().anyMatch(NodeExecution::isEffectiveSuccess);
            case ALWAYS -> true;
        };
    }
}
