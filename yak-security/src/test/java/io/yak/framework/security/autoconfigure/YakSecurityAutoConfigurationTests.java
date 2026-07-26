package io.yak.framework.security.autoconfigure;

import io.yak.framework.security.controller.v1.OplogController;
import io.yak.framework.security.controller.v1.UserController;
import io.yak.framework.security.dao.mapper.UserMapper;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.extend.ResourceExtend;
import io.yak.framework.security.properties.YakSecurityProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class YakSecurityAutoConfigurationTests {
  private final ApplicationContextRunner runner = new ApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(YakSecurityAutoConfiguration.class));

  @Test
  void loadsDefaultsWithoutDatabaseInfrastructure() {
    runner.withPropertyValues("yak.security.database-enabled=false").run(context -> {
      assertThat(context).hasSingleBean(YakSecurityProperties.class);
      assertThat(context).hasSingleBean(PasswordEncoder.class);
      assertThat(context).hasSingleBean(ResourceExtend.class);
    });
  }

  @Test
  void backsOffWhenDisabled() {
    runner.withPropertyValues("yak.security.enabled=false").run(context ->
        assertThat(context).doesNotHaveBean(YakSecurityProperties.class));
  }

  @Test
  void databaseSwitchPreventsTheEntireDatabaseLayer() {
    runner.withPropertyValues("yak.security.database-enabled=false").run(context -> {
      assertThat(context).doesNotHaveBean(UserMapper.class);
      assertThat(context).doesNotHaveBean(UserController.class);
    });
  }

  @Test
  void hostExtensionOverridesDefaultByType() {
    runner.withPropertyValues("yak.security.database-enabled=false")
        .withUserConfiguration(HostExtensionConfiguration.class)
        .run(context -> assertThat(context).getBean(PasswordEncoder.class)
            .isSameAs(context.getBean("hostPasswordEncoder")));
  }

  @Test
  void webAndAuditSwitchesPreventTheirControllers() {
    runner.withPropertyValues("yak.security.database-enabled=false",
        "yak.security.web-enabled=false", "yak.security.audit-enabled=false")
        .run(context -> {
          assertThat(context).doesNotHaveBean(UserController.class);
          assertThat(context).doesNotHaveBean(OplogController.class);
        });
  }

  @Configuration(proxyBeanMethods = false)
  static class HostExtensionConfiguration {
    @Bean PasswordEncoder hostPasswordEncoder() {
      return new PasswordEncoder() {
        public String encode(CharSequence raw) { return "host:" + raw; }
        public boolean matches(CharSequence raw, String encoded) {
          return encode(raw).equals(encoded);
        }
      };
    }
  }
}
