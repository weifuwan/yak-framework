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
