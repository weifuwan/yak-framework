package io.yak.framework.workflow.engine.spi;

import java.util.function.Supplier;

@FunctionalInterface
public interface ExecutionLock {

    <T> T execute(String executionId, Supplier<T> action);
}
