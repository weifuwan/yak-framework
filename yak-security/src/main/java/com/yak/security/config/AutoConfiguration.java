package com.yak.security.config;

import com.yak.security.audit.SecurityAspects;
import com.yak.security.mapper.OperationLogMapper;
import com.yak.security.mapper.UserMapper;
import com.yak.security.service.AuthenticationService;
import com.yak.security.service.InMemoryTokenService;
import com.yak.security.service.PasswordEncoder;
import com.yak.security.service.Pbkdf2PasswordEncoder;
import com.yak.security.service.PermissionChecker;
import com.yak.security.service.TokenService;
import com.yak.security.web.AuthenticationController;
import com.yak.security.web.TokenAuthenticationFilter;
import jakarta.servlet.Filter;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@org.springframework.boot.autoconfigure.AutoConfiguration
@AutoConfigureAfter(name =
    "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
@EnableConfigurationProperties(YakSecurityProperties.class)
@ConditionalOnProperty(prefix = "yak.security", name = "enabled",
    havingValue = "true", matchIfMissing = true)
public class AutoConfiguration {
  @Bean
  @ConditionalOnMissingBean
  public PasswordEncoder passwordEncoder(YakSecurityProperties properties) {
    return new Pbkdf2PasswordEncoder(properties.getPasswordIterations());
  }

  @Bean
  @ConditionalOnMissingBean
  public TokenService tokenService(YakSecurityProperties properties) {
    return new InMemoryTokenService(properties.getTokenTtlSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  public PermissionChecker permissionChecker() {
    return new PermissionChecker();
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnClass({DataSource.class, SqlSessionFactory.class})
  @ConditionalOnBean({DataSource.class, SqlSessionFactory.class})
  @ConditionalOnProperty(prefix = "yak.security", name = "database-enabled",
      havingValue = "true", matchIfMissing = true)
  @MapperScan("com.yak.security.mapper")
  static class DatabaseConfiguration {
    @Bean
    @ConditionalOnMissingBean
    AuthenticationService authenticationService(
        UserMapper users, PasswordEncoder encoder, TokenService tokens) {
      return new AuthenticationService(users, encoder, tokens);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "yak.security", name = "audit-enabled",
        havingValue = "true", matchIfMissing = true)
    SecurityAspects securityAspects(PermissionChecker checker,
                                    OperationLogMapper logs) {
      return new SecurityAspects(checker, logs);
    }
  }

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
  @ConditionalOnClass({Filter.class, DispatcherServlet.class})
  @ConditionalOnBean(AuthenticationService.class)
  @ConditionalOnProperty(prefix = "yak.security", name = "web-enabled",
      havingValue = "true", matchIfMissing = true)
  static class WebConfiguration {
    @Bean
    @ConditionalOnMissingBean
    AuthenticationController authenticationController(
        AuthenticationService service) {
      return new AuthenticationController(service);
    }

    @Bean
    @ConditionalOnMissingBean(name = "securityTokenFilter")
    FilterRegistrationBean<TokenAuthenticationFilter> securityTokenFilter(
        TokenService tokens, YakSecurityProperties properties) {
      FilterRegistrationBean<TokenAuthenticationFilter> registration =
          new FilterRegistrationBean<TokenAuthenticationFilter>();
      registration.setFilter(
          new TokenAuthenticationFilter(tokens, properties.getTokenHeader()));
      registration.addUrlPatterns("/*");
      registration.setOrder(-100);
      return registration;
    }
  }
}
