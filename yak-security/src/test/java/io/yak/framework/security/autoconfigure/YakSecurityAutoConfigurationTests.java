package io.yak.framework.security.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import io.yak.framework.security.controller.v1.OplogController;
import io.yak.framework.security.controller.v1.UserController;
import io.yak.framework.security.dao.mapper.UserMapper;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.extend.ResourceExtend;
import io.yak.framework.security.config.YakSecurityProperties;
import java.lang.reflect.Proxy;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.util.ReflectionTestUtils;

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

  @Test
  void securityDatabaseCoexistsWithHostPrimaryDataSource() {
    runner.withUserConfiguration(HostDataSourceConfiguration.class)
        .withPropertyValues(
            "yak.security.datasource.url=jdbc:mariadb://localhost/security",
            "yak.security.datasource.username=security_user",
            "yak.security.datasource.password=secret",
            "yak.security.datasource.driver-class-name=org.mariadb.jdbc.Driver")
        .run(context -> {
          assertThat(context).hasNotFailed();
          DataSource host = context.getBean("hostDataSource", DataSource.class);
          DataSource security = context.getBean("yakSecurityDataSource", DataSource.class);
          assertThat(context.getBean(DataSource.class)).isSameAs(host);
          assertThat(security).isNotSameAs(host).isInstanceOf(DruidDataSource.class);

          UserMapper mapper = context.getBean(UserMapper.class);
          Object invocationHandler = Proxy.getInvocationHandler(mapper);
          Object mapperSqlSession = ReflectionTestUtils.getField(invocationHandler, "sqlSession");
          assertThat(mapperSqlSession)
              .isSameAs(context.getBean("yakSecuritySqlSessionTemplate", SqlSessionTemplate.class));
          assertThat(((SqlSessionTemplate) mapperSqlSession).getSqlSessionFactory())
              .isSameAs(context.getBean("yakSecuritySqlSessionFactory"));
        });
  }

  @Test
  void datasourceSwitchPreventsSecurityDatabaseInfrastructure() {
    runner.withPropertyValues("yak.security.datasource.enabled=false")
        .run(context -> {
          assertThat(context).doesNotHaveBean("yakSecurityDataSource");
          assertThat(context).doesNotHaveBean(UserMapper.class);
        });
  }

  @Test
  void missingDatasourceSettingReportsOnlyItsKey() {
    runner.withPropertyValues(
            "yak.security.datasource.username=security_user",
            "yak.security.datasource.password=do-not-print",
            "yak.security.datasource.driver-class-name=org.mariadb.jdbc.Driver")
        .run(context -> assertThat(context.getStartupFailure())
            .hasMessageContaining("yak.security.datasource.url")
            .hasMessageNotContaining("do-not-print"));
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

  @Configuration(proxyBeanMethods = false)
  static class HostDataSourceConfiguration {
    @Bean
    @Primary
    DataSource hostDataSource() {
      DruidDataSource datasource = new DruidDataSource();
      datasource.setUrl("jdbc:mariadb://localhost/host");
      datasource.setUsername("host_user");
      datasource.setDriverClassName("org.mariadb.jdbc.Driver");
      return datasource;
    }
  }
}
