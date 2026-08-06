package io.yak.framework.workflow.engine.definition;

/** Defines how direct predecessor states activate a node. */
public enum TriggerRule {
    ALL_SUCCESS,
    ALL_DONE,
    NONE_FAILED,
    ONE_SUCCESS,
    ALWAYS
}
