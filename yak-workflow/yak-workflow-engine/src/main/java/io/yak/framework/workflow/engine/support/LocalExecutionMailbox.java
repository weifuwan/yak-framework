package io.yak.framework.workflow.engine.support;

import io.yak.framework.workflow.engine.command.WorkflowCommand;
import io.yak.framework.workflow.engine.execution.WorkflowExecution;
import io.yak.framework.workflow.engine.spi.ExecutionLock;
import io.yak.framework.workflow.engine.spi.ExecutionMailbox;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-process FIFO mailbox with one logical queue per workflow execution.
 *
 * <p>No worker thread is created. The first submitting caller drains the queue synchronously; callers
 * that arrive concurrently enqueue and wait for their own result. The configured {@link ExecutionLock}
 * still guards each command handler invocation, preserving compatibility with custom execution locks.
 */
public final class LocalExecutionMailbox implements ExecutionMailbox {

    private final ExecutionLock executionLock;
    private final ConcurrentMap<String, MailboxState> mailboxes = new ConcurrentHashMap<>();

    public LocalExecutionMailbox(ExecutionLock executionLock) {
        this.executionLock = Objects.requireNonNull(executionLock, "executionLock");
    }

    @Override
    public WorkflowExecution submit(WorkflowCommand command, WorkflowCommandHandler handler) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(handler, "handler");

        String executionId = command.executionId();
        MailboxState state = mailboxes.computeIfAbsent(executionId, ignored -> new MailboxState());
        Envelope envelope = new Envelope(command, handler);
        boolean shouldDrain = false;

        synchronized (state) {
            if (state.draining && state.owner == Thread.currentThread()) {
                throw new IllegalStateException(
                        "Reentrant command submission to the same execution mailbox is not supported: "
                                + executionId);
            }
            state.queue.addLast(envelope);
            if (!state.draining) {
                state.draining = true;
                state.owner = Thread.currentThread();
                shouldDrain = true;
            }
        }

        if (shouldDrain) {
            drain(executionId, state);
        }
        return await(envelope.result);
    }

    private void drain(String executionId, MailboxState state) {
        while (true) {
            Envelope envelope;
            synchronized (state) {
                envelope = state.queue.pollFirst();
                if (envelope == null) {
                    state.draining = false;
                    state.owner = null;
                    mailboxes.remove(executionId, state);
                    return;
                }
            }

            try {
                WorkflowExecution result = executionLock.execute(
                        executionId,
                        () -> envelope.handler.handle(envelope.command));
                envelope.result.complete(result);
            } catch (Throwable throwable) {
                envelope.result.completeExceptionally(throwable);
            }
        }
    }

    private WorkflowExecution await(CompletableFuture<WorkflowExecution> future) {
        try {
            return future.join();
        } catch (CompletionException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw exception;
        }
    }

    private static final class MailboxState {
        private final Deque<Envelope> queue = new ArrayDeque<>();
        private boolean draining;
        private Thread owner;
    }

    private static final class Envelope {
        private final WorkflowCommand command;
        private final WorkflowCommandHandler handler;
        private final CompletableFuture<WorkflowExecution> result = new CompletableFuture<>();

        private Envelope(WorkflowCommand command, WorkflowCommandHandler handler) {
            this.command = command;
            this.handler = handler;
        }
    }
}
