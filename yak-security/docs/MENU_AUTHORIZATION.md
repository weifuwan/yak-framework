# 菜单与按钮权限

Yak Security 将页面入口和页面内操作拆成两层授权：

- `yak_security_menu`：保存稳定菜单编码、父子关系、路由、排序及菜单对应的读取权限；
- `yak_security_role_menu`：保存角色可见、可访问的菜单；
- `yak_security_permission` / `yak_security_role_permission`：保存新增、编辑、删除、执行等按钮或操作权限；
- `yak_security_permission.menu_code`：声明一个按钮权限属于哪个菜单。

前端页面组件仍由代码注册，数据库不保存可执行组件名称。菜单编码应与宿主应用的前端导航元数据保持一致，避免数据库内容被用于任意组件加载。

## 包含关系

权限关系遵循以下单向规则：

1. 菜单权限只授予页面访问和该菜单绑定的读取权限；
2. 菜单权限不会自动授予页面内的任何按钮权限；
3. 按钮权限会自动包含所属菜单及其父级目录；
4. 取消菜单授权时，角色配置界面会同步移除该菜单下的按钮权限；
5. 后端保存角色时会再次根据按钮权限补齐菜单，不能依赖前端保证数据一致；
6. 登录态计算还会从历史按钮权限推导菜单，避免旧数据出现“有按钮权限但看不到页面”。

因此：

```text
新增用户按钮权限  => 用户管理菜单 => 系统管理目录
用户管理菜单      !=> 新增用户、编辑用户、删除用户
```

## 角色配置协议

角色权限树按“目录 → 菜单 → 按钮”展示，前端仍提交同一个 `permissionIdList`：

- 正数是按钮或其他普通权限 ID；
- `-1` 是菜单虚拟分组，不持久化；
- 小于 `-1` 的值由框架解码为菜单 ID，并写入 `yak_security_role_menu`。

菜单绑定的读取权限由角色菜单关系自动推导，不再作为重复按钮展示。取消菜单时，页面入口、直接 URL 访问和后端读取接口会一起失效。

## 声明按钮所属菜单

业务接口可以直接在声明式权限中指定稳定菜单编码：

```java
@RequiresPermission("order:create")
@YakPermission(
    code = "order:create",
    name = "新增订单",
    group = "订单管理",
    menuCode = "order-list",
    description = "创建订单")
@PostMapping("/orders")
public void createOrder() {
}
```

非控制器权限可以使用 `PermissionDefinition.Item.ofMenu(...)`：

```java
PermissionDefinition.Item.ofMenu(
    "order:export",
    "导出订单",
    "导出订单列表",
    "order-list");
```

同一权限编码若声明了不同的菜单归属，应用启动会直接失败，避免静默写入错误关系。未显式声明 `menuCode` 时，框架会保留 Flyway 或宿主应用已经维护的菜单绑定。

## 数据归属

框架基线负责通用菜单表、角色菜单关系表，以及 Yak Security 自有的 `system` 系统管理权限与菜单目录；不会包含宿主应用的业务菜单、路由或权限编码。

宿主应用应在自己的资源目录中提供 Yak Security 迁移脚本：

```text
src/main/resources/yak-security/db/migration/
```

宿主脚本负责：

1. 初始化本应用的业务权限目录；
2. 初始化本应用的业务菜单和路由元数据；
3. 为历史或 SQL 管理的按钮权限设置 `menu_code`；
4. 根据历史按钮权限回填角色菜单；
5. 按应用需要处理超级管理员或旧版本兼容数据。

Yak Security 的独立 Flyway 会扫描同一 classpath 位置，因此应用迁移会和框架迁移在同一个安全数据源中按版本顺序执行。宿主迁移版本必须高于框架基线并避免与其他依赖中的版本号冲突。
