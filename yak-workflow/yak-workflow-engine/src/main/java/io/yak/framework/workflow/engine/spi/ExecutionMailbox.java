package io.yak.framework.workflow.engine.spi;

import io.yak.framework.workflow.engine.command.WorkflowCommand;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;

/**
 * Serial command channel for one workflow execution.
 *
 * <p>The mailbox owns ordering/serialization only. It does not create background threads and it does
 * not persist commands. A host may replace this SPI later with a durable or distributed mailbox while
 * keeping the engine command model unchanged.
 */
public interface ExecutionMailbox {

    WorkflowExecution submit(WorkflowCommand command, WorkflowCommandHandler handler);

    @FunctionalInterface
    interface WorkflowCommandHandler {
        WorkflowExecution handle(WorkflowCommand command);
    }
}
