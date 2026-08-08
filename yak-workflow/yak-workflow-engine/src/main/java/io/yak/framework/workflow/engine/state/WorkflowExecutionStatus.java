package io.yak.framework.workflow.engine.state;

public enum WorkflowExecutionStatus {
    CREATED(false),
    RUNNING(false),
    PAUSING(false),
    PAUSED(false),
    RESUMING(false),
    SUCCESS(true),
    SUCCESS_WITH_WARNINGS(true),
    FAILED(true),
    CANCELED(true),
    TIMED_OUT(true);

    private final boolean terminal;

    WorkflowExecutionStatus(boolean terminal) {
        this.terminal = terminal;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean isPauseLifecycle() {
        return this == PAUSING || this == PAUSED || this == RESUMING;
    }
}
