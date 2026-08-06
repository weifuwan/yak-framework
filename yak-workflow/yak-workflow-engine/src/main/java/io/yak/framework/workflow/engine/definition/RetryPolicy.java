package io.yak.framework.workflow.engine.definition;

import java.time.Duration;
import java.util.Objects;

/** Automatic retry policy. maxAttempts includes the first execution attempt. */
public record RetryPolicy(int maxAttempts, Duration delay) {

    public RetryPolicy {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }
        delay = Objects.requireNonNull(delay, "delay");
        if (delay.isNegative()) {
            throw new IllegalArgumentException("delay must not be negative");
        }
    }

    public static RetryPolicy none() {
        return new RetryPolicy(1, Duration.ZERO);
    }

    public static RetryPolicy fixed(int maxAttempts, Duration delay) {
        return new RetryPolicy(maxAttempts, delay);
    }
}
