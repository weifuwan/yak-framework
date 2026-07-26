# Yak Security 接口文档

> 仓库：`weifuwan/yak-framework`  
> 模块：`yak-security`  
> 源码分支：`main`  
> 源码快照：`b39eabfa860d48a69a48a8ce4185b00bc5ba6bab`  
> 生成日期：`2026-07-26`  
> 接口规模：**11 个分组，69 个 Controller 方法**

## 1. 接口约定

- 统一前缀：`/yak-security/api/v1`
- 默认认证：服务端 HTTP Session。登录成功后由 Servlet 容器返回 Session Cookie，通常为 `JSESSIONID`，实际名称以宿主应用配置为准。
- 公开接口：登录、健康检查，以及 Swagger/OpenAPI 文档端点。
- 未登录：HTTP 状态码 `401`，响应体为 `Result`，业务码 `2001`。
- 项目隔离请求头：`X-YAK-SECURITY-PROJECT-ID`。是否必须取决于具体业务和宿主应用上下文。
- JSON 请求：含 `@RequestBody` 的接口使用 `Content-Type: application/json`。
- Swagger UI：默认 `/swagger-ui.html`；OpenAPI JSON：默认 `/v3/api-docs`。
- 默认配置：登录失败阈值 5 次、锁定 15 分钟、Session 空闲超时 30 分钟。

### 1.1 普通响应

```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

### 1.2 分页响应

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "bizData": [],
    "pagination": {
      "total": 0,
      "pages": 0,
      "pageNo": 1,
      "pageSize": 10
    }
  }
}
```

### 1.3 公共枚举

| 枚举 | 值 | 含义 |
| --- | --- | --- |
| UserCheckType | 1 | 用户名 |
| UserCheckType | 2 | 手机号 |
| UserCheckType | 3 | 邮箱 |
| ControlLevelCode | 0 | 无权限（查询结果可能返回） |
| ControlLevelCode | 1 | 查看权限 |
| ControlLevelCode | 2 | 管理权限 |

## 2. 接口清单

### 2.1 公共接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| GET | /yak-security/api/v1/common/heart | 健康检查 | 无 | Result<String> | 公开 | 返回固定健康检查文本。 |

### 2.2 账户认证接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| POST | /yak-security/api/v1/account/login | 用户登录 | AccountLoginDTO | Result<UserBriefVO> | 公开 | 登录成功后创建/刷新服务端 Session。 |
| POST | /yak-security/api/v1/account/logout | 用户退出登录 | 无 | Result<Boolean> | Session | 销毁当前 Session，成功时 data=true。 |

### 2.3 配置管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| POST | /yak-security/api/v1/config/list | 根据条件查询配置列表 | ConfigDTO | Result<List<ConfigVO>> | Session | ConfigDTO 作为筛选条件。 |
| POST | /yak-security/api/v1/config/page | 分页查询配置 | ConfigQueryDTO | PagingResult<ConfigVO> | Session | 默认 page=1、size=10。 |
| GET | /yak-security/api/v1/config/group/list | 查询全部配置分组 | 无 | Result<List<String>> | Session | 返回 valueGroup 列表。 |
| GET | /yak-security/api/v1/config/get?configId={configId} | 根据配置 ID 查询详情 | Query: configId(Long) | Result<ConfigVO> | Session |  |
| POST | /yak-security/api/v1/config/switch | 切换配置状态 | ConfigDTO{id,status} | Result<Void> | Session | 操作人从当前 Session 获取。 |
| DELETE | /yak-security/api/v1/config/del?id={id} | 删除配置 | Query: id(Long) | Result<Void> | Session |  |
| PUT | /yak-security/api/v1/config/add | 新增配置 | ConfigDTO | Result<Long> | Session | 保留 PUT 方式兼容现有前端。 |
| POST | /yak-security/api/v1/config/edit | 编辑配置 | ConfigDTO | Result<Void> | Session |  |

### 2.4 部门管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| GET | /yak-security/api/v1/dept/tree | 查询完整部门树 | 无 | Result<DeptTreeVO> | Session | 树节点子级字段为 childList。 |
| POST | /yak-security/api/v1/dept/import | 导入部门树 | List<DeptDTO> | Result<Void> | Session | 请求体本身是数组，支持递归子部门。 |

### 2.5 消息管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| GET | /yak-security/api/v1/message/list[/{readTag}] | 查询当前用户消息 | Path: readTag(Boolean，可选) | Result<List<MessageVO>> | Session | 省略 readTag 时查询全部；true/false 筛选已读状态。 |
| PUT | /yak-security/api/v1/message/switch | 批量切换消息已读状态 | List<Long> | Result<Void> | Session | 请求体为消息 ID 数组。 |

### 2.6 权限管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| GET | /yak-security/api/v1/permission/tree | 查询完整权限树 | 无 | Result<PermissionTreeVO> | Session |  |
| POST | /yak-security/api/v1/permission/import | 导入权限树 | List<PermissionDTO> | Result<Void> | Session | 请求体本身是数组，支持递归子权限。 |
| DELETE | /yak-security/api/v1/permission/{permissionId} | 删除权限及角色关联 | Path: permissionId(Long) | Result<Void> | Session | 同时清理角色权限关联。 |

### 2.7 项目管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| GET | /yak-security/api/v1/project/{id} | 根据项目 ID 查询详情 | Path: id(Long) | Result<ProjectVO> | Session |  |
| GET | /yak-security/api/v1/project/{id}/exist | 校验项目是否存在 | Path: id(Long) | Result<Boolean> | Session |  |
| PUT | /yak-security/api/v1/project/switch/{id} | 切换项目状态 | Path: id(Long) | Result<Void> | Session |  |
| PUT | /yak-security/api/v1/project | 更新项目 | ProjectSaveDTO | Result<Void> | Session | 更新时 id 必填。 |
| POST | /yak-security/api/v1/project | 创建项目 | ProjectSaveDTO | Result<ProjectVO> | Session |  |
| GET | /yak-security/api/v1/project/delete/check/{id} | 执行项目删除前校验 | Path: id(Long) | Result<ProjectDeleteCheckVO> | Session | 返回关联资源名称列表。 |
| DELETE | /yak-security/api/v1/project/{id} | 根据项目 ID 删除项目 | Path: id(Long) | Result<Void> | Session |  |
| POST | /yak-security/api/v1/project/page | 分页查询项目 | ProjectQueryDTO | PagingResult<ProjectVO> | Session |  |
| GET | /yak-security/api/v1/project/list | 查询全部项目简要信息 | 无 | Result<List<ProjectBriefVO>> | Session |  |
| PUT | /yak-security/api/v1/project/{id}/owner/{ownerId} | 添加项目负责人 | Path: id, ownerId | Result<Void> | Session |  |
| DELETE | /yak-security/api/v1/project/{id}/owner/{ownerId} | 删除项目负责人 | Path: id, ownerId | Result<Void> | Session |  |
| PUT | /yak-security/api/v1/project/{id}/user/{userId} | 添加项目用户 | Path: id, userId | Result<Void> | Session |  |
| DELETE | /yak-security/api/v1/project/{id}/user/{userId} | 删除项目用户 | Path: id, userId | Result<Void> | Session |  |
| GET | /yak-security/api/v1/project/unassigned?id={id} | 查询项目未分配用户 | Query: id(Long) | Result<List<UserBriefVO>> | Session |  |
| GET | /yak-security/api/v1/project/user/{userId} | 根据用户 ID 查询项目简要信息 | Path: userId(Long) | Result<List<ProjectBriefVO>> | Session |  |

### 2.8 资源权限管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| GET | /yak-security/api/v1/resource/type/list | 查询全部资源类型 | 无 | Result<List<ResourceTypeVO>> | Session |  |
| POST | /yak-security/api/v1/resource/type/import | 导入资源类型 | List<String> | Result<Void> | Session | 请求体为资源类型名称数组。 |
| GET | /yak-security/api/v1/resource/vpc/status | 查询资源查看权限控制状态 | 无 | Result<Boolean> | Session |  |
| PUT | /yak-security/api/v1/resource/vpc/switch | 切换资源查看权限控制状态 | 无 | Result<Void> | Session |  |
| POST | /yak-security/api/v1/resource/mbu/list | 查询按用户管理的资源权限数据 | MByUDataQueryDTO | Result<List<MByUDataVO>> | Session |  |
| POST | /yak-security/api/v1/resource/mbr/list | 查询按资源管理的用户权限数据 | MByRDataQueryDTO | Result<List<MByRDataVO>> | Session |  |
| POST | /yak-security/api/v1/resource/mbr/page | 分页查询按资源管理的权限信息 | MByRQueryDTO | PagingResult<MByRVO> | Session |  |
| POST | /yak-security/api/v1/resource/mbu/page | 分页查询按用户管理的权限信息 | MByUQueryDTO | PagingResult<MByUVO> | Session |  |
| POST | /yak-security/api/v1/resource/permission/mbr/assign | 为多个用户分配资源权限 | AssignToManyUserDTO | Result<Void> | Session |  |
| POST | /yak-security/api/v1/resource/permission/mbu/assign | 为单个用户分配资源权限 | AssignToOneUserDTO | Result<Void> | Session |  |
| POST | /yak-security/api/v1/resource/permission/assign/batch | 批量分配资源权限 | BatchAssignDTO | Result<Void> | Session |  |
| POST | /yak-security/api/v1/resource/control/level | 查询资源权限控制级别 | ControlLevelQueryDTO | Result<Integer> | Session | 返回 0 无权限、1 查看、2 管理。 |

### 2.9 角色管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| GET | /yak-security/api/v1/role/{id} | 根据角色 ID 查询详情 | Path: id(Long) | Result<RoleVO> | Session |  |
| PUT | /yak-security/api/v1/role | 更新角色 | RoleSaveDTO | Result<Void> | Session | 更新时 id 必填。 |
| POST | /yak-security/api/v1/role | 创建角色 | RoleSaveDTO | Result<Void> | Session |  |
| DELETE | /yak-security/api/v1/role/delete/check/{id} | 执行角色删除前校验 | Path: id(Long) | Result<RoleDeleteCheckVO> | Session | 兼容历史前端，查询动作使用 DELETE。 |
| DELETE | /yak-security/api/v1/role/{id}/user/{userId} | 从角色中删除用户 | Path: id, userId | Result<Void> | Session |  |
| DELETE | /yak-security/api/v1/role/{id} | 根据角色 ID 删除角色 | Path: id(Long) | Result<Void> | Session |  |
| POST | /yak-security/api/v1/role/page | 分页查询角色 | RoleQueryDTO | PagingResult<RoleVO> | Session |  |
| POST | /yak-security/api/v1/role/assign | 分配角色或为角色分配用户 | RoleAssignDTO | Result<Void> | Session | flag=true：id 为用户 ID、idList 为角色 ID；flag=false：id 为角色 ID、idList 为用户 ID。 |
| GET | /yak-security/api/v1/role/assign/list/{roleId} | 根据角色 ID 查询用户分配信息 | Path: roleId(Long) | Result<List<AssignInfoVO>> | Session |  |
| GET | /yak-security/api/v1/role/list[/{roleName}] | 根据角色名称查询角色 | Path: roleName(String，可选) | Result<List<RoleBriefVO>> | Session | 省略 roleName 时查询全部简要角色。 |

### 2.10 用户管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| GET | /yak-security/api/v1/user/{type}/{value}/check | 校验用户字段是否可用 | Path: type, value | Result<Void> | Session | type：1 用户名、2 手机号、3 邮箱。 |
| GET | /yak-security/api/v1/user?ids={jsonArray} | 根据用户 ID 集合批量查询详情 | Query: ids(String) | Result<List<UserVO>> | Session | ids 是 JSON 数组字符串，例如 [1,2,3]，建议 URL 编码。 |
| GET | /yak-security/api/v1/user/{id} | 根据用户 ID 查询详情 | Path: id(Long) | Result<UserVO> | Session |  |
| POST | /yak-security/api/v1/user/page | 分页查询用户 | UserQueryDTO | PagingResult<UserVO> | Session | 分页列表中的手机号会脱敏。 |
| GET | /yak-security/api/v1/user/list/dept/{deptId} | 根据部门 ID 查询用户 | Path: deptId(Long) | Result<List<UserBriefVO>> | Session | 包含该部门及其子部门用户。 |
| GET | /yak-security/api/v1/user/list/role/{roleId} | 根据角色 ID 查询用户 | Path: roleId(Long) | Result<List<UserBriefVO>> | Session |  |
| GET | /yak-security/api/v1/user/assign/list/{userId} | 查询用户的角色分配信息 | Path: userId(Long) | Result<List<AssignInfoVO>> | Session |  |
| GET | /yak-security/api/v1/user/list/{keyword} | 按用户名或真实姓名模糊查询 | Path: keyword(String) | Result<List<UserBriefVO>> | Session |  |
| PUT | /yak-security/api/v1/user/add | 新增用户 | UserDTO | Result<Void> | Session | 保留 PUT 方式兼容现有前端；密码必填。 |
| POST | /yak-security/api/v1/user/edit | 编辑用户 | UserDTO | Result<Void> | Session | 以 userName 定位用户；pw 为空表示不修改密码。 |
| DELETE | /yak-security/api/v1/user/{id} | 根据用户 ID 删除用户 | Path: id(Long) | Result<Void> | Session | 同步清理角色、项目和资源权限关系。 |

### 2.11 操作日志管理接口

| 方法 | 路径 | 接口名称 | 请求 | 响应 | 认证 | 说明 |
| --- | --- | --- | --- | --- | --- | --- |
| POST | /yak-security/api/v1/oplog/page | 分页查询操作日志 | OplogQueryDTO | PagingResult<OplogVO> | Session |  |
| GET | /yak-security/api/v1/oplog/{id} | 根据操作日志 ID 查询详情 | Path: id(Long) | Result<OplogVO> | Session |  |
| GET | /yak-security/api/v1/oplog/type/list | 查询全部操作目标类型 | 无 | Result<List<String>> | Session |  |

## 3. 请求模型

### 3.1 `PageParamDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| page | int | 否 | 当前页码，默认 1 |
| size | int | 否 | 每页记录数，默认 10 |

### 3.2 `AccountLoginDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| userName | String | 是 | 用户名，不能为空 |
| pw | String | 是 | 密码，不能为空；必须通过 HTTPS 传输 |

### 3.3 `ConfigDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | Long | 按接口 | 配置 ID；编辑、切换状态时使用 |
| valueGroup | String | 按接口 | 配置分组 |
| valueName | String | 按接口 | 配置名称 |
| value | String | 按接口 | 配置值 |
| status | Integer | 按接口 | 配置状态；切换状态时使用 |
| memo | String | 否 | 备注 |
| operator | String | 否 | 查询过滤字段；变更操作的真实操作人从 Session 获取 |

### 3.4 `ConfigQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| page | int | 否 | 默认 1 |
| size | int | 否 | 默认 10 |
| valueGroup | String | 否 | 配置分组 |
| valueName | String | 否 | 配置名称 |
| status | Integer | 否 | 状态 |
| memo | String | 否 | 备注 |
| operator | String | 否 | 操作人 |

### 3.5 `DeptDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| deptName | String | 建议是 | 部门名称 |
| description | String | 否 | 描述 |
| childDeptDTOList | List<DeptDTO> | 否 | 子部门，递归结构 |

### 3.6 `PermissionDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| permissionCode | String | 建议是 | 权限编码 |
| permissionName | String | 建议是 | 权限名称 |
| description | String | 否 | 描述 |
| childPermissionDTOList | List<PermissionDTO> | 否 | 子权限，递归结构 |

### 3.7 `ProjectQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| page | int | 否 | 默认 1 |
| size | int | 否 | 默认 10 |
| projectName | String | 否 | 项目名称 |
| projectCode | String | 否 | 项目编码 |
| chargeUsername | String | 否 | 项目负责人用户名 |
| deptId | Long | 否 | 所属部门 ID |
| running | Boolean | 否 | 运行状态 |

### 3.8 `ProjectSaveDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | Long | 更新时是 | 项目 ID |
| projectName | String | 是 | 项目名称 |
| userIdList | List<Long> | 否 | 项目用户 ID 列表 |
| ownerIdList | List<Long> | 是 | 项目负责人 ID 列表 |
| description | String | 是 | 项目描述 |
| running | Boolean | 否 | 是否运行 |
| deptId | Long | 是 | 所属部门 ID |

### 3.9 `RoleAssignDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | Long | 是 | flag=true 时为用户 ID；flag=false 时为角色 ID |
| idList | List<Long> | 是 | flag=true 时为角色 ID 列表；flag=false 时为用户 ID 列表 |
| flag | Boolean | 是 | true 为用户分配角色；false 为角色分配用户 |

### 3.10 `RoleQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| page | int | 否 | 默认 1 |
| size | int | 否 | 默认 10 |
| roleCode | String | 否 | 角色编码 |
| id | Long | 否 | 角色 ID |
| roleName | String | 否 | 角色名称 |
| description | String | 否 | 描述 |

### 3.11 `RoleSaveDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | Long | 更新时是 | 角色 ID |
| roleName | String | 是 | 角色名称 |
| description | String | 是 | 角色描述 |
| permissionIdList | List<Long> | 是 | 权限 ID 列表 |

### 3.12 `UserDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| userName | String | 是 | 用户名；新增和编辑均用于唯一定位 |
| pw | String | 新增时是 | 编辑时为空表示不修改密码 |
| realName | String | 否 | 真实姓名 |
| phone | String | 否 | 手机号 |
| email | String | 否 | 邮箱 |
| roleIds | List<Long> | 否 | 角色 ID 列表 |

### 3.13 `UserQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| page | int | 否 | 默认 1 |
| size | int | 否 | 默认 10 |
| id | Long | 否 | 用户 ID |
| roleId | Long | 否 | 角色 ID |
| userName | String | 否 | 用户名 |
| realName | String | 否 | 真实姓名 |

### 3.14 `OplogQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| page | int | 否 | 默认 1 |
| size | int | 否 | 默认 10 |
| operateType | String | 否 | 操作类型 |
| detail | String | 否 | 操作详情 |
| operator | String | 否 | 操作人 |
| target | String | 否 | 操作目标 |
| targetType | String | 否 | 操作目标类型 |
| operationMethods | String | 否 | 操作方法列表/筛选文本 |
| startTime | Long | 否 | 开始时间戳 |
| endTime | Long | 否 | 结束时间戳 |

### 3.15 `AssignToManyUserDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| projectId | Long | 按层级 | 项目 ID |
| resourceTypeId | Long | 按层级 | 资源类型 ID |
| resourceId | Long | 是 | 资源 ID |
| userIdList | List<Long> | 否 | 待分配用户 ID |
| excludeUserIdList | List<Long> | 否 | 排除用户 ID |
| controlLevel | Integer | 是 | 1 查看、2 管理 |

### 3.16 `AssignToOneUserDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| userId | Long | 是 | 用户 ID |
| projectId | Long | 按层级 | 项目 ID |
| resourceTypeId | Long | 按层级 | 资源类型 ID |
| idList | List<Long> | 否 | 资源 ID 列表 |
| excludeIdList | List<Long> | 否 | 排除资源 ID |
| controlLevel | Integer | 是 | 1 查看、2 管理 |

### 3.17 `BatchAssignDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| userIdList | List<Long> | 是 | 用户 ID 列表 |
| projectId | Long | 按层级 | 项目 ID |
| resourceTypeId | Long | 按层级 | 资源类型 ID |
| idList | List<Long> | 是 | 资源 ID 列表 |
| controlLevel | Integer | 是 | 1 查看、2 管理 |
| assignFlag | Boolean | 是 | 分配标记 |

### 3.18 `ControlLevelQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| userId | Long | 是 | 用户 ID |
| projectId | Long | 按层级 | 项目 ID |
| resourceTypeId | Long | 按层级 | 资源类型 ID |
| resourceId | Long | 是 | 资源 ID |

### 3.19 `MByRDataQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| projectId | Long | 按层级 | 项目 ID |
| resourceTypeId | Long | 按层级 | 资源类型 ID |
| resourceId | Long | 按层级 | 资源 ID |
| controlLevel | Integer | 否 | 管控级别 |
| batch | Boolean | 否 | 是否批量查询 |

### 3.20 `MByRQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| page | int | 否 | 默认 1 |
| size | int | 否 | 默认 10 |
| projectId | Long | 按层级 | 项目 ID |
| resourceTypeId | Long | 按层级 | 资源类型 ID |
| showLevel | Integer | 是 | 展示级别 1~3；2 要求 projectId，3 要求 projectId 和 resourceTypeId |
| name | String | 否 | 资源名称筛选 |

### 3.21 `MByUDataQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| userId | Long | 是 | 用户 ID |
| projectId | Long | 按层级 | 项目 ID |
| resourceTypeId | Long | 按层级 | 资源类型 ID |
| showLevel | Integer | 是 | 展示级别 1~3 |
| controlLevel | Integer | 否 | 管控级别 |
| batch | Boolean | 否 | 是否批量查询 |

### 3.22 `MByUQueryDTO`

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| page | int | 否 | 默认 1 |
| size | int | 否 | 默认 10 |
| deptId | Long | 否 | 部门 ID |
| deptName | String | 否 | 部门名称 |
| userName | String | 否 | 用户名 |
| realName | String | 否 | 真实姓名 |

## 4. 响应模型

### 4.1 `UserBriefVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 用户 ID |
| userName | String | 用户名 |
| realName | String | 真实姓名 |
| deptId | Long | 部门 ID |
| phone | String | 手机号 |
| email | String | 邮箱 |
| roleList | List<String> | 角色名称列表 |

### 4.2 `UserVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 用户 ID |
| userName | String | 用户名 |
| realName | String | 真实姓名 |
| phone | String | 手机号；分页接口会脱敏 |
| email | String | 邮箱 |
| updateTime | Date | 更新时间 |
| createTime | Date | 创建时间 |
| roleList | List<RoleBriefVO> | 角色列表 |
| permissionTreeVO | PermissionTreeVO | 权限树 |
| projectList | List<ProjectBriefVO> | 项目列表 |

### 4.3 `RoleBriefVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 角色 ID |
| roleName | String | 角色名称 |

### 4.4 `RoleVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 角色 ID |
| roleName | String | 角色名称 |
| roleCode | String | 角色编码 |
| description | String | 描述 |
| authedUserCnt | Integer | 已授权用户数量 |
| authedUsers | List<String> | 已授权用户名 |
| lastReviser | String | 最后修改人 |
| createTime | Date | 创建时间 |
| updateTime | Date | 更新时间 |
| permissionTreeVO | PermissionTreeVO | 权限树，可能为 null |

### 4.5 `AssignInfoVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 对象 ID |
| name | String | 名称 |
| has | Boolean | 是否已分配 |

### 4.6 `RoleDeleteCheckVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| roleId | Long | 角色 ID |
| userNameList | List<String> | 关联用户名列表 |

### 4.7 `ProjectBriefVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 项目 ID |
| projectCode | String | 项目编码 |
| projectName | String | 项目名称 |

### 4.8 `ProjectVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 项目 ID |
| projectCode | String | 项目编码 |
| projectName | String | 项目名称 |
| userList | List<UserBriefVO> | 项目用户 |
| ownerList | List<UserBriefVO> | 负责人 |
| description | String | 描述 |
| running | Boolean | 运行状态 |
| deptList | List<DeptBriefVO> | 部门列表 |
| deptId | Long | 部门 ID |
| createTime | Date | 创建时间 |

### 4.9 `ProjectDeleteCheckVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| projectId | Long | 项目 ID |
| resourceNameList | List<String> | 关联资源名称 |

### 4.10 `DeptBriefVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 部门 ID |
| deptName | String | 部门名称 |
| parentId | Long | 父部门 ID |

### 4.11 `DeptTreeVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 部门 ID |
| deptName | String | 部门名称 |
| description | String | 描述 |
| parentId | Long | 父节点 ID |
| leaf | Boolean | 是否叶子节点 |
| childList | List<DeptTreeVO> | 子节点 |

### 4.12 `PermissionTreeVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 权限 ID |
| has | Boolean | 是否已分配 |
| permissionName | String | 权限名称 |
| parentId | Long | 父节点 ID |
| leaf | Boolean | 是否叶子节点 |
| childList | List<PermissionTreeVO> | 子节点 |

### 4.13 `ConfigVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 配置 ID |
| valueGroup | String | 配置分组 |
| valueName | String | 配置名称 |
| value | String | 配置值 |
| status | Integer | 状态 |
| memo | String | 备注 |
| createTime | Date | 创建时间 |
| updateTime | Date | 更新时间 |
| operator | String | 操作人 |

### 4.14 `MessageVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 消息 ID |
| title | String | 标题 |
| content | String | 内容 |
| readTag | Boolean | 是否已读 |
| createTime | Long | 创建时间戳 |
| oplogId | Long | 关联操作日志 ID |

### 4.15 `OplogVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 日志 ID |
| operatorIp | String | 操作人 IP |
| operator | String | 操作人 |
| operateType | String | 操作类型 |
| target | String | 操作目标 |
| targetType | String | 目标类型 |
| detail | String | 操作详情 |
| createTime | Date | 创建时间 |
| updateTime | Date | 更新时间 |

### 4.16 `ResourceTypeVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 资源类型 ID |
| typeName | String | 资源类型名称 |

### 4.17 `MByRDataVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| userId | Long | 用户 ID |
| userName | String | 用户名 |
| realName | String | 真实姓名 |
| hasLevel | Integer | 授权级别 |

### 4.18 `MByRVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| adminUserCnt | Integer | 管理员用户数量 |
| viewUserCnt | Integer | 只读用户数量 |
| projectId | Long | 项目 ID |
| projectCode | String | 项目编码 |
| projectName | String | 项目名称 |
| resourceTypeId | Long | 资源类型 ID |
| resourceTypeName | String | 资源类型名称 |
| resourceId | Long | 资源 ID |
| resourceName | String | 资源名称 |

### 4.19 `MByUDataVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 资源/层级对象 ID |
| name | String | 名称 |
| hasLevel | Integer | 授权级别 |

### 4.20 `MByUVO`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| userId | Long | 用户 ID |
| userName | String | 用户名 |
| realName | String | 真实姓名 |
| deptList | List<DeptBriefVO> | 部门列表 |
| adminResourceCnt | Integer | 管理权限资源数量 |
| viewResourceCnt | Integer | 查看权限资源数量 |

## 5. 调用示例

### 5.1 登录并保存 Session Cookie

```bash
curl -i -c cookies.txt \
  -H "Content-Type: application/json" \
  -d '{"userName":"admin","pw":"your-password"}' \
  http://localhost:8080/yak-security/api/v1/account/login

curl -b cookies.txt \
  http://localhost:8080/yak-security/api/v1/user/1
```

### 5.2 新增用户

```http
PUT /yak-security/api/v1/user/add
Content-Type: application/json
Cookie: JSESSIONID=<session-id>

{
  "userName": "zhangsan",
  "pw": "StrongPassword",
  "realName": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "roleIds": [1, 2]
}
```

### 5.3 编辑用户

```http
POST /yak-security/api/v1/user/edit
Content-Type: application/json
Cookie: JSESSIONID=<session-id>

{
  "userName": "zhangsan",
  "pw": "",
  "realName": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "roleIds": [2]
}
```

`pw` 为空表示保持原密码；接口通过 `userName` 定位待编辑用户。

### 5.4 角色分配

```http
POST /yak-security/api/v1/role/assign
Content-Type: application/json
Cookie: JSESSIONID=<session-id>

{
  "id": 1001,
  "idList": [1, 2, 3],
  "flag": true
}
```

上例表示：为用户 `1001` 设置角色 `1、2、3`。`flag=false` 时，`id` 表示角色 ID，`idList` 表示用户 ID 列表。

### 5.5 创建项目

```http
POST /yak-security/api/v1/project
Content-Type: application/json
Cookie: JSESSIONID=<session-id>

{
  "projectName": "Yak Ops",
  "userIdList": [1001, 1002],
  "ownerIdList": [1001],
  "description": "Yak 运维项目",
  "running": true,
  "deptId": 10
}
```

### 5.6 为多个用户分配资源权限

```http
POST /yak-security/api/v1/resource/permission/mbr/assign
Content-Type: application/json
Cookie: JSESSIONID=<session-id>
X-YAK-SECURITY-PROJECT-ID: 100

{
  "projectId": 100,
  "resourceTypeId": 5,
  "resourceId": 9001,
  "userIdList": [1001, 1002],
  "excludeUserIdList": [],
  "controlLevel": 1
}
```

## 6. 错误码

| 业务码 | 说明 |
| --- | --- |
| 200 | 成功 |
| 999 | 失败 |
| 1001 | 参数无效 |
| 1002 | 参数为空 |
| 1003 | 参数 id 为空 |
| 1004 | 参数类型错误 |
| 1005 | 参数缺失 |
| 1006 | 参数长度不正确 |
| 1007 | 参数错误 |
| 2001 | 用户未登录 |
| 2002 | 账号已过期 |
| 2003 | 密码错误 |
| 2004 | 密码过期 |
| 2005 | 账号不可用 |
| 2006 | 账号被锁定 |
| 2007 | 账号不存在 |
| 2008 | 账号已存在 |
| 2009 | 账号下线 |
| 2010 | 用户注册失败 |
| 2011 | 手机号已存在 |
| 2012 | 邮箱格式错误 |
| 2013 | 邮箱已存在 |
| 2014 | 密码解密出错 |
| 2015 | 密码解密出错（枚举名为 USER_PASSWORD_ENCODE_ERROR） |
| 2016 | 用户 ID 不可为空 |
| 2017 | 用户不存在 |
| 2018 | 用户更新失败 |
| 2019 | 手机号格式错误 |
| 2020 | 用户名格式错误 |
| 2021 | 用户名已经存在 |
| 3001 | 没有权限 |
| 4001 | 角色内部错误 |
| 4002 | 角色不存在 |
| 4003 | 有用户已绑定该角色 |
| 4004 | 角色名已存在 |
| 4005 | 角色名不可为空 |
| 4006 | 角色描述不可为空 |
| 4007 | 角色权限不可为空 |
| 4008 | 角色分配 flag 不可为空 |
| 4009 | 角色 ID 不可为空 |
| 5001 | 项目名已存在 |
| 5002 | 项目不存在 |
| 5003 | 项目未运行 |
| 5004 | 项目 ID 不可为空 |
| 5005 | 项目名不可为空 |
| 5006 | 项目描述不可为空 |
| 5007 | 项目使用部门不可为空 |
| 5008 | 项目负责人不可为空 |
| 5009 | 项目存在所属资源，不能删除 |
| 6001 | 操作日志不存在 |
| 7001 | 消息不存在 |
| 8001 | 获取权限数据异常 |
| 9001 | 获取部门数据异常 |
| 10001 | 资源权限分配异常：资源 ID 非空时资源类型 ID 不可为空 |
| 10002 | 资源权限分配异常：资源类型 ID 非空时项目 ID 不可为空 |
| 10003 | 展示级别无效，要求 1 <= showLevel <= 3 |
| 10004 | 2 级展示时项目 ID 不可为空；源码中该数值也被 RESOURCE_DUPLICATION 复用 |
| 10005 | 3 级展示时项目 ID 或资源类型 ID 不可为空 |
| 10006 | 资源权限批量分配标识不可为空 |
| 10007 | 资源权限控制级别无效，要求 1 <= controlLevel <= 2 |
| 10008 | 资源类型 ID 不可为空 |
| 10009 | 资源 ID 不可为空 |
| 10010 | 资源类型不存在 |

## 7. 配置参考

```yaml
yak:
  security:
    enabled: true
    database-enabled: true
    web-enabled: true
    authentication-enabled: true
    application-name: demo
    public-paths:
      - /yak-security/api/v1/account/login
      - /yak-security/api/v1/common/heart
      - /v3/api-docs/**
      - /swagger-ui/**
      - /swagger-ui.html
    login:
      max-failure-count: 5
      lock-duration: 15m
      hide-account-not-found: true
    session:
      timeout: 30m
    permission-cache:
      enabled: true
      ttl-minutes: 20
      maximum-size: 10000
```

生产环境应启用 HTTPS，并配置 Session Cookie 的 `HttpOnly`、`Secure` 和 `SameSite`。

## 8. 源码现状与接入注意事项

1. `RoleAssignDTO.flag` 决定 `id/idList` 的语义，前端必须严格区分两种模式。
2. `GET /user?ids=...` 的 `ids` 不是重复 Query 参数，而是 JSON 数组字符串；建议 URL 编码。
3. `DELETE /role/delete/check/{id}` 实际是删除前查询，为兼容历史前端保留了 DELETE 方法。
4. `PUT /config/add` 和 `PUT /user/add` 是兼容性接口，不是常规 REST 命名。
5. `UserDTO` 没有 `id` 字段；编辑接口按 `userName` 查询用户，空密码表示不改密码。
6. `MessageVO.createTime` 为 `Long`，其他多数时间字段为 `Date`；客户端应做好两种格式的兼容。
7. `ResultCode` 中数值 `10004` 存在重复含义，客户端不宜仅凭该业务码判断具体资源权限错误。
8. 除登录 DTO 使用 `@Valid` 外，多数 Controller 没有显式 Bean Validation；部分必填规则由 Service 层校验。
9. 日期字段的最终 JSON 格式由宿主应用 Jackson 配置决定。
10. 默认认证不信任客户端传入的用户 Header/Cookie 作为身份依据；外部 SSO 应通过可信服务端扩展点接入。

## 9. 源码依据

- `yak-security/src/main/java/io/yak/framework/security/controller/v1/*Controller.java`
- `yak-security/src/main/java/io/yak/framework/security/common/dto/**`
- `yak-security/src/main/java/io/yak/framework/security/common/vo/**`
- `yak-common/src/main/java/io/yak/framework/common/Result.java`
- `yak-common/src/main/java/io/yak/framework/common/PagingResult.java`
- `yak-security/src/main/java/io/yak/framework/security/config/YakSecurityProperties.java`
- `yak-security/README.md`
