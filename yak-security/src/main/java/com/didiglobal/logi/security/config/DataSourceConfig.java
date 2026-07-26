/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.DbType
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.core.config.GlobalConfig
 *  com.baomidou.mybatisplus.core.config.GlobalConfig$DbConfig
 *  com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
 *  com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor
 *  com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
 *  com.zaxxer.hikari.HikariDataSource
 *  org.mybatis.spring.annotation.MapperScan
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.didiglobal.logi.security.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.didiglobal.logi.security.properties.LogiSecurityProper;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value="logiSecurityDataSourceConfig")
@MapperScan(value={"com.didiglobal.logi.security.dao.mapper"})
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

    @Bean(value={"logiSecurityDataSource"})
    public DataSource dataSource(LogiSecurityProper proper) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(proper.getUsername());
        dataSource.setPassword(proper.getPassword());
        dataSource.setJdbcUrl(proper.getJdbcUrl());
        dataSource.setDriverClassName(proper.getDriverClassName());
        return dataSource;
    }

    @Bean(value={"logiSecurityMybatisPlusInterceptor"})
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor((InnerInterceptor)new PaginationInnerInterceptor(DbType.MARIADB));
        return interceptor;
    }
}

