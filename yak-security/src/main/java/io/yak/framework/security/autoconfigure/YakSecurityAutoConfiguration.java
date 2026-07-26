package io.yak.framework.security.autoconfigure;

import io.yak.framework.security.extend.*;
import io.yak.framework.security.extend.impl.*;
import io.yak.framework.security.properties.YakSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(YakSecurityProperties.class)
@ConditionalOnProperty(prefix = "yak.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({YakSecurityAutoConfiguration.ExtensionConfiguration.class,
    YakSecurityDatabaseConfiguration.class, YakSecurityWebConfiguration.class,
    YakSecurityAuditConfiguration.class})
public class YakSecurityAutoConfiguration {
  static class ExtensionConfiguration {
    @Bean @ConditionalOnMissingBean
    PasswordEncoder passwordEncoder() { return new DefaultPasswordEncoder(); }
    @Bean @ConditionalOnMissingBean
    PermissionExtend permissionExtend() { return new DefaultPermissionExtend(); }
    @Bean @ConditionalOnMissingBean
    CurrentUserProvider currentUserProvider() { return new DefaultCurrentUserProvider(); }
    @Bean @ConditionalOnMissingBean
    TokenSessionStore tokenSessionStore() { return new InMemoryTokenSessionStore(); }
    @Bean @ConditionalOnMissingBean
    OperationLogExtend operationLogExtend() { return new NoOpOperationLogExtend(); }
    @Bean @ConditionalOnMissingBean
    ResourceExtend resourceExtend() { return new DefaultResourceExtendImpl(); }
    @Bean @ConditionalOnMissingBean
    ResourceExtendBeanTool resourceExtendBeanTool(ResourceExtend extend) { return new ResourceExtendBeanTool(extend); }
    @Bean @ConditionalOnBean(LoginExtend.class) @ConditionalOnMissingBean
    LoginExtendBeanTool loginExtendBeanTool(LoginExtend extend) { return new LoginExtendBeanTool(extend); }
  }
}
