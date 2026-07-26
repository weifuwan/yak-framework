package io.yak.framework.security.autoconfigure;

import io.yak.framework.security.bootstrap.YakSecurityBootstrapInitializer;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.extend.*;
import io.yak.framework.security.extend.impl.*;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.service.RoleService;
import io.yak.framework.security.service.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@org.springframework.context.annotation.Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(YakSecurityProperties.class)
@ConditionalOnProperty(prefix = "yak.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({YakSecurityAutoConfiguration.ExtensionConfiguration.class,
    YakSecurityDatabaseConfiguration.class, YakSecurityWebConfiguration.class,
    YakSecurityAuditConfiguration.class, YakSecurityOpenApiConfiguration.class})
public class YakSecurityAutoConfiguration {
  @Bean
  @ConditionalOnBean({UserService.class, RoleService.class,
      RolePermissionService.class, PermissionDao.class})
  @ConditionalOnProperty(prefix = "yak.security.bootstrap", name = "enabled", havingValue = "true")
  YakSecurityBootstrapInitializer yakSecurityBootstrapInitializer(
          YakSecurityProperties properties, UserService userService,
          RoleService roleService, RolePermissionService rolePermissionService,
          PermissionDao permissionDao) {
    return new YakSecurityBootstrapInitializer(
            properties, userService, roleService, rolePermissionService, permissionDao);
  }

  static class ExtensionConfiguration {
    @Bean @ConditionalOnMissingBean(PasswordEncoder.class)
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
  }
}
