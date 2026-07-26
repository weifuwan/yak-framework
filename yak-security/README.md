# yak-security-spring-boot-starter

`yak-security` 为 Spring Boot 应用提供用户、角色、权限、登录、数据隔离和操作审计能力。

## 接入

在应用配置中声明安全模块的独立数据源：

```yaml
spring:
  yak-security:
    app-name: demo
    username: root
    password: secret
    jdbc-url: jdbc:mariadb://localhost:3306/demo
    driver-class-name: org.mariadb.jdbc.Driver
    resource-extend-bean-name: yakSecurityDefaultResourceExtendImpl
```

自定义登录或资源查询逻辑时，分别实现
`io.yak.framework.security.extend.LoginExtend` 或
`io.yak.framework.security.extend.ResourceExtend`，并将 Bean 名配置到相应的扩展配置项。

HTTP 接口统一位于 `/yak-security/api/v1`，项目隔离标识通过
`X-YAK-SECURITY-PROJECT-ID` 请求头传递。
## Password storage and migration

Yak Security stores only one-way BCrypt hashes. New users and password changes are
always passed through the configured `PasswordEncoder`; authentication uses
`matches` and never decrypts a stored credential. Applications can replace the
encoder by declaring their own `PasswordEncoder` bean.

Login credentials must be sent over HTTPS as ordinary request values. The module
does not provide application-level password decryption or embed a transport key.

The former reversible Base64 format is intentionally **not** accepted. Before
upgrading, reset legacy credentials or perform an offline forced-password-reset
campaign. There is no compatibility switch because retaining a decoder would
preserve the ability to recover user passwords.
