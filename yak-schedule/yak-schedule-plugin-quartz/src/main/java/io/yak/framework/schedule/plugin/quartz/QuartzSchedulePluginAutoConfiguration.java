package io.yak.framework.schedule.plugin.quartz;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleEngineBindingRepository;
import io.yak.framework.schedule.core.ScheduleCoreAutoConfiguration;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;

/** Quartz 插件自动注册。 */
@AutoConfiguration(
        before = ScheduleCoreAutoConfiguration.class,
        after = QuartzAutoConfiguration.class)
@ConditionalOnClass(Scheduler.class)
@ConditionalOnProperty(
        prefix = "yak.schedule",
        name = "engine",
        havingValue = "quartz",
        matchIfMissing = true)
public class QuartzSchedulePluginAutoConfiguration {

    @Bean
    SchedulerFactoryBeanCustomizer yakScheduleQuartzJobFactoryCustomizer(
            AutowireCapableBeanFactory beanFactory) {
        return factory -> factory.setJobFactory(
                new AutowiringQuartzJobFactory(beanFactory));
    }

    @Bean
    @ConditionalOnMissingBean(name = "quartzScheduleEngine")
    ScheduleEngine quartzScheduleEngine(
            Scheduler scheduler,
            @Qualifier("yakScheduleObjectMapper") ObjectMapper objectMapper,
            ScheduleEngineBindingRepository bindingRepository) {
        return new QuartzScheduleEngine(
                scheduler,
                objectMapper,
                bindingRepository);
    }
}
