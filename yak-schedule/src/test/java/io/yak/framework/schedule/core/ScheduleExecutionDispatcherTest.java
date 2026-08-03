package io.yak.framework.schedule.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.yak.framework.schedule.ScheduleTaskHandler;
import io.yak.framework.schedule.api.ScheduleExecutionContext;
import io.yak.framework.schedule.api.ScheduleExecutionResult;
import io.yak.framework.schedule.api.ScheduleHandler;
import io.yak.framework.schedule.api.model.ScheduleKey;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class ScheduleExecutionDispatcherTest {

    @Test
    void dispatchesProviderNeutralHandler() throws Exception {
        ApplicationContext applicationContext =
                mock(ApplicationContext.class);
        ScheduleHandler handler = context ->
                ScheduleExecutionResult.accepted(
                        "workflow-instance-100");
        when(applicationContext.containsBean("workflowHandler"))
                .thenReturn(true);
        when(applicationContext.getBean("workflowHandler"))
                .thenReturn(handler);

        ScheduleExecutionResult result =
                new ScheduleExecutionDispatcher(applicationContext)
                        .dispatch(context("workflowHandler"));

        assertThat(result.isAccepted()).isTrue();
        assertThat(result.getBusinessExecutionId())
                .isEqualTo("workflow-instance-100");
    }

    @Test
    void supportsLegacyStringParameterHandler() throws Exception {
        ApplicationContext applicationContext =
                mock(ApplicationContext.class);
        String[] received = new String[1];
        ScheduleTaskHandler handler =
                parameters ->
                        received[0] =
                                parameters.get("definitionId");
        when(applicationContext.containsBean("legacyHandler"))
                .thenReturn(true);
        when(applicationContext.getBean("legacyHandler"))
                .thenReturn(handler);

        new ScheduleExecutionDispatcher(applicationContext)
                .dispatch(context("legacyHandler"));

        assertThat(received[0]).isEqualTo("10001");
    }

    private ScheduleExecutionContext context(String handler) {
        return new ScheduleExecutionContext(
                "trigger-1",
                new ScheduleKey("offline-sync", "job-10001"),
                handler,
                Instant.now(),
                Instant.now(),
                false,
                1,
                Map.of("definitionId", "10001"),
                Collections.emptyMap());
    }
}
