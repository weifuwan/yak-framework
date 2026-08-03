package io.yak.framework.schedule.provider.quartz;

import static org.assertj.core.api.Assertions.assertThat;

import io.yak.framework.schedule.api.model.ScheduleDefinition;
import io.yak.framework.schedule.api.model.ScheduleKey;
import io.yak.framework.schedule.api.model.SchedulePolicy;
import io.yak.framework.schedule.api.model.ScheduleStatus;
import io.yak.framework.schedule.api.model.ScheduleTarget;
import io.yak.framework.schedule.api.model.ScheduleTrigger;
import io.yak.framework.schedule.api.model.TriggerType;
import java.time.Instant;
import java.util.Collections;
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
    void savesAndReadsOneTimeDefinition() throws Exception {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        QuartzScheduleEngine engine =
                new QuartzScheduleEngine(scheduler);
        ScheduleKey key =
                new ScheduleKey("maintenance", "cleanup-once");

        engine.save(
                new ScheduleDefinition(
                        key,
                        "清理历史文件",
                        null,
                        "quartz",
                        ScheduleTrigger.oneTime(
                                Instant.now().plusSeconds(300)),
                        new ScheduleTarget(
                                "cleanupHandler",
                                Map.of("retentionDays", "30")),
                        SchedulePolicy.defaults(),
                        true,
                        null,
                        Collections.emptyMap()));

        assertThat(engine.get(key)).isPresent();
        assertThat(engine.get(key).get()
                .getDefinition()
                .getTrigger()
                .getType())
                .isEqualTo(TriggerType.ONE_TIME);

        engine.pause(key);
        assertThat(engine.get(key).get().getStatus())
                .isEqualTo(ScheduleStatus.PAUSED);

        engine.delete(key);
        assertThat(engine.get(key)).isEmpty();
    }
}
