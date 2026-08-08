package io.yak.framework.workflow.engine.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class WorkflowCommandTest {

    @Test
    void nodeSuccessSnapshotsOutputWhenCommandIsCreated() {
        List<String> items = new ArrayList<>(List.of("a"));
        Map<String, Object> nested = new LinkedHashMap<>();
        nested.put("items", items);
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("data", nested);

        WorkflowCommand.NodeSucceeded command = new WorkflowCommand.NodeSucceeded(
                "exec-1", "node-1", "attempt-1", output);

        items.add("changed");
        nested.put("later", true);
        output.put("new", "value");

        assertEquals(
                Map.of("data", Map.of("items", List.of("a"))),
                command.output());
        assertThrows(
                UnsupportedOperationException.class,
                () -> command.output().put("extra", true));
    }
}
