package io.yak.framework.schedule.plugin.xxljob;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.ScheduleTarget;
import io.yak.framework.schedule.api.ScheduleTrigger;
import io.yak.framework.schedule.core.InMemoryScheduleDefinitionRepository;
import io.yak.framework.schedule.core.InMemoryScheduleEngineBindingRepository;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class XxlJobScheduleEngineTest {

    @Test
    void createsAdminTaskAndStoresExternalBinding() {
        XxlJobPluginProperties properties = new XxlJobPluginProperties();
        properties.setAdminAddress("http://localhost:8080/xxl-job-admin");
        properties.setJobGroupId(1);
        RecordingClient client = new RecordingClient();
        InMemoryScheduleEngineBindingRepository bindings =
                new InMemoryScheduleEngineBindingRepository();
        InMemoryScheduleDefinitionRepository definitions =
                new InMemoryScheduleDefinitionRepository();
        XxlJobScheduleEngine engine = new XxlJobScheduleEngine(
                properties,
                client,
                bindings,
                definitions,
                new ObjectMapper().findAndRegisterModules());

        ScheduleDefinition definition = new ScheduleDefinition(
                new ScheduleKey("workflow", "nightly"),
                null,
                ScheduleTrigger.cron("0 0 2 * * ?", ZoneId.systemDefault()),
                new ScheduleTarget("workflowScheduleHandler", Map.of("id", 10L)),
                null,
                true,
                Map.of());
        definitions.save(definition);

        assertThat(engine.save(definition).externalId()).isEqualTo("100");
        assertThat(client.lastForm)
                .containsEntry("executorHandler", XxlJobScheduleEngine.HANDLER_NAME)
                .containsEntry("scheduleType", "CRON");
        assertThat(bindings.find(definition.key(), "xxl-job"))
                .get()
                .extracting(binding -> binding.externalId())
                .isEqualTo("100");
    }

    private static final class RecordingClient implements XxlJobAdminClient {
        private Map<String, String> lastForm = new LinkedHashMap<>();
        @Override public String create(Map<String, String> form) {
            lastForm = new LinkedHashMap<>(form);
            return "100";
        }
        @Override public void update(String id, Map<String, String> form) { lastForm = form; }
        @Override public void delete(String id) { }
        @Override public void pause(String id) { }
        @Override public void resume(String id) { }
        @Override public void trigger(String id, String executorParam) { }
        @Override public Optional<XxlJobAdminTask> find(String id, int jobGroupId) {
            return Optional.of(new XxlJobAdminTask(id, 1, 0, 1));
        }
    }
}
