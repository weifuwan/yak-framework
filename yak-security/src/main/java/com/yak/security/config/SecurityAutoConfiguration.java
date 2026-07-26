package com.yak.security.config;
import com.yak.security.audit.SecurityAspects;
import com.yak.security.mapper.*;
import com.yak.security.service.*;
import com.yak.security.web.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
@Configuration
@ConditionalOnClass(UserMapper.class)
@ConditionalOnProperty(prefix = "yak.security", name = "enabled",
                       havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SecurityProperties.class)
@MapperScan("com.yak.security.mapper")
public class SecurityAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean
  public PasswordEncoder passwordEncoder(SecurityProperties p) {
    return new Pbkdf2PasswordEncoder(p.getPasswordIterations());
  }
  @Bean
  @ConditionalOnMissingBean
  public TokenService tokenService(SecurityProperties p) {
    return new InMemoryTokenService(p.getTokenTtlSeconds());
  }
  @Bean
  @ConditionalOnMissingBean
  public PermissionChecker permissionChecker() {
    return new PermissionChecker();
  }
  @Bean
  public AuthenticationService
  authenticationService(UserMapper u, PasswordEncoder e, TokenService t) {
    return new AuthenticationService(u, e, t);
  }
  @Bean
  public SecurityAspects securityAspects(PermissionChecker c,
                                         OperationLogMapper l) {
    return new SecurityAspects(c, l);
  }
  @Bean
  public AuthenticationController
  authenticationController(AuthenticationService s) {
    return new AuthenticationController(s);
  }
  @Bean
  public FilterRegistrationBean<TokenAuthenticationFilter>
  securityTokenFilter(TokenService t, SecurityProperties p) {
    FilterRegistrationBean<TokenAuthenticationFilter> b =
        new FilterRegistrationBean<TokenAuthenticationFilter>();
    b.setFilter(new TokenAuthenticationFilter(t, p.getTokenHeader()));
    b.addUrlPatterns("/*");
    b.setOrder(-100);
    return b;
  }
}
