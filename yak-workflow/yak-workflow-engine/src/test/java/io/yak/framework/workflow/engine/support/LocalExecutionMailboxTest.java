package io.yak.framework.workflow.engine.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.yak.framework.workflow.engine.command.WorkflowCommand;
import io.yak.framework.workflow.engine.spi.ExecutionMailbox;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class LocalExecutionMailboxTest {

    @Test
    void serializesCommandsForSameExecutionAndDrainsQueuedCommandAfterFailure() throws Exception {
        ExecutionMailbox mailbox = new LocalExecutionMailbox(new LocalExecutionLock());
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch firstEntered = new CountDownLatch(1);
        CountDownLatch secondSubmitting = new CountDownLatch(1);
        List<String> order = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger activeHandlers = new AtomicInteger();
        AtomicInteger maxActiveHandlers = new AtomicInteger();

        try {
            Future<?> first = pool.submit(() -> mailbox.submit(
                    new WorkflowCommand.PauseWorkflow("exec-1", "first"),
                    command -> {
                        int active = activeHandlers.incrementAndGet();
                        maxActiveHandlers.accumulateAndGet(active, Math::max);
                        order.add("first-start");
                        firstEntered.countDown();
                        await(secondSubmitting);
                        order.add("first-fail");
                        activeHandlers.decrementAndGet();
                        throw new IllegalStateException("boom");
                    }));

            assertTrue(firstEntered.await(2, TimeUnit.SECONDS));
            Future<?> second = pool.submit(() -> {
                secondSubmitting.countDown();
                return mailbox.submit(
                        new WorkflowCommand.ResumeWorkflow("exec-1"),
                        command -> {
                            int active = activeHandlers.incrementAndGet();
                            maxActiveHandlers.accumulateAndGet(active, Math::max);
                            order.add("second");
                            activeHandlers.decrementAndGet();
                            return null;
                        });
            });

            ExecutionException failure = assertThrows(
                    ExecutionException.class,
                    () -> first.get(2, TimeUnit.SECONDS));
            assertTrue(failure.getCause() instanceof IllegalStateException);
            second.get(2, TimeUnit.SECONDS);

            assertEquals(List.of("first-start", "first-fail", "second"), order);
            assertEquals(1, maxActiveHandlers.get());
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void differentExecutionsCanDrainIndependently() throws Exception {
        ExecutionMailbox mailbox = new LocalExecutionMailbox(new LocalExecutionLock());
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch bothInside = new CountDownLatch(2);

        try {
            Future<?> first = pool.submit(() -> mailbox.submit(
                    new WorkflowCommand.CheckTimeouts("exec-a"),
                    command -> {
                        bothInside.countDown();
                        await(bothInside);
                        return null;
                    }));
            Future<?> second = pool.submit(() -> mailbox.submit(
                    new WorkflowCommand.CheckTimeouts("exec-b"),
                    command -> {
                        bothInside.countDown();
                        await(bothInside);
                        return null;
                    }));

            first.get(2, TimeUnit.SECONDS);
            second.get(2, TimeUnit.SECONDS);
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void rejectsSynchronousReentrantSubmissionForSameExecution() {
        ExecutionMailbox mailbox = new LocalExecutionMailbox(new LocalExecutionLock());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> mailbox.submit(
                        new WorkflowCommand.CheckTimeouts("exec-reentrant"),
                        command -> mailbox.submit(
                                new WorkflowCommand.CancelWorkflow("exec-reentrant", "nested"),
                                nested -> null)));

        assertTrue(exception.getMessage().contains("Reentrant command submission"));
    }

    private static void await(CountDownLatch latch) {
        try {
            if (!latch.await(Duration.ofSeconds(2).toMillis(), TimeUnit.MILLISECONDS)) {
                throw new IllegalStateException("Timed out waiting for test latch");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exception);
        }
    }
}
