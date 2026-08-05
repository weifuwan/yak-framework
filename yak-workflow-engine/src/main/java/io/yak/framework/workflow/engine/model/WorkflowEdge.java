package io.yak.framework.workflow.engine.model;

/**
 * A directed dependency between two workflow nodes.
 *
 * @param sourceCode upstream node code
 * @param targetCode downstream node code
 */
public record WorkflowEdge(String sourceCode, String targetCode) {}
