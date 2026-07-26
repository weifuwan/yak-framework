package io.yak.framework.schedule;

import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = QuartzAutoConfiguration.class)
@ConditionalOnClass(Scheduler.class)
@ConditionalOnProperty(prefix = "yak.schedule", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(YakScheduleProperties.class)
public class YakScheduleAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    CurrentOperatorProvider currentOperatorProvider() {
        return () -> "SYSTEM";
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleExecutionLogRepository scheduleExecutionLogRepository(YakScheduleProperties properties) {
        return new InMemoryScheduleExecutionLogRepository(properties.getLogCapacity());
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleOperationAuditRepository scheduleOperationAuditRepository(YakScheduleProperties properties) {
        return new InMemoryScheduleOperationAuditRepository(properties.getLogCapacity());
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleTaskService scheduleTaskService(Scheduler scheduler, CurrentOperatorProvider operatorProvider,
                                            ScheduleExecutionLogRepository logRepository, ScheduleOperationAuditRepository auditRepository) {
        return new ScheduleTaskService(scheduler, operatorProvider, logRepository, auditRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(prefix = "yak.schedule", name = "web-enabled", havingValue = "true", matchIfMissing = true)
    ScheduleTaskController scheduleTaskController(ScheduleTaskService service) {
        return new ScheduleTaskController(service);
    }
}
