package io.yak.framework.workflow.engine.state;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class NodeStateMachine {

    private static final Map<NodeExecutionStatus, Set<NodeExecutionStatus>> TRANSITIONS =
            new EnumMap<>(NodeExecutionStatus.class);

    static {
        allow(NodeExecutionStatus.WAITING,
                NodeExecutionStatus.READY,
                NodeExecutionStatus.SUCCESS,
                NodeExecutionStatus.SKIPPED,
                NodeExecutionStatus.UPSTREAM_FAILED,
                NodeExecutionStatus.CANCELED);
        allow(NodeExecutionStatus.READY,
                NodeExecutionStatus.SUBMITTED,
                NodeExecutionStatus.UPSTREAM_FAILED,
                NodeExecutionStatus.CANCELED);
        allow(NodeExecutionStatus.SUBMITTED,
                NodeExecutionStatus.RUNNING,
                NodeExecutionStatus.SUCCESS,
                NodeExecutionStatus.FAILED,
                NodeExecutionStatus.CANCELED);
        allow(NodeExecutionStatus.RUNNING,
                NodeExecutionStatus.SUCCESS,
                NodeExecutionStatus.FAILED,
                NodeExecutionStatus.CANCELED);
        allow(NodeExecutionStatus.FAILED, NodeExecutionStatus.WAITING, NodeExecutionStatus.READY);
        allow(NodeExecutionStatus.UPSTREAM_FAILED, NodeExecutionStatus.WAITING);
        allow(NodeExecutionStatus.SKIPPED, NodeExecutionStatus.WAITING);
        allow(NodeExecutionStatus.CANCELED, NodeExecutionStatus.WAITING);
    }

    private NodeStateMachine() {
    }

    public static void requireTransition(
            NodeExecutionStatus current, NodeExecutionStatus target) {
        if (current == target) {
            return;
        }
        if (!TRANSITIONS.getOrDefault(current, Set.of()).contains(target)) {
            throw new IllegalStateException(
                    "Invalid node state transition: " + current + " -> " + target);
        }
    }

    private static void allow(NodeExecutionStatus source, NodeExecutionStatus... targets) {
        TRANSITIONS.put(source, EnumSet.of(targets[0], targets));
    }
}
