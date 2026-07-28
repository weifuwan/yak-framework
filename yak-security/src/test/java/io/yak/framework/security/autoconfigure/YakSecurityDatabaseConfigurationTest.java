package io.yak.framework.security.autoconfigure;

import io.yak.framework.security.service.impl.CaffeinePermissionCache;
import io.yak.framework.security.service.impl.MenuAuthorizationService;
import io.yak.framework.security.service.impl.MenuAwarePermissionService;
import io.yak.framework.security.service.impl.MenuAwareRolePermissionService;
import io.yak.framework.security.service.impl.PermissionAdministrationService;
import io.yak.framework.security.service.impl.RbacPermissionServiceImpl;
import io.yak.framework.security.service.impl.UserAdministrationService;
import io.yak.framework.security.service.impl.UserMenuGrantService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class YakSecurityDatabaseConfigurationTest {

  @Test
  void importsDefaultPermissionServicesAndCache() {
    Import importedBeans =
        YakSecurityDatabaseConfiguration.class.getAnnotation(Import.class);

    assertThat(importedBeans).isNotNull();
    assertThat(Arrays.asList(importedBeans.value()))
        .contains(
            RbacPermissionServiceImpl.class,
            CaffeinePermissionCache.class,
            PermissionAdministrationService.class,
            UserAdministrationService.class,
            MenuAuthorizationService.class,
            MenuAwarePermissionService.class,
            MenuAwareRolePermissionService.class,
            UserMenuGrantService.class);
  }
}
