package io.yak.framework.workflow.engine.model;

/**
 * A node in a workflow definition.
 *
 * @param code stable node identifier within one workflow
 * @param type node behavior type
 */
public record WorkflowNode(String code, WorkflowNodeType type) {}
