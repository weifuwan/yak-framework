package io.yak.framework.workflow.engine.spi;

import java.time.Instant;
import java.util.Map;

public record NodeDispatch(
        String workflowExecutionId,
        String nodeExecutionId,
        String nodeId,
        String attemptId,
        int attemptNumber,
        Instant availableAt,
        Map<String, Object> workflowInput,
        Map<String, Object> nodeConfiguration) {
}
