# Yak Security 使用说明书

本文面向需要在 Spring Boot 应用中接入 `yak-security-spring-boot-starter` 的开发者，覆盖从安装、
建库、首次启动到登录、接口保护和扩展实现的完整流程。

## 1. 模块能力与运行要求

Yak Security 提供：

- 用户、部门、角色与权限管理；
- 基于服务端 HTTP Session 的登录认证；
- `@RequiresPermission` 声明式 RBAC 权限校验；
- 项目、业务资源授权与应用级数据隔离；
- 配置、消息和操作审计扩展能力；
- 独立 Druid 数据源及 Flyway 自动迁移。

运行环境：

- JDK 21 或更高版本；
- Spring Boot 3.3.13（使用 `jakarta.servlet` API）；
- MariaDB 10.6 或更高版本；
- Maven 3.6 或更高版本。

> 本模块会注册 Spring MVC 控制器和拦截器，因此宿主应用应为 Spring MVC 应用。

## 2. 引入依赖

### 2.1 本地构建

仓库根目录执行：

```bash
mvn -pl yak-security -am clean install
```

### 2.2 宿主应用添加依赖

```xml
<dependency>
    <groupId>io.yak.framework</groupId>
    <artifactId>yak-security-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Starter 通过 Spring Boot 自动配置加载，不需要在宿主应用中额外添加 `@ComponentScan` 或
`@MapperScan`。

## 3. 准备数据库

先创建空数据库及专用账号。示例：

```sql
CREATE DATABASE yak_security
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER 'yak_security'@'%' IDENTIFIED BY 'change-me';
GRANT ALL PRIVILEGES ON yak_security.* TO 'yak_security'@'%';
FLUSH PRIVILEGES;
```

应用启动时，模块使用 Flyway 自动执行 `V1__init_yak_security.sql` 和
`V2__init_yak_security_data.sql`，无需手工导入表结构。生产环境应由数据库管理员按最小权限原则
调整账号权限；如果运行账号没有 DDL 权限，可在发布阶段使用迁移账号预先执行 Flyway。

不要重复执行旧版 `yak-security.sql`、`schema.sql` 或自建初始化脚本，否则可能与 Flyway 历史记录
及现有表结构冲突。

## 4. 配置应用

在宿主应用的 `application.yml` 中添加：

```yaml
yak:
  security:
    enabled: true
    database-enabled: true
    web-enabled: true
    authentication-enabled: true
    audit-enabled: true

    # 必填。作为 app_name 写入全部核心表；上线后不要随意变更。
    application-name: order-service

    # 除登录、健康检查外，可额外配置无需登录的 Ant 风格路径。
    public-paths:
      - /yak-security/api/v1/account/login
      - /yak-security/api/v1/common/heart
      - /api/public/**

    datasource:
      enabled: true
      url: jdbc:mariadb://127.0.0.1:3306/yak_security?useUnicode=true&characterEncoding=utf8
      username: yak_security
      password: ${YAK_SECURITY_DB_PASSWORD}
      driver-class-name: org.mariadb.jdbc.Driver
      initial-size: 1
      min-idle: 1
      max-active: 8
      max-wait: 60000
      validation-query: SELECT 1
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
```

关键开关说明：

| 配置项 | 默认值 | 说明 |
| --- | --- | --- |
| `yak.security.enabled` | `true` | 是否启用整个 Starter |
| `yak.security.database-enabled` | `true` | 是否启用数据库、DAO 和 Service |
| `yak.security.web-enabled` | `true` | 是否注册内置 HTTP 接口 |
| `yak.security.authentication-enabled` | `true` | 是否拦截请求并校验登录及权限 |
| `yak.security.audit-enabled` | `true` | 是否启用审计配置 |
| `yak.security.application-name` | 无 | 必填的数据隔离键，不同应用应使用不同值 |
| `yak.security.datasource.enabled` | `true` | 是否创建安全模块独立数据源 |

安全模块创建的 Bean 使用 `yakSecurity` 前缀（例如 `yakSecurityDataSource` 和
`yakSecurityTransactionManager`），不会替换宿主应用的主数据源。

## 5. 首次启动与管理员初始化

V2 迁移只写入模块运行所需的基础权限目录和资源类型，**不会创建默认用户或默认密码**。因此首次
部署需要由宿主应用通过受控的初始化流程创建第一个用户。可临时加入如下初始化器，创建成功后应
通过配置开关关闭，而不是长期保留固定密码：

```java
package com.example.demo.config;

import io.yak.framework.security.common.dto.user.UserDTO;
import io.yak.framework.security.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Configuration
public class YakSecurityBootstrapConfiguration {

    @Bean
    @Profile("yak-security-bootstrap")
    CommandLineRunner createFirstAdministrator(
            UserService userService) {
        return args -> {
            if (userService.getUserByUsername("admin") != null) {
                return;
            }

            UserDTO user = new UserDTO();
            user.setUserName("admin");
            user.setPw(System.getenv("YAK_SECURITY_BOOTSTRAP_PASSWORD"));
            user.setRealName("系统管理员");
            user.setRoleIds(Collections.emptyList());
            userService.addUser(user, "bootstrap");
        };
    }
}
```

首次启动示例：

```bash
export YAK_SECURITY_DB_PASSWORD='database-password'
export YAK_SECURITY_BOOTSTRAP_PASSWORD='a-long-random-password'
java -jar app.jar --spring.profiles.active=yak-security-bootstrap
```

确认用户创建成功后，停止应用并以正常 Profile 重启，同时清除
`YAK_SECURITY_BOOTSTRAP_PASSWORD`。随后通过角色和权限接口为该用户分配管理员角色。生产环境也可
使用一次性部署任务调用 `UserService`，但不应直接写入明文密码；`UserService` 会使用配置的
`PasswordEncoder` 生成 BCrypt 哈希。

## 6. 验证启动与登录

### 6.1 在业务 Service 中获取当前用户

Starter 会自动注册 `CurrentUser` Bean。业务 Service 应优先注入该接口，
而不是在每层方法中传递 `operator`：

```java
@Service
public class JobService {
    private final CurrentUser currentUser;

    public JobService(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public void createJob(JobDTO dto) {
        String operator = currentUser.getUsername();
        Long userId = currentUser.getUserId();
        Long projectId = currentUser.getProjectId();
        List<Long> roleIds = currentUser.getRoleIds();
    }
}
```

非 Spring 管理的代码可使用 `YakSecurityContext.getCurrentUserId()`、
`getCurrentUsername()`、`getCurrentProjectId()` 和 `getCurrentRoleIds()`。未登录时标量值为
`null`、角色列表为不可变空列表，`isAuthenticated()` 为 `false`。

用户 ID 和用户名只从服务端已验证的 Session 读取；项目 ID 来自
`X-YAK-SECURITY-PROJECT-ID` 业务上下文请求头。上下文在请求结束后会自动清理，
不应将 `CurrentUser` 的值延迟到其他线程中再读取；异步任务应在提交前显式复制所需值。

### 6.2 健康检查

```bash
curl -i http://localhost:8080/yak-security/api/v1/common/heart
```

健康检查默认公开，可用于确认 Web 接口已加载。

### 6.3 登录并保存 Session Cookie

```bash
curl -i \
  -c cookies.txt \
  -H 'Content-Type: application/json' \
  -d '{"userName":"admin","pw":"a-long-random-password"}' \
  http://localhost:8080/yak-security/api/v1/account/login
```

接口统一返回如下 JSON 结构：

```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

调用方应同时检查 HTTP 状态和业务字段 `code`。登录成功后，浏览器会自动携带 Session Cookie；
命令行调用则使用 `-b cookies.txt`：

```bash
curl -i \
  -b cookies.txt \
  http://localhost:8080/yak-security/api/v1/user
```

未登录或 Session 对应用户不存在、已禁用时返回 HTTP 401。不要把客户端传入的
`X-SSO-USER`、`X-SSO-USER-ID` 请求头或同名 Cookie 当作可信身份。

### 6.3 退出登录

```bash
curl -i \
  -b cookies.txt \
  -X POST \
  http://localhost:8080/yak-security/api/v1/account/logout
```

生产环境必须使用 HTTPS 传输账号、密码和 Cookie，并根据宿主应用的安全策略设置 Session Cookie
的 `Secure`、`HttpOnly` 和 `SameSite` 属性。

## 7. 保护宿主应用接口

默认情况下，认证拦截器覆盖宿主应用的全部 Spring MVC 请求，而不只覆盖 `/yak-security/**`。

### 7.1 公开接口

推荐对少量公开接口使用 `@PublicEndpoint`：

```java
import io.yak.framework.security.web.PublicEndpoint;

@PublicEndpoint
@GetMapping("/api/public/version")
public String version() {
    return "1.0.0";
}
```

也可以把路径加入 `yak.security.public-paths`。`OPTIONS` 请求会自动放行，以支持 CORS 预检。

### 7.2 权限接口

使用 `@RequiresPermission` 声明权限编码：

```java
import io.yak.framework.security.web.RequiresPermission;

@RequiresPermission("order:read")
@GetMapping("/api/orders/{id}")
public OrderVO getOrder(@PathVariable Long id) {
    return orderService.get(id);
}
```

注解可放在控制器类或方法上；方法级配置优先。用户已登录但没有权限时返回 HTTP 403。注解值必须
与 `yak_security_permission.permission_code` 中的权限编码一致，并通过角色—权限、用户—角色关系
完成授权。

> `@PublicEndpoint` 的优先级高于权限校验。不要在同一接口上同时声明公开访问和敏感权限。

## 8. 常用内置接口

所有内置接口以 `/yak-security/api/v1` 为前缀：

| 功能 | 路径前缀 | 示例 |
| --- | --- | --- |
| 登录/退出 | `/account` | `POST /account/login`、`POST /account/logout` |
| 用户 | `/user` | 查询、分页、新增、编辑、删除 |
| 角色 | `/role` | 查询、分页、分配用户或权限 |
| 权限 | `/permission` | 权限树、导入、删除 |
| 部门 | `/dept` | 部门树、导入 |
| 项目 | `/project` | 项目维护及成员、负责人管理 |
| 资源 | `/resource` | 资源类型、资源授权和控制级别 |
| 配置 | `/config` | 配置查询、启停和维护 |
| 消息 | `/message` | 消息列表和已读状态 |
| 操作日志 | `/oplog` | 日志分页、详情和类型列表 |

接口 DTO 字段可参考 `io.yak.framework.security.common.dto` 包。实际联调时应以对应 Controller 的
HTTP 方法、路径变量和请求体定义为准。

项目维度的请求需要传递：

```http
X-YAK-SECURITY-PROJECT-ID: 1001
```

`application-name` 用于应用级数据库隔离，项目请求头用于应用内部的项目上下文，两者用途不同。

## 9. 自定义扩展点

扩展实现只需注册成 Spring Bean；自动配置使用 `@ConditionalOnMissingBean`，宿主 Bean 会替换默认
实现，无需配置 Bean 名。

### 9.1 业务资源查询

当资源授权需要读取宿主系统的订单、主机等业务对象时，实现 `ResourceExtend`：

```java
@Component
public class OrderResourceExtend implements ResourceExtend {
    @Override
    public PagingData<ResourceDTO> getResourcePage(
            Long projectId, Long resourceTypeId, String name,
            int page, int size) {
        // 从宿主业务库查询并转换为 ResourceDTO
        return orderResourceService.page(projectId, name, page, size);
    }

    @Override
    public List<ResourceDTO> getResourceList(
            Long projectId, Long resourceTypeId) {
        return orderResourceService.list(projectId);
    }

    @Override
    public int getResourceCnt(
            Long projectId, Long resourceTypeId) {
        return orderResourceService.count(projectId);
    }
}
```

分页页码从 1 开始，集合和分页结果不应返回 `null`。

### 9.2 外部登录系统

接入 SSO 或网关认证时，实现 `LoginExtend` 和 `CurrentUserProvider`。实现必须从**已经由服务端验签
或校验**的认证上下文中读取用户，不能直接信任任意请求头、Cookie 或请求参数。

### 9.3 其他扩展

| 接口 | 默认行为 | 典型用途 |
| --- | --- | --- |
| `PasswordEncoder` | BCrypt | 接入组织统一的不可逆密码散列策略 |
| `PermissionExtend` | 默认权限实现 | 增加宿主业务的额外权限判断 |
| `OperationLogExtend` | 不执行外部操作 | 写入消息队列、日志平台或审计系统 |
| `TokenSessionStore` | 单机内存存储 | 使用 Redis 支持多实例 Token 会话 |

自定义 `OperationLogExtend` 时不得记录密码、Token、Cookie 等敏感信息。替换
`PasswordEncoder` 时必须使用不可逆的安全散列算法，并制定现有密码哈希迁移方案。

## 10. 关闭或部分启用模块

- 完全关闭：`yak.security.enabled=false`；
- 只关闭统一认证：`yak.security.authentication-enabled=false`；
- 不注册内置 Web 接口：`yak.security.web-enabled=false`；
- 不启用数据库能力：`yak.security.database-enabled=false`。

`web-enabled=true` 依赖数据库 Service，因此不能在关闭数据库能力后继续使用内置 Web 接口。若宿主
应用完全接管认证，可关闭 `authentication-enabled`，但此时必须自行保护所有敏感接口。

## 11. 数据隔离与运维注意事项

1. `application-name` 是必填且非空的应用隔离键，同库部署多个应用时必须使用不同值。
2. 核心表由 MyBatis 租户拦截器自动附加 `app_name`；不要绕过 DAO 执行缺少该条件的 SQL。
3. 表之间刻意不创建物理外键，关系完整性由 Service 层及
   `yakSecurityTransactionManager` 事务维护。
4. `is_delete=0` 表示未删除，`is_delete=1` 表示逻辑删除；不要自行颠倒其语义。
5. 备份与恢复时必须同时保留 Flyway 历史表和全部 Yak Security 表。
6. 多实例部署时，默认 HTTP Session 也需要由宿主应用配置共享存储（例如 Spring Session Redis）；
   否则应确保负载均衡会话粘滞。

## 12. 常见问题排查

### 启动提示缺少 `yak.security.application-name`

数据库功能开启时该值必填。检查配置层级是否为 `yak.security.application-name`，并确认配置文件或
环境变量在当前 Profile 中生效。

### 启动提示缺少数据源 URL 或用户名

检查 `yak.security.datasource.url` 和 `yak.security.datasource.username`。安全模块不会自动复用
宿主应用的 `spring.datasource`。

### 登录成功后下一次请求仍然是 401

确认客户端保存并回传了 Session Cookie；跨域前端还需要开启凭证传递，并在服务端正确配置 CORS。
多实例环境还应检查 Session 共享或会话粘滞配置。

### 接口返回 403

说明用户已经登录但缺少 `@RequiresPermission` 要求的权限。检查权限编码、角色权限关系、用户角色
关系以及数据所属的 `application-name` 是否一致。

### Flyway 报表已存在或校验失败

不要在 Flyway 之外重复导入初始化 SQL。已有旧库升级前应先备份，再基于实际基线建立迁移方案，
不要删除 Flyway 历史记录后直接重跑生产库。
