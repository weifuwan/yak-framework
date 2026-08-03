package io.yak.framework.schedule;

import io.yak.framework.schedule.api.ScheduleManager;
import io.yak.framework.schedule.api.model.MisfirePolicy;
import io.yak.framework.schedule.api.model.ScheduleDefinition;
import io.yak.framework.schedule.api.model.ScheduleKey;
import io.yak.framework.schedule.api.model.SchedulePolicy;
import io.yak.framework.schedule.api.model.ScheduleSnapshot;
import io.yak.framework.schedule.api.model.ScheduleTarget;
import io.yak.framework.schedule.api.model.ScheduleTrigger;
import io.yak.framework.schedule.core.DefaultScheduleManager;
import io.yak.framework.schedule.core.InMemoryScheduleEngineBindingRepository;
import io.yak.framework.schedule.core.ScheduleEngineRegistry;
import io.yak.framework.schedule.model.ConcurrencyPolicy;
import io.yak.framework.schedule.model.ScheduleExecutionLog;
import io.yak.framework.schedule.model.ScheduleOperationAudit;
import io.yak.framework.schedule.model.ScheduleTaskDefinition;
import io.yak.framework.schedule.provider.quartz.QuartzScheduleEngine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Backward-compatible facade for the original project/task API.
 *
 * <p>New applications should depend on {@link ScheduleManager}. This class
 * maps the original Cron-only model onto the provider-neutral core.</p>
 */
public class ScheduleTaskService {

    private final ScheduleManager scheduleManager;
    private final ScheduleExecutionLogRepository logRepository;
    private final ScheduleOperationAuditRepository auditRepository;

    public ScheduleTaskService(
            ScheduleManager scheduleManager,
            ScheduleExecutionLogRepository logRepository,
            ScheduleOperationAuditRepository auditRepository) {

        this.scheduleManager = scheduleManager;
        this.logRepository = logRepository;
        this.auditRepository = auditRepository;
    }

    /** Retains the original constructor for source compatibility and tests. */
    public ScheduleTaskService(
            Scheduler scheduler,
            CurrentOperatorProvider operatorProvider,
            ScheduleExecutionLogRepository logRepository,
            ScheduleOperationAuditRepository auditRepository) {

        this(
                new DefaultScheduleManager(
                        new ScheduleEngineRegistry(
                                Collections.singletonList(
                                        new QuartzScheduleEngine(scheduler))),
                        new InMemoryScheduleEngineBindingRepository(),
                        operatorProvider,
                        auditRepository,
                        QuartzScheduleEngine.ENGINE_TYPE),
                logRepository,
                auditRepository);
    }

    public void save(ScheduleTaskDefinition definition)
            throws SchedulerException {

        scheduleManager.save(toDefinition(definition));
    }

    public void pause(String project, String taskName)
            throws SchedulerException {

        scheduleManager.pause(key(project, taskName));
    }

    public void resume(String project, String taskName)
            throws SchedulerException {

        scheduleManager.resume(key(project, taskName));
    }

    public void runNow(String project, String taskName)
            throws SchedulerException {

        scheduleManager.runNow(key(project, taskName));
    }

    public void delete(String project, String taskName)
            throws SchedulerException {

        scheduleManager.delete(key(project, taskName));
    }

    public ScheduleTaskDefinition get(
            String project,
            String taskName)
            throws SchedulerException {

        return scheduleManager.get(key(project, taskName))
                .map(this::toLegacyDefinition)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown schedule task: "
                                + project
                                + "/"
                                + taskName));
    }

    public List<ScheduleTaskDefinition> list(String project)
            throws SchedulerException {

        List<ScheduleTaskDefinition> result = new ArrayList<>();

        for (ScheduleSnapshot snapshot
                : scheduleManager.list(project)) {
            result.add(toLegacyDefinition(snapshot));
        }

        return Collections.unmodifiableList(result);
    }

    public List<ScheduleExecutionLog> logs(
            String project,
            String taskName) {

        return logRepository.find(project, taskName);
    }

    public List<ScheduleOperationAudit> audits(
            String project,
            String taskName) {

        return auditRepository.find(project, taskName);
    }

    private ScheduleDefinition toDefinition(
            ScheduleTaskDefinition definition) {

        if (definition == null) {
            throw new IllegalArgumentException(
                    "definition must not be null");
        }

        ConcurrencyPolicy concurrencyPolicy =
                definition.getConcurrencyPolicy() == null
                        ? ConcurrencyPolicy.FORBID
                        : definition.getConcurrencyPolicy();

        return new ScheduleDefinition(
                key(
                        definition.getProject(),
                        definition.getName()),
                definition.getName(),
                definition.getDescription(),
                null,
                ScheduleTrigger.cron(
                        definition.getCron(),
                        definition.getZoneId()),
                new ScheduleTarget(
                        definition.getHandler(),
                        definition.getParameters()),
                new SchedulePolicy(
                        concurrencyPolicy,
                        MisfirePolicy.SMART,
                        definition.getMaxRetries()),
                true,
                null,
                Collections.emptyMap());
    }

    private ScheduleTaskDefinition toLegacyDefinition(
            ScheduleSnapshot snapshot) {

        ScheduleDefinition definition =
                snapshot.getDefinition();

        if (definition.getTrigger().getType()
                != io.yak.framework.schedule.api.model.TriggerType.CRON) {

            throw new IllegalStateException(
                    "Legacy ScheduleTaskService only supports Cron schedules");
        }

        return new ScheduleTaskDefinition(
                definition.getKey().getNamespace(),
                definition.getKey().getName(),
                definition.getDescription(),
                definition.getTarget().getHandler(),
                definition.getTrigger().getCron(),
                definition.getTrigger().getZoneId(),
                definition.getPolicy().getConcurrencyPolicy(),
                definition.getPolicy().getMaxTriggerRetries(),
                definition.getTarget().getPayload());
    }

    private static ScheduleKey key(
            String project,
            String taskName) {

        return new ScheduleKey(project, taskName);
    }
}
