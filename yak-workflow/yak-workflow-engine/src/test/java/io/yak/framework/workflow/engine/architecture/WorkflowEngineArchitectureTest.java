package io.yak.framework.workflow.engine.architecture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class WorkflowEngineArchitectureTest {

    @Test
    void publicApiFacadesStayThin() throws IOException {
        String engine = source("api/DefaultWorkflowEngine.java");
        String recovery = source("api/WorkflowRecoveryCoordinator.java");

        assertTrue(engine.length() < 9000, "DefaultWorkflowEngine must remain a thin facade");
        assertTrue(recovery.length() < 5000, "WorkflowRecoveryCoordinator must remain a thin facade");
        assertFalse(engine.contains("DefaultWorkflowScheduler"));
        assertFalse(engine.contains("DefaultFailurePropagationPolicy"));
        assertFalse(engine.contains("handleNodeSucceeded"));
        assertFalse(recovery.contains("DefaultWorkflowScheduler"));
        assertFalse(recovery.contains("NodeInputResolver"));
    }

    @Test
    void internalRuntimeDoesNotDependBackOnApi() throws IOException {
        for (String folder : List.of("runtime", "recovery")) {
            Path root = productionRoot().resolve(folder);
            try (Stream<Path> files = Files.walk(root)) {
                for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                    String source = Files.readString(file, StandardCharsets.UTF_8);
                    assertFalse(
                            source.contains("io.yak.framework.workflow.engine.api."),
                            file + " must not depend back on public api package");
                }
            }
        }
    }

    @Test
    void lowerLevelPackagesDoNotDependOnRuntimeOrRecovery() throws IOException {
        for (String folder : List.of(
                "command", "definition", "event", "execution", "graph", "policy", "scheduler", "spi", "state")) {
            Path root = productionRoot().resolve(folder);
            try (Stream<Path> files = Files.walk(root)) {
                for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                    String source = Files.readString(file, StandardCharsets.UTF_8);
                    assertFalse(
                            source.contains("io.yak.framework.workflow.engine.runtime."),
                            file + " must not depend upward on runtime");
                    assertFalse(
                            source.contains("io.yak.framework.workflow.engine.recovery."),
                            file + " must not depend upward on recovery");
                    assertFalse(
                            source.contains("io.yak.framework.workflow.engine.api."),
                            file + " must not depend upward on api");
                }
            }
        }
    }

    @Test
    void engineCoreRemainsFrameworkFree() throws IOException {
        try (Stream<Path> files = Files.walk(productionRoot())) {
            for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                assertFalse(source.contains("org.springframework."), file + " must stay Spring-free");
                assertFalse(source.contains("jakarta.persistence."), file + " must stay persistence-framework-free");
                assertFalse(source.contains("com.baomidou.mybatisplus."), file + " must stay MyBatis-free");
            }
        }
    }

    @Test
    void broadImplementationBucketsCannotReturn() {
        Path root = productionRoot();
        for (String forbidden : List.of(
                "service", "helper", "helpers", "utils", "util", "common", "base")) {
            assertFalse(Files.exists(root.resolve(forbidden)), "forbidden broad package: " + forbidden);
        }
    }

    private String source(String relative) throws IOException {
        return Files.readString(productionRoot().resolve(relative), StandardCharsets.UTF_8);
    }

    private Path productionRoot() {
        Path local = Path.of("src/main/java/io/yak/framework/workflow/engine");
        if (Files.isDirectory(local)) {
            return local;
        }
        Path repository = Path.of(
                "yak-workflow",
                "yak-workflow-engine",
                "src",
                "main",
                "java",
                "io",
                "yak",
                "framework",
                "workflow",
                "engine");
        assertTrue(Files.isDirectory(repository), "Unable to locate workflow engine source root");
        return repository;
    }
}
