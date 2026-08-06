package io.yak.framework.workflow.engine.policy;

import io.yak.framework.workflow.engine.definition.TriggerRule;
import io.yak.framework.workflow.engine.execution.NodeExecution;
import java.util.Collection;

@FunctionalInterface
public interface TriggerRuleEvaluator {

    boolean isSatisfied(TriggerRule triggerRule, Collection<NodeExecution> predecessors);
}
