package io.yak.framework.workflow.engine.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.yak.framework.workflow.engine.definition.EdgeDefinition;
import io.yak.framework.workflow.engine.definition.NodeDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowDefinition;
import io.yak.framework.workflow.engine.definition.WorkflowFailureStrategy;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorkflowDefinitionValidatorTest {

    private final WorkflowDefinitionValidator validator = new WorkflowDefinitionValidator();
    private final WorkflowGraphBuilder graphBuilder = new WorkflowGraphBuilder();

    @Test
    void rejectsCycle() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "cycle",
                "cycle",
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                List.of(NodeDefinition.task("a"), NodeDefinition.task("b")),
                List.of(new EdgeDefinition("a", "b"), new EdgeDefinition("b", "a")));

        WorkflowValidationException exception = assertThrows(
                WorkflowValidationException.class, () -> validator.validate(definition));

        assertTrue(exception.getMessage().contains("cycle"));
    }

    @Test
    void sortsDagTopologically() {
        WorkflowDefinition definition = new WorkflowDefinition(
                "dag",
                "dag",
                WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
                List.of(
                        NodeDefinition.task("a"),
                        NodeDefinition.task("b"),
                        NodeDefinition.task("c"),
                        NodeDefinition.task("d")),
                List.of(
                        new EdgeDefinition("a", "b"),
                        new EdgeDefinition("a", "c"),
                        new EdgeDefinition("b", "d"),
                        new EdgeDefinition("c", "d")));

        validator.validate(definition);
        WorkflowGraph graph = graphBuilder.build(definition);

        assertEquals(List.of("a"), graph.startNodes().stream().toList());
        assertEquals("a", graph.topologicalSort().get(0));
        assertEquals("d", graph.topologicalSort().get(3));
    }
}
