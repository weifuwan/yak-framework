# yak-security-spring-boot-starter

`yak-security` 提供一个可维护的最小安全闭环：用户注册与登录、PBKDF2 密码编码、用户—角色—权限查询、基于注解的权限校验，以及操作审计。

## 启用与配置

Spring Boot 2 会通过 `META-INF/spring.factories` 加载自动配置。Mapper XML 位于 `classpath*:mapper/security/*.xml`；数据库可参考 `yak-security-schema.sql` 建表。

```yaml
yak:
  security:
    enabled: true
    token-header: X-Yak-Token
    token-ttl-seconds: 7200
    password-iterations: 120000
mybatis-plus:
  mapper-locations: classpath*:mapper/security/*.xml
  configuration:
    map-underscore-to-camel-case: true
```

- `POST /yak/security/register`：请求 `{"username":"...","password":"..."}`。
- `POST /yak/security/login`：同上；返回结构均为 `{"code":0,"message":"success","data":...}`，失败时 `data` 为 `null`。
- 在业务方法上使用 `@RequiresPermission("permission.code")` 校验权限，使用 `@AuditedOperation("operation")` 写入操作日志。
- 默认令牌存储在单 JVM 内存中；生产集群应提供自定义 `TokenService` Bean。

## 恢复范围

仓库和当前环境中没有可验证的原始 `yak-security` 源码制品、源码备份或可信发布包，因此本模块采用上述最小实现，而没有把 Git 历史中的 `com.didiglobal.logi.job` 调度代码或 `yak-job` 代码当作安全源码。

由于缺少可信的旧 API 清单，无法恢复或保证兼容旧版中除用户、角色、权限、登录、注册和操作日志之外的控制器、DTO 与扩展点。新增兼容工作必须以可验证的原始制品为依据。
