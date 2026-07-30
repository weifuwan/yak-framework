# Yak Security 租户模型

## 1. 两层隔离不能混用

Yak Security 已有的 `app_name` 是**宿主应用隔离键**。它解决多个应用共用安全库时的数据串扰问题，不代表企业业务租户。

本次新增的 tenant 是应用内部的**组织/客户/业务空间**。最终层次是：

```text
安全数据库
└── app_name（宿主应用）
    └── tenant（企业、组织、客户或业务空间）
        └── user membership（成员关系）
```

因此不能把现有 MyBatis `TenantLineInnerInterceptor` 的 `app_name` 直接替换成 `tenant_id`。

## 2. 为什么不直接复制 DolphinScheduler

DolphinScheduler 的租户首先是任务执行身份：租户包含 `tenantCode` 和队列，用户记录上直接保存一个 `tenantId`，资源目录和任务执行会使用该租户编码。

权限中心面向企业内部系统时，一个员工账号可能同时属于集团、子公司、项目组织或多个客户空间。Yak Security 因此采用：

- 用户表保存应用内唯一身份；
- 租户表保存业务隔离域；
- 用户租户关系表支持一名用户加入多个租户；
- 请求通过 `X-Yak-Tenant-Id` 或 `X-Yak-Tenant-Code` 选择当前租户；
- 未显式选择时优先使用默认租户；
- 宿主应用可覆盖 `TenantContextProvider`，从 OIDC、LDAP、网关声明或内部 IAM 中解析租户。

## 3. 外部系统对接

`yak_security_tenant` 提供：

- `external_system`
- `external_tenant_id`

二者构成应用内的外部租户唯一映射。`PUT /yak-security/api/v1/tenant/external` 按该映射执行幂等新增或更新，适合企业 IAM、组织中心或主数据平台同步。

对于复杂同步流程，宿主系统应实现适配器并调用 `TenantService`，不要把 LDAP/OIDC SDK 直接耦合到框架核心。

## 4. 当前 PR 的边界

本次先建立稳定的租户域、成员关系、请求上下文和外部目录映射。

角色、部门、授权项目和业务资源的 `tenant_id` 数据隔离建议在下一阶段按以下原则接入：

- 权限、菜单、资源类型保持应用级公共定义；
- 用户身份保持应用级唯一；
- 用户角色分配、部门、项目和资源授权进入租户域；
- 平台管理员权限与租户角色分开；
- 所有跨租户管理操作必须显式使用平台管理权限，不能依赖缺失租户头自动放行。

这样可以避免一次性修改现有 RBAC 查询链路造成权限回归。
