package io.yak.framework.workflow.engine.policy;

import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.execution.NodeExecution;

public final class DefaultRetryDecider implements RetryDecider {

    @Override
    public boolean shouldRetry(NodeDefinition definition, NodeExecution execution) {
        return execution.attempts().size() < definition.retryPolicy().maxAttempts();
    }
}
