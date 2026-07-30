-- Add first-class business tenants without changing the existing app_name isolation layer.

CREATE TABLE yak_security_tenant (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  tenant_code VARCHAR(64) NOT NULL COMMENT '稳定租户编码',
  tenant_name VARCHAR(128) NOT NULL COMMENT '租户名称',
  external_system VARCHAR(64) NULL COMMENT '外部目录系统标识',
  external_tenant_id VARCHAR(128) NULL COMMENT '外部目录中的租户标识',
  status INT NOT NULL DEFAULT 1 COMMENT '租户状态：1 启用，2 禁用',
  description VARCHAR(512) NOT NULL DEFAULT '' COMMENT '租户说明',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_delete TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  app_name VARCHAR(64) NOT NULL COMMENT '应用级数据隔离键',
  active_tenant_code VARCHAR(64)
    GENERATED ALWAYS AS (IF(is_delete=0,tenant_code,NULL)) STORED
    COMMENT '未删除租户编码唯一键',
  active_external_tenant_id VARCHAR(128)
    GENERATED ALWAYS AS (IF(is_delete=0,external_tenant_id,NULL)) STORED
    COMMENT '未删除外部租户标识唯一键',
  PRIMARY KEY(id),
  UNIQUE KEY uk_tenant_app_code(app_name,active_tenant_code),
  UNIQUE KEY uk_tenant_app_external(
    app_name,external_system,active_external_tenant_id),
  KEY idx_tenant_app_status(app_name,status,is_delete)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表';

CREATE TABLE yak_security_user_tenant (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id BIGINT NOT NULL COMMENT '用户主键',
  tenant_id BIGINT NOT NULL COMMENT '租户主键',
  member_type TINYINT NOT NULL DEFAULT 0 COMMENT '成员类型：0 普通成员，1 租户管理员',
  default_tenant TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为用户默认租户',
  status INT NOT NULL DEFAULT 1 COMMENT '成员状态：1 启用，2 禁用',
  external_membership_id VARCHAR(128) NULL COMMENT '外部目录中的成员关系标识',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_delete TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  app_name VARCHAR(64) NOT NULL COMMENT '应用级数据隔离键',
  active_relation TINYINT
    GENERATED ALWAYS AS (IF(is_delete=0,0,NULL)) STORED
    COMMENT '未删除关系唯一键',
  active_default_user_id BIGINT
    GENERATED ALWAYS AS (
      IF(is_delete=0 AND default_tenant=1,user_id,NULL)
    ) STORED
    COMMENT '每个用户只能有一个默认租户',
  PRIMARY KEY(id),
  UNIQUE KEY uk_user_tenant_active(
    app_name,user_id,tenant_id,active_relation),
  UNIQUE KEY uk_user_tenant_default(
    app_name,active_default_user_id),
  KEY idx_user_tenant_tenant(
    app_name,tenant_id,is_delete),
  KEY idx_user_tenant_user(
    app_name,user_id,status,is_delete)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户租户成员关系表';

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
    SELECT 'security:tenant:read' permission_code,
           '查看租户管理' permission_name,
           '查看租户、外部目录映射和成员关系' description
    UNION ALL SELECT 'security:tenant:create','新增租户','创建业务租户'
    UNION ALL SELECT 'security:tenant:update','编辑租户','修改租户资料和状态'
    UNION ALL SELECT 'security:tenant:assign','分配租户成员','维护租户成员和管理员'
    UNION ALL SELECT 'security:tenant:sync','同步外部租户','从企业目录幂等同步租户'
    UNION ALL SELECT 'security:tenant:delete','删除租户','删除没有成员关系的租户'
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

INSERT INTO yak_security_menu
(menu_code,menu_name,parent_code,route_path,icon_key,menu_type,sort_order,visible,active,required_permission_code,description,app_name)
VALUES
('system-tenants','租户管理','system','/system/tenants','system',2,45,1,1,
 'security:tenant:read','租户、成员及外部目录映射管理','${appName}')
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

-- 历史 root 角色自动获得租户管理权限。
INSERT IGNORE INTO yak_security_role_permission(
  role_id,permission_id,app_name)
SELECT root_relation.role_id,
       tenant_permission.id,
       root_relation.app_name
FROM yak_security_role_permission root_relation
JOIN yak_security_permission root_permission
  ON root_permission.id=root_relation.permission_id
 AND root_permission.app_name=root_relation.app_name
 AND root_permission.permission_code='security:root'
 AND root_permission.is_delete=0
JOIN yak_security_permission tenant_permission
  ON tenant_permission.app_name=root_relation.app_name
 AND tenant_permission.permission_code LIKE 'security:tenant:%'
 AND tenant_permission.is_delete=0
WHERE root_relation.app_name='${appName}'
  AND root_relation.is_delete=0;

INSERT IGNORE INTO yak_security_role_menu(
  role_id,menu_id,app_name)
SELECT root_relation.role_id,
       tenant_menu.id,
       root_relation.app_name
FROM yak_security_role_permission root_relation
JOIN yak_security_permission root_permission
  ON root_permission.id=root_relation.permission_id
 AND root_permission.app_name=root_relation.app_name
 AND root_permission.permission_code='security:root'
 AND root_permission.is_delete=0
JOIN yak_security_menu tenant_menu
  ON tenant_menu.app_name=root_relation.app_name
 AND tenant_menu.menu_code='system-tenants'
 AND tenant_menu.is_delete=0
WHERE root_relation.app_name='${appName}'
  AND root_relation.is_delete=0;
