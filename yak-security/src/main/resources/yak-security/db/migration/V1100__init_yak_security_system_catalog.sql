-- Yak Security 自有的系统管理权限、菜单及历史授权兼容数据。
-- 宿主应用只需要在自己的迁移脚本中维护业务权限和业务菜单。
-- 使用高于宿主 V1000 的版本，确保已经执行过宿主目录的数据库仍能继续升级。

-- 1. 系统管理权限分组。
INSERT INTO yak_security_permission
(permission_code,permission_name,parent_id,leaf,level,description,active,declared,app_name)
VALUES
('security','系统管理',0,0,1,'Yak Security 系统管理权限',1,0,'${appName}')
ON DUPLICATE KEY UPDATE
permission_name=VALUES(permission_name),
parent_id=VALUES(parent_id),
leaf=VALUES(leaf),
level=VALUES(level),
description=VALUES(description),
active=VALUES(active),
declared=VALUES(declared);

-- 2. 系统管理权限叶子节点。
-- 已有数据库中的 security:root 可能是旧版根节点，本迁移会将其归一为 security 分组下的叶子权限。
INSERT INTO yak_security_permission
(permission_code,permission_name,parent_id,leaf,level,description,active,declared,app_name)
SELECT item.permission_code,
       item.permission_name,
       parent.id,
       1,
       2,
       item.description,
       1,
       0,
       parent.app_name
FROM yak_security_permission parent
JOIN (
    SELECT 'security:root' permission_code,'超级管理员' permission_name,'拥有当前应用全部权限' description
    UNION ALL SELECT 'security:user:read','查看用户管理','查看用户管理页面及接口'
    UNION ALL SELECT 'security:user:create','新增用户','创建系统用户'
    UNION ALL SELECT 'security:user:update','编辑用户','修改系统用户资料'
    UNION ALL SELECT 'security:user:reset-password','重置用户密码','管理员重置用户密码'
    UNION ALL SELECT 'security:user:delete','删除用户','删除系统用户'
    UNION ALL SELECT 'security:role:read','查看角色管理','查看角色管理页面及接口'
    UNION ALL SELECT 'security:role:create','新增角色','创建系统角色'
    UNION ALL SELECT 'security:role:update','编辑角色','修改角色资料和权限'
    UNION ALL SELECT 'security:role:assign','分配角色关系','为用户分配角色或为角色分配用户'
    UNION ALL SELECT 'security:role:delete','删除角色','删除系统角色'
    UNION ALL SELECT 'security:permission:read','查看权限管理','查看权限管理页面及接口'
    UNION ALL SELECT 'security:permission:import','导入权限','导入手工权限目录'
    UNION ALL SELECT 'security:permission:delete','删除权限','删除手工权限及角色关联'
    UNION ALL SELECT 'security:department:read','查看部门管理','查看部门管理页面及接口'
    UNION ALL SELECT 'security:project:read','查看授权项目','查看 Yak Security 授权项目页面及接口'
    UNION ALL SELECT 'security:resource-permission:read','查看资源授权','查看资源授权页面及接口'
    UNION ALL SELECT 'security:config:read','查看系统配置','查看系统配置页面及接口'
    UNION ALL SELECT 'security:operation-log:read','查看操作日志','查看操作日志页面及接口'
) item
WHERE parent.permission_code='security'
  AND parent.app_name='${appName}'
  AND parent.is_delete=0
ON DUPLICATE KEY UPDATE
permission_name=VALUES(permission_name),
parent_id=VALUES(parent_id),
leaf=VALUES(leaf),
level=VALUES(level),
description=VALUES(description),
active=VALUES(active),
declared=VALUES(declared);

-- 3. Yak Security 系统管理菜单目录。
INSERT INTO yak_security_menu
(menu_code,menu_name,parent_code,route_path,icon_key,menu_type,sort_order,visible,active,required_permission_code,description,app_name)
VALUES
('system','系统管理',NULL,NULL,'system',1,60,1,1,NULL,'Yak Security 系统管理入口','${appName}'),
('system-users','用户管理','system','/system/users','system',2,10,1,1,'security:user:read','用户管理','${appName}'),
('system-roles','角色管理','system','/system/roles','system',2,20,1,1,'security:role:read','角色及授权管理','${appName}'),
('system-permissions','权限管理','system','/system/permissions','system',2,30,1,1,'security:permission:read','操作权限管理','${appName}'),
('system-departments','部门管理','system','/system/departments','system',2,40,1,1,'security:department:read','部门管理','${appName}'),
('system-security-projects','Security 授权项目','system','/system/projects','system',2,50,1,1,'security:project:read','安全项目管理','${appName}'),
('system-resource-permissions','资源授权','system','/system/resource-permissions','system',2,60,1,1,'security:resource-permission:read','资源级授权管理','${appName}'),
('system-configs','系统配置','system','/system/configs','system',2,70,1,1,'security:config:read','系统配置管理','${appName}'),
('system-operation-logs','操作日志','system','/system/oplogs','system',2,80,1,1,'security:operation-log:read','操作日志查询','${appName}')
ON DUPLICATE KEY UPDATE
menu_name=VALUES(menu_name),
parent_code=VALUES(parent_code),
route_path=VALUES(route_path),
icon_key=VALUES(icon_key),
menu_type=VALUES(menu_type),
sort_order=VALUES(sort_order),
visible=VALUES(visible),
active=VALUES(active),
required_permission_code=VALUES(required_permission_code),
description=VALUES(description);

-- 4. 为历史数据库中的系统管理员补齐 root 权限。
INSERT IGNORE INTO yak_security_role_permission(role_id,permission_id,app_name)
SELECT role_row.id,permission_row.id,role_row.app_name
FROM yak_security_role role_row
JOIN yak_security_permission permission_row
  ON permission_row.permission_code='security:root'
 AND permission_row.app_name=role_row.app_name
 AND permission_row.is_delete=0
WHERE role_row.role_name='系统管理员'
  AND role_row.app_name='${appName}'
  AND role_row.is_delete=0;

-- 5. 根据已有读取权限回填系统菜单，升级后不丢失原页面入口。
INSERT IGNORE INTO yak_security_role_menu(role_id,menu_id,app_name)
SELECT DISTINCT role_permission.role_id,menu_row.id,role_permission.app_name
FROM yak_security_role_permission role_permission
JOIN yak_security_permission permission_row
  ON permission_row.id=role_permission.permission_id
 AND permission_row.app_name=role_permission.app_name
 AND permission_row.is_delete=0
JOIN yak_security_menu menu_row
  ON menu_row.required_permission_code=permission_row.permission_code
 AND menu_row.app_name=role_permission.app_name
 AND menu_row.parent_code='system'
 AND menu_row.is_delete=0
WHERE role_permission.app_name='${appName}'
  AND role_permission.is_delete=0;

-- 6. root 角色拥有完整的 Yak Security 系统管理菜单。
INSERT IGNORE INTO yak_security_role_menu(role_id,menu_id,app_name)
SELECT DISTINCT role_permission.role_id,menu_row.id,role_permission.app_name
FROM yak_security_role_permission role_permission
JOIN yak_security_permission permission_row
  ON permission_row.id=role_permission.permission_id
 AND permission_row.app_name=role_permission.app_name
 AND permission_row.permission_code='security:root'
 AND permission_row.is_delete=0
JOIN yak_security_menu menu_row
  ON menu_row.app_name=role_permission.app_name
 AND (menu_row.menu_code='system' OR menu_row.parent_code='system')
 AND menu_row.is_delete=0
WHERE role_permission.app_name='${appName}'
  AND role_permission.is_delete=0;