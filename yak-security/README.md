# yak-security-spring-boot-starter

## 首次管理员初始化

新应用可以临时启用内置初始化器，在当前应用尚无任何用户时创建管理员、默认管理员角色，
并将所有内置权限授予该角色：

```yaml
yak:
  security:
    bootstrap:
      enabled: true
      username: admin
      password: ${YAK_ADMIN_PASSWORD}
      real-name: 系统管理员
```

密码没有默认值，生产环境应通过环境变量或密钥管理系统提供。创建成功后启动日志会发出警告；
请立即将 `yak.security.bootstrap.enabled` 设为 `false`。如果当前应用已经存在用户，初始化器不会
修改任何用户、角色或权限数据。

`yak-security` 为 Spring Boot 应用提供用户、角色、权限、登录、数据隔离和操作审计能力。

完整的依赖引入、数据库准备、首次管理员初始化、登录调用、权限注解及扩展点示例，参见
**[使用说明书](docs/USAGE.md)**。

## 接入

在应用配置中声明安全模块的独立数据源：

```yaml
yak:
  security:
    application-name: demo
    datasource:
      url: jdbc:mariadb://localhost:3306/demo
      username: root
      password: secret
      driver-class-name: org.mariadb.jdbc.Driver
```

自定义登录或资源查询逻辑时，分别实现
`io.yak.framework.security.extend.LoginExtend` 或
`io.yak.framework.security.extend.ResourceExtend`，并将实现注册为 Spring Bean；Starter 会自动使用
宿主应用提供的实现。

HTTP 接口统一位于 `/yak-security/api/v1`，项目隔离标识通过
`X-YAK-SECURITY-PROJECT-ID` 请求头传递。

## 声明式权限注册

业务接口可以把鉴权和权限元数据放在一起；启动时会自动新增权限、同步名称和描述，并把已从
代码中移除的声明标记为无效。失效不会物理删除权限或角色授权关系，因此重新声明同一编码后
原有授权会恢复生效。

```java
@RequiresPermission("job:create")
@YakPermission(code = "job:create", name = "创建作业", group = "作业管理")
@PostMapping("/jobs")
public void createJob() { }
```

非接口权限也可以集中声明；仅提供编码时，权限显示名称默认使用编码：

```java
@Bean
PermissionDefinitionProvider yakOpsPermissions() {
  return PermissionDefinitionProvider.of(
      PermissionDefinition.of("job", "作业管理",
          "job:list", "job:create", "job:update", "job:delete"));
}
```

同一编码的声明若名称或分组冲突，应用会快速启动失败，避免静默写入错误元数据。可通过
`yak.security.permission-registration.enabled=false` 关闭同步。

## OpenAPI 3 / Swagger UI

Starter 默认生成 OpenAPI 3 接口文档。应用启动后可访问 `/swagger-ui.html` 查看 Swagger UI，
或通过 `/v3/api-docs` 获取 OpenAPI JSON。文档端点默认不要求登录；如果宿主应用声明自己的
`OpenAPI` Bean，Starter 会保留宿主应用提供的标题、版本、服务器等自定义信息。将
`yak.security.web-enabled` 设置为 `false` 时，安全模块的接口和 OpenAPI 配置会一并关闭。

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
`yak.security.authentication-enabled` 设为 `false`。本模块使用 Spring Boot 3.3.13 和
`jakarta.servlet` API 构建，需要 JDK 21。

### 登录与 Session 加固

默认登录实现会同时按规范化用户名和请求源 IP 统计连续失败，任一维度达到阈值都会临时锁定；
用户不存在与密码错误默认统一返回“密码错误”，避免账号枚举。计数器位于当前 JVM，集群部署应
自定义 `LoginExtend`，使用 Redis 等共享存储实现原子计数。不要直接信任客户端传入的
`X-Forwarded-For`；反向代理应先限制可信代理并由容器解析真实 `remoteAddr`。

```yaml
yak:
  security:
    login:
      max-failure-count: 5
      lock-duration: 15m
      hide-account-not-found: true
    session:
      timeout: 30m
```

登录成功时框架会更新 Session ID，并把超时写入该 Session。会话还记录登录时的密码摘要；用户
密码一旦被管理接口修改，所有旧会话在下一次受保护请求时都会被拒绝并销毁。退出登录也会立即
销毁当前 Session。

Session Cookie 属于 Servlet 容器配置。生产环境必须启用 HTTPS，并通过 Spring Boot 的标准配置
设置 `HttpOnly`、`Secure` 和 `SameSite`；`SameSite=Lax` 适合普通同站管理界面，确需跨站发送时
才使用 `None`，且浏览器要求它同时为 `Secure`：

```yaml
server:
  servlet:
    session:
      cookie:
        http-only: true
        secure: true
        same-site: lax
```

若在本地纯 HTTP 环境调试，可仅在该环境覆盖 `secure: false`，禁止把此覆盖带入生产环境。

## 权限缓存

默认使用 Caffeine 按 `applicationName + userId` 缓存用户的权限编码集合，权限判断命中缓存后
直接执行集合查询。条目默认在写入 20 分钟后过期，最多保存 10,000 个用户；可按需调整：

```yaml
yak:
  security:
    permission-cache:
      enabled: true
      ttl-minutes: 20
      maximum-size: 10000
```

用户角色、角色用户或角色权限关系发生增删时会主动清除受影响用户的缓存；权限记录发生变更时
清空整个应用实例的权限缓存。单体应用无需部署额外基础设施，集群部署可通过替换
`PermissionCache` Bean 扩展为共享缓存。

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
