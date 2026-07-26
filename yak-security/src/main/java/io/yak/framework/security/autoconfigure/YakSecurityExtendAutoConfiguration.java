package io.yak.framework.security.autoconfigure;

import io.yak.framework.security.extend.CurrentUserProvider;
import io.yak.framework.security.extend.LoginExtend;
import io.yak.framework.security.extend.LoginExtendBeanTool;
import io.yak.framework.security.extend.OperationLogExtend;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.extend.PermissionExtend;
import io.yak.framework.security.extend.ResourceExtend;
import io.yak.framework.security.extend.ResourceExtendBeanTool;
import io.yak.framework.security.extend.TokenSessionStore;
import io.yak.framework.security.extend.impl.DefaultCurrentUserProvider;
import io.yak.framework.security.extend.impl.DefaultLoginExtendImpl;
import io.yak.framework.security.extend.impl.DefaultPasswordEncoder;
import io.yak.framework.security.extend.impl.DefaultPermissionExtend;
import io.yak.framework.security.extend.impl.DefaultResourceExtendImpl;
import io.yak.framework.security.extend.impl.InMemoryTokenSessionStore;
import io.yak.framework.security.extend.impl.NoOpOperationLogExtend;
import io.yak.framework.security.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Yak Security 扩展点自动配置。
 *
 * <p>宿主应用未提供自定义实现时，注册默认扩展实现。</p>
 *
 * @author weifuwan
 */
@Configuration(proxyBeanMethods = false)
public class YakSecurityExtendAutoConfiguration {

    /**
     * 注册默认密码编码器。
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new DefaultPasswordEncoder();
    }

    /**
     * 注册默认当前用户提供器。
     */
    @Bean
    @ConditionalOnMissingBean(CurrentUserProvider.class)
    public CurrentUserProvider currentUserProvider() {
        return new DefaultCurrentUserProvider();
    }

    /**
     * 注册默认登录扩展。
     */
    @Bean
    @ConditionalOnMissingBean(LoginExtend.class)
    public LoginExtend loginExtend(
            UserService userService,
            PasswordEncoder passwordEncoder) {

        return new DefaultLoginExtendImpl(
                userService,
                passwordEncoder);
    }

    /**
     * 注册默认权限扩展。
     */
    @Bean
    @ConditionalOnMissingBean(PermissionExtend.class)
    public PermissionExtend permissionExtend() {
        return new DefaultPermissionExtend();
    }

    /**
     * 注册默认资源扩展。
     */
    @Bean
    @ConditionalOnMissingBean(ResourceExtend.class)
    public ResourceExtend resourceExtend() {
        return new DefaultResourceExtendImpl();
    }

    /**
     * 注册默认操作日志扩展。
     */
    @Bean
    @ConditionalOnMissingBean(OperationLogExtend.class)
    public OperationLogExtend operationLogExtend() {
        return new NoOpOperationLogExtend();
    }

    /**
     * 注册默认 Token 会话存储。
     */
    @Bean
    @ConditionalOnMissingBean(TokenSessionStore.class)
    public TokenSessionStore tokenSessionStore() {
        return new InMemoryTokenSessionStore();
    }

    /**
     * 注册登录扩展兼容包装类。
     */
    @Bean
    @ConditionalOnMissingBean(LoginExtendBeanTool.class)
    public LoginExtendBeanTool loginExtendBeanTool(
            LoginExtend loginExtend) {

        return new LoginExtendBeanTool(loginExtend);
    }

    /**
     * 注册资源扩展兼容包装类。
     */
    @Bean
    @ConditionalOnMissingBean(ResourceExtendBeanTool.class)
    public ResourceExtendBeanTool resourceExtendBeanTool(
            ResourceExtend resourceExtend) {

        return new ResourceExtendBeanTool(resourceExtend);
    }
}