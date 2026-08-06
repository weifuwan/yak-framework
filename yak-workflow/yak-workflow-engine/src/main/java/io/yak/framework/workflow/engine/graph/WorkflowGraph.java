package io.yak.framework.workflow.engine.graph;

import java.util.List;
import java.util.Set;

public interface WorkflowGraph {

    Set<String> nodes();

    Set<String> predecessors(String nodeId);

    Set<String> successors(String nodeId);

    Set<String> startNodes();

    Set<String> endNodes();

    Set<String> ancestors(String nodeId);

    Set<String> descendants(String nodeId);

    List<String> topologicalSort();

    boolean hasCycle();
}
