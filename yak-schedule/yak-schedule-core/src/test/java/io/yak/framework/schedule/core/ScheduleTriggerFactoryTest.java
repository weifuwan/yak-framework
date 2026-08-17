package io.yak.framework.schedule.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.yak.framework.schedule.api.ScheduleTrigger;
import io.yak.framework.schedule.api.TriggerType;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class ScheduleTriggerFactoryTest {

    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    @Test
    void dailyShouldCreatePortableCronTrigger() {
        ScheduleTrigger trigger = ScheduleTrigger.daily(
                LocalTime.of(2, 30, 15), ZONE);

        assertThat(trigger.type()).isEqualTo(TriggerType.CRON);
        assertThat(trigger.expression()).isEqualTo("15 30 2 * * ?");
        assertThat(trigger.zoneId()).isEqualTo(ZONE);
    }

    @Test
    void weeklyShouldCreatePortableCronTrigger() {
        ScheduleTrigger trigger = ScheduleTrigger.weekly(
                DayOfWeek.MONDAY,
                LocalTime.of(3, 5),
                ZONE);

        assertThat(trigger.type()).isEqualTo(TriggerType.CRON);
        assertThat(trigger.expression()).isEqualTo("0 5 3 ? * MON");
        assertThat(trigger.zoneId()).isEqualTo(ZONE);
    }
}
