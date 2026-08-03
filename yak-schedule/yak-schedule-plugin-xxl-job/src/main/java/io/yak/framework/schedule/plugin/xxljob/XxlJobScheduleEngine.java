package io.yak.framework.schedule.plugin.xxljob;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yak.framework.schedule.api.ConcurrencyPolicy;
import io.yak.framework.schedule.api.MisfirePolicy;
import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleDefinitionRepository;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleEngineBinding;
import io.yak.framework.schedule.api.ScheduleEngineBindingRepository;
import io.yak.framework.schedule.api.ScheduleEngineCapabilities;
import io.yak.framework.schedule.api.ScheduleEngineTypes;
import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.ScheduleNotFoundException;
import io.yak.framework.schedule.api.ScheduleProviderException;
import io.yak.framework.schedule.api.ScheduleSnapshot;
import io.yak.framework.schedule.api.ScheduleStatus;
import io.yak.framework.schedule.api.ScheduleTriggerResult;
import io.yak.framework.schedule.api.TriggerType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** XXL-JOB 调度引擎插件。 */
public final class XxlJobScheduleEngine implements ScheduleEngine {
    public static final String HANDLER_NAME = "yakScheduleHandler";

    private final XxlJobPluginProperties properties;
    private final XxlJobAdminClient adminClient;
    private final ScheduleEngineBindingRepository bindingRepository;
    private final ScheduleDefinitionRepository definitionRepository;
    private final ObjectMapper objectMapper;
    private final ScheduleEngineCapabilities capabilities =
            new ScheduleEngineCapabilities(
                    EnumSet.of(TriggerType.CRON),
                    EnumSet.of(ConcurrencyPolicy.FORBID),
                    EnumSet.allOf(MisfirePolicy.class),
                    true,
                    true,
                    false,
                    true);

    public XxlJobScheduleEngine(
            XxlJobPluginProperties properties,
            XxlJobAdminClient adminClient,
            ScheduleEngineBindingRepository bindingRepository,
            ScheduleDefinitionRepository definitionRepository,
            ObjectMapper objectMapper) {
        this.properties = properties;
        this.adminClient = adminClient;
        this.bindingRepository = bindingRepository;
        this.definitionRepository = definitionRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public String type() {
        return ScheduleEngineTypes.XXL_JOB;
    }

    @Override
    public ScheduleEngineCapabilities capabilities() {
        return capabilities;
    }

    @Override
    public ScheduleSnapshot save(ScheduleDefinition definition) {
        Map<String, String> form = jobForm(definition, null);
        Optional<ScheduleEngineBinding> existing = bindingRepository.find(
                definition.key(), type());
        String externalId;
        if (existing.isPresent()) {
            externalId = existing.get().externalId();
            adminClient.update(externalId, form);
        } else {
            externalId = adminClient.create(form);
            if (externalId == null || externalId.isBlank()) {
                throw new ScheduleProviderException(
                        "XXL-JOB Admin returned an empty job id for "
                                + definition.key().value());
            }
        }
        bindingRepository.save(new ScheduleEngineBinding(
                definition.key(),
                type(),
                externalId,
                Map.of("jobGroupId", String.valueOf(properties.getJobGroupId()))));
        if (definition.enabled()) {
            adminClient.resume(externalId);
        } else {
            adminClient.pause(externalId);
        }
        return snapshot(definition, externalId);
    }

    @Override
    public void pause(ScheduleKey key) {
        adminClient.pause(requiredBinding(key).externalId());
    }

    @Override
    public void resume(ScheduleKey key) {
        adminClient.resume(requiredBinding(key).externalId());
    }

    @Override
    public void delete(ScheduleKey key) {
        ScheduleEngineBinding binding = requiredBinding(key);
        adminClient.delete(binding.externalId());
        bindingRepository.delete(key, type());
    }

    @Override
    public ScheduleTriggerResult runNow(ScheduleKey key) {
        ScheduleDefinition definition = definitionRepository.find(key)
                .orElseThrow(() -> new ScheduleNotFoundException(key));
        ScheduleEngineBinding binding = requiredBinding(key);
        String triggerId = UUID.randomUUID().toString();
        adminClient.trigger(
                binding.externalId(),
                payload(definition, triggerId));
        return new ScheduleTriggerResult(
                triggerId,
                binding.externalId(),
                Instant.now());
    }

    @Override
    public Optional<ScheduleSnapshot> get(ScheduleKey key) {
        Optional<ScheduleDefinition> definition = definitionRepository.find(key);
        Optional<ScheduleEngineBinding> binding = bindingRepository.find(key, type());
        if (definition.isEmpty() || binding.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(snapshot(
                definition.get(), binding.get().externalId()));
    }

    @Override
    public List<ScheduleSnapshot> list(String namespace) {
        List<ScheduleSnapshot> result = new ArrayList<>();
        for (ScheduleDefinition definition
                : definitionRepository.findByNamespace(namespace)) {
            get(definition.key()).ifPresent(result::add);
        }
        result.sort(Comparator.comparing(
                item -> item.definition().key().name()));
        return List.copyOf(result);
    }

    private ScheduleSnapshot snapshot(
            ScheduleDefinition definition,
            String externalId) {
        Optional<XxlJobAdminTask> task = adminClient.find(
                externalId,
                properties.getJobGroupId());
        if (task.isEmpty()) {
            throw new ScheduleNotFoundException(definition.key());
        }
        XxlJobAdminTask value = task.get();
        return new ScheduleSnapshot(
                definition,
                type(),
                externalId,
                value.triggerStatus() == 1
                        ? ScheduleStatus.ENABLED
                        : ScheduleStatus.PAUSED,
                instant(value.triggerNextTime()),
                instant(value.triggerLastTime()));
    }

    private Map<String, String> jobForm(
            ScheduleDefinition definition,
            String triggerId) {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("jobGroup", String.valueOf(properties.getJobGroupId()));
        form.put("name", definition.key().value());
        form.put("author", value(properties.getAuthor()));
        form.put("alarmEmail", value(properties.getAlarmEmail()));
        form.put("scheduleType", "CRON");
        form.put("scheduleConf", definition.trigger().expression());
        form.put(
                "misfireStrategy",
                definition.policy().misfirePolicy() == MisfirePolicy.IGNORE
                        ? "DO_NOTHING"
                        : "FIRE_ONCE_NOW");
        form.put("executorRouteStrategy", value(properties.getRouteStrategy()));
        form.put("executorHandler", HANDLER_NAME);
        form.put("executorParam", payload(definition, triggerId));
        form.put("executorBlockStrategy", "DISCARD_LATER");
        form.put("executorTimeout", "0");
        form.put(
                "executorFailRetryCount",
                String.valueOf(definition.policy().triggerRetries()));
        form.put("glueType", "BEAN");
        form.put("glueSource", "");
        form.put("glueRemark", "GLUE代码初始化");
        form.put("childJobId", "");
        return form;
    }

    private String payload(
            ScheduleDefinition definition,
            String triggerId) {
        try {
            return objectMapper.writeValueAsString(
                    new XxlJobDispatchPayload(
                            triggerId,
                            definition.key(),
                            definition.target()));
        } catch (Exception exception) {
            throw new ScheduleProviderException(
                    "Serialize XXL-JOB executor parameter failed: "
                            + definition.key().value(),
                    exception);
        }
    }

    private ScheduleEngineBinding requiredBinding(ScheduleKey key) {
        return bindingRepository.find(key, type())
                .orElseThrow(() -> new ScheduleNotFoundException(key));
    }

    private static Instant instant(long epochMillis) {
        return epochMillis <= 0 ? null : Instant.ofEpochMilli(epochMillis);
    }

    private static String value(String value) {
        return value == null ? "" : value;
    }
}
