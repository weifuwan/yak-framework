package io.yak.framework.workflow.engine.spi;

@FunctionalInterface
public interface IdGenerator {

    String nextId();
}
