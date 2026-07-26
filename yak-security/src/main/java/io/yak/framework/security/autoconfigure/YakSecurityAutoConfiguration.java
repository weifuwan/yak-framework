package io.yak.framework.security.autoconfigure;

import io.yak.framework.security.bootstrap.YakSecurityBootstrapInitializer;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.context.CurrentUser;
import io.yak.framework.security.context.DefaultCurrentUser;
import io.yak.framework.security.context.YakSecurityContextFilter;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.extend.*;
import io.yak.framework.security.extend.impl.*;
import io.yak.framework.security.permission.PermissionRegistrationInitializer;
import io.yak.framework.security.permission.PermissionRegistrationService;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.service.RoleService;
import io.yak.framework.security.service.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

@org.springframework.context.annotation.Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(YakSecurityProperties.class)
@ConditionalOnProperty(prefix = "yak.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({YakSecurityAutoConfiguration.ExtensionConfiguration.class,
    YakSecurityDatabaseConfiguration.class, YakSecurityWebConfiguration.class,
    YakSecurityAuditConfiguration.class, YakSecurityOpenApiConfiguration.class})
public class YakSecurityAutoConfiguration {
  @Bean
  @ConditionalOnBean(PermissionDao.class)
  @ConditionalOnProperty(prefix = "yak.security.permission-registration", name = "enabled",
      havingValue = "true", matchIfMissing = true)
  PermissionRegistrationService yakPermissionRegistrationService(PermissionDao permissionDao) {
    return new PermissionRegistrationService(permissionDao);
  }

  @Bean
  @ConditionalOnBean(PermissionDao.class)
  @ConditionalOnProperty(prefix = "yak.security.permission-registration", name = "enabled",
      havingValue = "true", matchIfMissing = true)
  PermissionRegistrationInitializer yakPermissionRegistrationInitializer(
      org.springframework.beans.factory.ListableBeanFactory beanFactory,
      PermissionRegistrationService registrationService) {
    return new PermissionRegistrationInitializer(beanFactory, registrationService);
  }

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
    @Bean @ConditionalOnMissingBean(CurrentUser.class)
    CurrentUser currentUser() { return new DefaultCurrentUser(); }
    @Bean
    @ConditionalOnMissingBean(YakSecurityContextFilter.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    YakSecurityContextFilter yakSecurityContextFilter(
            ObjectProvider<UserRoleDao> userRoleDaoProvider) {
      return new YakSecurityContextFilter(userRoleDaoProvider);
    }
    @Bean @ConditionalOnMissingBean
    TokenSessionStore tokenSessionStore() { return new InMemoryTokenSessionStore(); }
    @Bean @ConditionalOnMissingBean
    OperationLogExtend operationLogExtend() { return new NoOpOperationLogExtend(); }
    @Bean @ConditionalOnMissingBean
    ResourceExtend resourceExtend() { return new DefaultResourceExtendImpl(); }
  }
}
