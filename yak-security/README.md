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

## 统一登录认证

Starter 会自动注册 Spring MVC `HandlerInterceptor`。除登录接口
`/yak-security/api/v1/account/login`、健康检查和公开接口外，所有 MVC 请求都必须具有有效的
服务端 HTTP Session；未登录或会话中的用户已禁用/不存在时返回 HTTP 401。

当前用户身份仅由服务端登录流程写入 Session。框架不会把客户端传入的
`X-SSO-USER`、`X-SSO-USER-ID` 请求头或同名 Cookie 当作认证依据。需要接入
外部认证系统时，应实现 `CurrentUserProvider`，并且只从已完成服务端校验的可信
认证上下文读取用户信息。

公开接口可以在控制器类或方法上添加 `@PublicEndpoint`，也可以通过配置添加 Ant 风格路径：

```yaml
yak:
  security:
    authentication-enabled: true
    public-paths:
      - /yak-security/api/v1/account/login
      - /yak-security/api/v1/common/heart
      - /api/public/**
```

`OPTIONS` CORS 预检请求会被直接放行。若宿主应用需要完全接管认证，可将
`yak.security.authentication-enabled` 设为 `false`。本模块使用 Spring Boot 2.7 和
`javax.servlet` API 构建，支持 JDK 8。

## 数据库迁移与应用隔离

模块启动时由 Flyway 依次执行 `db/migration/V1__init_yak_security.sql` 和
`V2__init_yak_security_data.sql`。旧的覆盖式 `yak-security.sql` 已移除，生产环境不得再通过
`schema.sql` 或初始化脚本重复加载表结构。目标数据库为 MariaDB 10.6 及以上版本。

`yak.security.application-name` 是**必填、非空且无默认值**的应用级数据隔离键。
全部核心表的 `app_name` 均为 `NOT NULL`；MyBatis 租户拦截器会为用户、角色、权限、项目及其
关联表的查询、更新、删除和新增自动附加该值。禁止绕过 DAO 执行不带 `app_name` 的 SQL。

唯一索引使用“未删除值”的生成列：`is_delete=0` 时参与应用维度唯一约束，删除后生成列为
`NULL`，因此同一应用可重新创建同名记录，同时仍可保留多条历史删除记录。`is_delete` 的含义
统一为 `0` 未删除、`1` 已删除。

数据库刻意不创建物理外键，**关系完整性由 Service 层维护**。创建用户角色、角色权限、用户项目
或用户资源关系前，Service 必须在同一 `applicationName` 下校验两端记录存在且未删除；删除用户、
角色或权限时，必须同步逻辑删除相应关系记录。上述校验、主记录变更和关系清理必须置于同一个
`yakSecurityTransactionManager` 事务边界（实现方法使用 `@Transactional(transactionManager =
"yakSecurityTransactionManager")`），任一步骤失败即整体回滚。

V2 只提供安全模块运行所需的权限目录和资源类型，不会插入默认用户、明文密码或通用管理员密码。
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
