package io.yak.framework.security.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.StringValue;
import org.flywaydb.core.Flyway;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

/** 安全模块独立数据源与 MyBatis 配置。 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "yak.security", name = "database-enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnProperty(prefix = "yak.security.datasource", name = "enabled", havingValue = "true", matchIfMissing = true)
@MapperScan(basePackages = "io.yak.framework.security.dao.mapper",
    sqlSessionTemplateRef = "yakSecuritySqlSessionTemplate")
public class DataSourceConfig {
  @Bean("yakSecurityGlobalConfig")
  public GlobalConfig yakSecurityGlobalConfig() {
    GlobalConfig config = new GlobalConfig();
    config.setBanner(false);
    GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
    dbConfig.setIdType(IdType.AUTO);
    config.setDbConfig(dbConfig);
    return config;
  }

  @Bean("yakSecurityDataSource")
  public DataSource yakSecurityDataSource(YakSecurityProperties properties) {
    YakSecurityProperties.DataSourceProperties datasource = properties.getDatasource();
    requireText(datasource.getUrl(), "yak.security.datasource.url");
    requireText(datasource.getUsername(), "yak.security.datasource.username");
    requireText(datasource.getDriverClassName(), "yak.security.datasource.driver-class-name");

    DruidDataSource result = new DruidDataSource();
    result.setUrl(datasource.getUrl());
    result.setUsername(datasource.getUsername());
    result.setPassword(datasource.getPassword());
    result.setDriverClassName(datasource.getDriverClassName());
    result.setInitialSize(datasource.getInitialSize());
    result.setMinIdle(datasource.getMinIdle());
    result.setMaxActive(datasource.getMaxActive());
    result.setMaxWait(datasource.getMaxWait());
    result.setValidationQuery(datasource.getValidationQuery());
    result.setTestWhileIdle(datasource.isTestWhileIdle());
    result.setTestOnBorrow(datasource.isTestOnBorrow());
    result.setTestOnReturn(datasource.isTestOnReturn());
    return result;
  }

  @Bean("yakSecurityMybatisPlusInterceptor")
  public MybatisPlusInterceptor yakSecurityMybatisPlusInterceptor(
      YakSecurityProperties properties) {
    requireText(properties.getApplicationName(), "yak.security.application-name");
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    // Every ORM SELECT/UPDATE/DELETE and INSERT is constrained to the configured app.
    interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(
        () -> new StringValue(properties.getApplicationName())));
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MARIADB));
    return interceptor;
  }

  /** Runs versioned migrations on the dedicated security datasource. */
  @Bean(initMethod = "migrate", name = "yakSecurityFlyway")
  public Flyway yakSecurityFlyway(
      @Qualifier("yakSecurityDataSource") DataSource dataSource) {
    return Flyway.configure().dataSource(dataSource)
        .locations("classpath:db/migration").load();
  }

  @Bean("yakSecuritySqlSessionFactory")
  public SqlSessionFactory yakSecuritySqlSessionFactory(
      @Qualifier("yakSecurityDataSource") DataSource dataSource,
      @Qualifier("yakSecurityGlobalConfig") GlobalConfig globalConfig,
      @Qualifier("yakSecurityMybatisPlusInterceptor") MybatisPlusInterceptor interceptor)
      throws Exception {
    MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
    factory.setDataSource(dataSource);
    factory.setConfiguration(new MybatisConfiguration());
    factory.setGlobalConfig(globalConfig);
    factory.setPlugins(interceptor);
    return factory.getObject();
  }

  @Bean("yakSecuritySqlSessionTemplate")
  public SqlSessionTemplate yakSecuritySqlSessionTemplate(
      @Qualifier("yakSecuritySqlSessionFactory") SqlSessionFactory factory) {
    return new SqlSessionTemplate(factory);
  }

  @Bean("yakSecurityTransactionManager")
  public PlatformTransactionManager yakSecurityTransactionManager(
      @Qualifier("yakSecurityDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  private static void requireText(String value, String key) {
    if (!StringUtils.hasText(value)) {
      throw new IllegalStateException("Missing required configuration: " + key);
    }
  }
}
