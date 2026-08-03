package io.yak.framework.schedule.plugin.xxljob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import io.yak.framework.schedule.api.ScheduleDefinitionRepository;
import io.yak.framework.schedule.api.ScheduleEngine;
import io.yak.framework.schedule.api.ScheduleEngineBindingRepository;
import io.yak.framework.schedule.core.ScheduleCoreAutoConfiguration;
import io.yak.framework.schedule.core.ScheduleExecutionDispatcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/** XXL-JOB 插件自动注册。 */
@AutoConfiguration(before = ScheduleCoreAutoConfiguration.class)
@ConditionalOnClass(XxlJobSpringExecutor.class)
@ConditionalOnProperty(
        prefix = "yak.schedule",
        name = "engine",
        havingValue = "xxl-job")
@EnableConfigurationProperties(XxlJobPluginProperties.class)
public class XxlJobSchedulePluginAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    XxlJobAdminClient xxlJobAdminClient(
            XxlJobPluginProperties properties,
            @Qualifier("yakScheduleObjectMapper") ObjectMapper objectMapper) {
        properties.validate();
        return new XxlJobHttpAdminClient(properties, objectMapper);
    }

    @Bean
    XxlJobBridgeHandler xxlJobBridgeHandler(
            @Qualifier("yakScheduleObjectMapper") ObjectMapper objectMapper,
            ScheduleExecutionDispatcher dispatcher) {
        return new XxlJobBridgeHandler(objectMapper, dispatcher);
    }

    @Bean
    @ConditionalOnMissingBean
    XxlJobSpringExecutor xxlJobSpringExecutor(
            XxlJobPluginProperties properties) {
        properties.validate();
        XxlJobPluginProperties.Executor config = properties.getExecutor();
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(properties.getAdminAddress());
        executor.setAccessToken(config.getAccessToken());
        executor.setTimeout(config.getTimeoutSeconds());
        executor.setEnabled(config.isEnabled());
        executor.setAppname(config.getAppName());
        executor.setAddress(config.getAddress());
        executor.setIp(config.getIp());
        executor.setPort(config.getPort());
        executor.setLogPath(config.getLogPath());
        executor.setLogRetentionDays(config.getLogRetentionDays());
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(name = "xxlJobScheduleEngine")
    ScheduleEngine xxlJobScheduleEngine(
            XxlJobPluginProperties properties,
            XxlJobAdminClient adminClient,
            ScheduleEngineBindingRepository bindingRepository,
            ScheduleDefinitionRepository definitionRepository,
            @Qualifier("yakScheduleObjectMapper") ObjectMapper objectMapper) {
        properties.validate();
        return new XxlJobScheduleEngine(
                properties,
                adminClient,
                bindingRepository,
                definitionRepository,
                objectMapper);
    }
}
