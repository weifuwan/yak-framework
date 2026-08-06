package io.yak.framework.workflow.engine.spi;

public interface NodeExecutor {

    void submit(NodeDispatch dispatch);

    default void cancel(NodeCancellation cancellation) {
        // Implement when the underlying executor supports cancellation.
    }
}
