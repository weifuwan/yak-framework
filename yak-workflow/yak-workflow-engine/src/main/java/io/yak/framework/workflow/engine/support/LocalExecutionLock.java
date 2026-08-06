package io.yak.framework.workflow.engine.support;

import io.yak.framework.workflow.engine.spi.ExecutionLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public final class LocalExecutionLock implements ExecutionLock {

    private final ConcurrentMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    @Override
    public <T> T execute(String executionId, Supplier<T> action) {
        ReentrantLock lock = locks.computeIfAbsent(executionId, ignored -> new ReentrantLock());
        lock.lock();
        try {
            return action.get();
        } finally {
            lock.unlock();
        }
    }
}
