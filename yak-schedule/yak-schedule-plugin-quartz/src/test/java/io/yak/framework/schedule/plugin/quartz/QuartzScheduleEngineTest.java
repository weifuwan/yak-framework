package io.yak.framework.schedule.plugin.quartz;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yak.framework.schedule.api.ConcurrencyPolicy;
import io.yak.framework.schedule.api.MisfirePolicy;
import io.yak.framework.schedule.api.ScheduleDefinition;
import io.yak.framework.schedule.api.ScheduleKey;
import io.yak.framework.schedule.api.SchedulePolicy;
import io.yak.framework.schedule.api.ScheduleStatus;
import io.yak.framework.schedule.api.ScheduleTarget;
import io.yak.framework.schedule.api.ScheduleTrigger;
import io.yak.framework.schedule.core.InMemoryScheduleEngineBindingRepository;
import java.time.ZoneId;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

class QuartzScheduleEngineTest {
    private Scheduler scheduler;

    @AfterEach
    void shutdown() throws Exception {
        if (scheduler != null) {
            scheduler.shutdown(true);
        }
    }

    @Test
    void savesCronScheduleAndUsesNamespaceAsQuartzGroup() throws Exception {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        QuartzScheduleEngine engine = new QuartzScheduleEngine(
                scheduler,
                new ObjectMapper().findAndRegisterModules(),
                new InMemoryScheduleEngineBindingRepository());

        ScheduleDefinition definition = new ScheduleDefinition(
                new ScheduleKey("workflow", "daily-report"),
                "daily workflow",
                ScheduleTrigger.cron("0 0 2 * * ?", ZoneId.of("Asia/Shanghai")),
                new ScheduleTarget("workflowScheduleHandler", Map.of("id", 1L)),
                new SchedulePolicy(
                        ConcurrencyPolicy.FORBID,
                        MisfirePolicy.FIRE_ONCE_NOW,
                        1),
                true,
                Map.of());

        assertThat(engine.save(definition).status())
                .isEqualTo(ScheduleStatus.ENABLED);
        assertThat(engine.get(definition.key()))
                .get()
                .extracting(item -> item.definition().target().handler())
                .isEqualTo("workflowScheduleHandler");
    }
}
