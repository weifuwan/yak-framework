package io.yak.framework.schedule;

import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleManager;
import io.yak.framework.schedule.core.DefaultScheduleManager;
import io.yak.framework.schedule.core.InMemoryScheduleEngineBindingRepository;
import io.yak.framework.schedule.core.ScheduleEngineBindingRepository;
import io.yak.framework.schedule.core.ScheduleEngineRegistry;
import io.yak.framework.schedule.core.ScheduleExecutionDispatcher;
import io.yak.framework.schedule.provider.quartz.AutowiringSpringBeanJobFactory;
import io.yak.framework.schedule.provider.quartz.QuartzScheduleEngine;
import java.util.List;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = QuartzAutoConfiguration.class)
@ConditionalOnClass(Scheduler.class)
@ConditionalOnProperty(
        prefix = "yak.schedule",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties(YakScheduleProperties.class)
public class YakScheduleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    CurrentOperatorProvider currentOperatorProvider() {
        return () -> "SYSTEM";
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleExecutionLogRepository scheduleExecutionLogRepository(
            YakScheduleProperties properties) {

        return new InMemoryScheduleExecutionLogRepository(
                properties.getLogCapacity());
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleOperationAuditRepository scheduleOperationAuditRepository(
            YakScheduleProperties properties) {

        return new InMemoryScheduleOperationAuditRepository(
                properties.getLogCapacity());
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleExecutionDispatcher scheduleExecutionDispatcher(
            ApplicationContext applicationContext) {

        return new ScheduleExecutionDispatcher(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean(name = "yakScheduleJobFactoryCustomizer")
    SchedulerFactoryBeanCustomizer yakScheduleJobFactoryCustomizer(
            ApplicationContext applicationContext) {

        return schedulerFactoryBean ->
                schedulerFactoryBean.setJobFactory(
                        new AutowiringSpringBeanJobFactory(
                                applicationContext
                                        .getAutowireCapableBeanFactory()));
    }

    @Bean
    @ConditionalOnMissingBean(name = "quartzScheduleEngine")
    ScheduleEngine quartzScheduleEngine(Scheduler scheduler) {
        return new QuartzScheduleEngine(scheduler);
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleEngineRegistry scheduleEngineRegistry(
            List<ScheduleEngine> engines) {

        return new ScheduleEngineRegistry(engines);
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleEngineBindingRepository scheduleEngineBindingRepository() {
        return new InMemoryScheduleEngineBindingRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleManager scheduleManager(
            ScheduleEngineRegistry registry,
            ScheduleEngineBindingRepository bindings,
            CurrentOperatorProvider operatorProvider,
            ScheduleOperationAuditRepository auditRepository,
            YakScheduleProperties properties) {

        return new DefaultScheduleManager(
                registry,
                bindings,
                operatorProvider,
                auditRepository,
                properties.getDefaultEngine());
    }

    @Bean
    @ConditionalOnMissingBean
    ScheduleTaskService scheduleTaskService(
            ScheduleManager scheduleManager,
            ScheduleExecutionLogRepository logRepository,
            ScheduleOperationAuditRepository auditRepository) {

        return new ScheduleTaskService(
                scheduleManager,
                logRepository,
                auditRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(
            type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(
            prefix = "yak.schedule",
            name = "web-enabled",
            havingValue = "true",
            matchIfMissing = true)
    ScheduleTaskController scheduleTaskController(
            ScheduleTaskService service) {

        return new ScheduleTaskController(service);
    }
}
