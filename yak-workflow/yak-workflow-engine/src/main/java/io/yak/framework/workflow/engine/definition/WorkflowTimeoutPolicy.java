package io.yak.framework.workflow.engine.definition;

import java.time.Duration;
import java.util.Objects;

/** Wall-clock timeout for one active workflow run segment. A zero duration disables it. */
public record WorkflowTimeoutPolicy(Duration timeout) {

    public WorkflowTimeoutPolicy {
        timeout = Objects.requireNonNullElse(timeout, Duration.ZERO);
        if (timeout.isNegative()) {
            throw new IllegalArgumentException("timeout must not be negative");
        }
    }

    public static WorkflowTimeoutPolicy none() {
        return new WorkflowTimeoutPolicy(Duration.ZERO);
    }

    public static WorkflowTimeoutPolicy of(Duration timeout) {
        return new WorkflowTimeoutPolicy(timeout);
    }

    public boolean enabled() {
        return !timeout.isZero();
    }
}
