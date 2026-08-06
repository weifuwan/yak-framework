package io.yak.framework.workflow.engine.state;

public enum NodeExecutionStatus {
    WAITING(false),
    READY(false),
    SUBMITTED(false),
    RUNNING(false),
    SUCCESS(true),
    FAILED(true),
    UPSTREAM_FAILED(true),
    SKIPPED(true),
    CANCELED(true);

    private final boolean terminal;

    NodeExecutionStatus(boolean terminal) {
        this.terminal = terminal;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean isActive() {
        return this == READY || this == SUBMITTED || this == RUNNING;
    }
}
