/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.zaxxer.hikari.HikariDataSource
 *  org.apache.ibatis.session.Configuration
 *  org.apache.ibatis.session.SqlSessionFactory
 *  org.mybatis.spring.SqlSessionFactoryBean
 *  org.mybatis.spring.SqlSessionTemplate
 *  org.mybatis.spring.annotation.MapperScan
 *  org.springframework.beans.factory.annotation.Qualifier
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.core.io.support.PathMatchingResourcePatternResolver
 *  org.springframework.transaction.annotation.EnableTransactionManagement
 */
package com.didiglobal.logi.job.configuration;

import com.didiglobal.logi.job.LogIJobProperties;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@MapperScan(value={"com.didiglobal.logi.job.mapper"}, sqlSessionFactoryRef="auvSqlSessionFactory")
public class AuvDataSourceConfig {
    @Bean(value={"auvDataSource"})
    public DataSource dataSource(LogIJobProperties logIJobProperties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(logIJobProperties.getUsername());
        dataSource.setPassword(logIJobProperties.getPassword());
        dataSource.setJdbcUrl(logIJobProperties.getJdbcUrl());
        dataSource.setDriverClassName(logIJobProperties.getDriverClassName());
        dataSource.setMaxLifetime(logIJobProperties.getMaxLifetime().longValue());
        return dataSource;
    }

    @Bean(value={"auvSqlSessionFactory"})
    public SqlSessionFactory sqlSessionFactory(@Qualifier(value="auvDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/logi-job/*.xml"));
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);
        return bean.getObject();
    }

    @Bean(value={"auvSqlSessionTemplate"})
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier(value="auvSqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}

