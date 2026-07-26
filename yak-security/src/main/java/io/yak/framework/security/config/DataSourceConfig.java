package io.yak.framework.security.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import io.yak.framework.security.properties.YakSecurityProperties;
import javax.sql.DataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "yakSecurityDataSourceConfig")
@MapperScan(value = {"io.yak.framework.security.dao.mapper"})
/**
 * 安全模块独立数据源与 MyBatis 配置，Mapper
 * 扫描限定在当前模块以避免跨模块数据访问。
 */
public class DataSourceConfig {
  @Bean
  public GlobalConfig globalConfig() {
    GlobalConfig globalConfig = new GlobalConfig();
    globalConfig.setBanner(false);
    GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
    dbConfig.setIdType(IdType.AUTO);
    globalConfig.setDbConfig(dbConfig);
    return globalConfig;
  }

  @Bean(value = {"yakSecurityDataSource"})
  public DataSource dataSource(YakSecurityProperties proper) {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setUsername(proper.getUsername());
    dataSource.setPassword(proper.getPassword());
    dataSource.setJdbcUrl(proper.getJdbcUrl());
    dataSource.setDriverClassName(proper.getDriverClassName());
    return dataSource;
  }

  @Bean(value = {"yakSecurityMybatisPlusInterceptor"})
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(
        (InnerInterceptor) new PaginationInnerInterceptor(DbType.MARIADB));
    return interceptor;
  }
}
