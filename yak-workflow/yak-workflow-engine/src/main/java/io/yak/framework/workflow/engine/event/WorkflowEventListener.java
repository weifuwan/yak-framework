package io.yak.framework.workflow.engine.event;

@FunctionalInterface
public interface WorkflowEventListener {

    /**
     * Observe an execution event emitted while its command is being handled.
     *
     * <p>Listeners should remain observational and must not synchronously submit another command for
     * the same workflow execution. If an event should trigger follow-up workflow control, hand it off
     * to the host/runtime and submit that command after this callback returns.
     */
    void onEvent(WorkflowEvent event);

    static WorkflowEventListener noop() {
        return event -> {
        };
    }
}
