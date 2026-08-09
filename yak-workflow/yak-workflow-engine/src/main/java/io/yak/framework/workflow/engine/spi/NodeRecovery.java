package io.yak.framework.workflow.engine.spi;

import io.yak.framework.workflow.engine.state.NodeAttemptStatus;
import io.yak.framework.workflow.engine.state.NodeExecutionStatus;
import io.yak.framework.workflow.engine.state.WorkflowExecutionStatus;
import java.util.Objects;

/**
 * Recovery context for an already persisted concrete node attempt.
 *
 * <p>The attempt identity is never regenerated during recovery. Hosts may use the attempt id carried
 * by {@link #dispatch()} as an idempotency key when reconciling external executors.</p>
 */
public record NodeRecovery(
        NodeDispatch dispatch,
        WorkflowExecutionStatus workflowStatus,
        NodeExecutionStatus nodeStatus,
        NodeAttemptStatus attemptStatus) {

    public NodeRecovery {
        dispatch = Objects.requireNonNull(dispatch, "dispatch");
        workflowStatus = Objects.requireNonNull(workflowStatus, "workflowStatus");
        nodeStatus = Objects.requireNonNull(nodeStatus, "nodeStatus");
        attemptStatus = Objects.requireNonNull(attemptStatus, "attemptStatus");
    }
}
