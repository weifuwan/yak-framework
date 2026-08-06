package io.yak.framework.workflow.engine.policy;

import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.execution.NodeExecution;

@FunctionalInterface
public interface RetryDecider {

    boolean shouldRetry(NodeDefinition definition, NodeExecution execution);
}
