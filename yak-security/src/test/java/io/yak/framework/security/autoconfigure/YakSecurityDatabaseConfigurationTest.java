package io.yak.framework.security.autoconfigure;

import io.yak.framework.security.service.impl.RbacPermissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class YakSecurityDatabaseConfigurationTest {

  @Test
  void importsDefaultRbacPermissionService() {
    Import importedBeans =
        YakSecurityDatabaseConfiguration.class.getAnnotation(Import.class);

    assertThat(importedBeans).isNotNull();
    assertThat(Arrays.asList(importedBeans.value()))
        .contains(RbacPermissionServiceImpl.class);
  }
}
