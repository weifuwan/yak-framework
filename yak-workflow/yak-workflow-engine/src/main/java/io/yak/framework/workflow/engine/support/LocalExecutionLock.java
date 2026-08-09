package io.yak.framework.workflow.engine.support;

import io.yak.framework.workflow.engine.spi.ExecutionLock;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Bounded in-process execution lock using fixed lock stripes.
 *
 * <p>Execution ids are intentionally not retained forever. Different executions may share one
 * stripe, while commands for the same execution always resolve to the same lock.</p>
 */
public final class LocalExecutionLock implements ExecutionLock {

    private static final int DEFAULT_STRIPE_COUNT = 256;

    private final ReentrantLock[] stripes;

    public LocalExecutionLock() {
        this(DEFAULT_STRIPE_COUNT);
    }

    public LocalExecutionLock(int stripeCount) {
        if (stripeCount <= 0) {
            throw new IllegalArgumentException("stripeCount must be greater than zero");
        }
        this.stripes = new ReentrantLock[stripeCount];
        for (int index = 0; index < stripeCount; index++) {
            stripes[index] = new ReentrantLock();
        }
    }

    @Override
    public <T> T execute(String executionId, Supplier<T> action) {
        Objects.requireNonNull(executionId, "executionId");
        Objects.requireNonNull(action, "action");
        ReentrantLock lock = stripe(executionId);
        lock.lock();
        try {
            return action.get();
        } finally {
            lock.unlock();
        }
    }

    int stripeCount() {
        return stripes.length;
    }

    private ReentrantLock stripe(String executionId) {
        int hash = executionId.hashCode();
        hash ^= hash >>> 16;
        return stripes[Math.floorMod(hash, stripes.length)];
    }
}
