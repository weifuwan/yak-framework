package io.yak.framework.workflow.engine.event;

@FunctionalInterface
public interface WorkflowEventListener {

    void onEvent(WorkflowEvent event);

    static WorkflowEventListener noop() {
        return event -> {
        };
    }
}
