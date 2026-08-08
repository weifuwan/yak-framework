package io.yak.framework.workflow.engine.state;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class WorkflowStateMachine {

    private static final Map<WorkflowExecutionStatus, Set<WorkflowExecutionStatus>> TRANSITIONS =
            new EnumMap<>(WorkflowExecutionStatus.class);

    static {
        allow(WorkflowExecutionStatus.CREATED,
                WorkflowExecutionStatus.RUNNING,
                WorkflowExecutionStatus.CANCELED,
                WorkflowExecutionStatus.TIMED_OUT);
        allow(WorkflowExecutionStatus.RUNNING,
                WorkflowExecutionStatus.SUCCESS,
                WorkflowExecutionStatus.SUCCESS_WITH_WARNINGS,
                WorkflowExecutionStatus.FAILED,
                WorkflowExecutionStatus.CANCELED,
                WorkflowExecutionStatus.TIMED_OUT);
        allow(WorkflowExecutionStatus.FAILED, WorkflowExecutionStatus.RUNNING);
        allow(WorkflowExecutionStatus.SUCCESS_WITH_WARNINGS, WorkflowExecutionStatus.RUNNING);
        allow(WorkflowExecutionStatus.CANCELED, WorkflowExecutionStatus.RUNNING);
        allow(WorkflowExecutionStatus.TIMED_OUT, WorkflowExecutionStatus.RUNNING);
    }

    private WorkflowStateMachine() {
    }

    public static void requireTransition(
            WorkflowExecutionStatus current, WorkflowExecutionStatus target) {
        if (current == target) {
            return;
        }
        if (!TRANSITIONS.getOrDefault(current, Set.of()).contains(target)) {
            throw new IllegalStateException(
                    "Invalid workflow state transition: " + current + " -> " + target);
        }
    }

    private static void allow(
            WorkflowExecutionStatus source, WorkflowExecutionStatus... targets) {
        TRANSITIONS.put(source, EnumSet.of(targets[0], targets));
    }
}
