package io.yak.framework.workflow.engine.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LocalExecutionLockTest {

    @Test
    void keepsFixedNumberOfLockStripes() {
        LocalExecutionLock lock = new LocalExecutionLock(32);

        for (int index = 0; index < 10_000; index++) {
            String executionId = "execution-" + index;
            assertEquals(executionId, lock.execute(executionId, () -> executionId));
        }

        assertEquals(32, lock.stripeCount());
    }

    @Test
    void rejectsInvalidStripeCount() {
        assertThrows(IllegalArgumentException.class, () -> new LocalExecutionLock(0));
    }
}
