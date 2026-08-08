package io.yak.framework.workflow.engine.state;

public enum WorkflowExecutionStatus {
    CREATED(false),
    RUNNING(false),
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
}
