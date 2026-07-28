-- Yak Security 通用菜单定义与角色菜单授权表。
-- 业务系统应在自己的 classpath:yak-security/db/migration 目录中初始化菜单、权限及兼容数据。
CREATE TABLE yak_security_menu (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  menu_code VARCHAR(128) NOT NULL COMMENT '稳定菜单编码，与宿主应用路由标识对应',
  menu_name VARCHAR(128) NOT NULL COMMENT '菜单名称',
  parent_code VARCHAR(128) NULL COMMENT '父菜单编码，NULL 表示根节点',
  route_path VARCHAR(255) NULL COMMENT '前端路由路径',
  icon_key VARCHAR(64) NULL COMMENT '前端图标键',
  menu_type TINYINT NOT NULL DEFAULT 2 COMMENT '菜单类型：1 目录，2 页面',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
  visible TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否显示：0 否，1 是',
  active TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0 否，1 是',
  required_permission_code VARCHAR(128) NULL COMMENT '访问菜单隐含授予的读取权限编码',
  description VARCHAR(255) NULL COMMENT '菜单说明',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_delete TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  app_name VARCHAR(64) NOT NULL COMMENT '应用级数据隔离键',
  active_menu_code VARCHAR(128) GENERATED ALWAYS AS (IF(is_delete=0,menu_code,NULL)) STORED COMMENT '未删除菜单唯一键',
  PRIMARY KEY(id),
  UNIQUE KEY uk_menu_app_code(app_name,active_menu_code),
  KEY idx_menu_app_parent(app_name,parent_code,is_delete),
  KEY idx_menu_app_permission(app_name,required_permission_code,is_delete),
  KEY idx_menu_app_status(app_name,active,visible,is_delete)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单定义表';

CREATE TABLE yak_security_role_menu (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  role_id BIGINT NOT NULL COMMENT '角色主键',
  menu_id BIGINT NOT NULL COMMENT '菜单主键',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_delete TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  app_name VARCHAR(64) NOT NULL COMMENT '应用级数据隔离键',
  active_relation TINYINT GENERATED ALWAYS AS (IF(is_delete=0,0,NULL)) STORED COMMENT '未删除关系唯一键',
  PRIMARY KEY(id),
  UNIQUE KEY uk_role_menu_active(app_name,role_id,menu_id,active_relation),
  KEY idx_role_menu_role(app_name,role_id,is_delete),
  KEY idx_role_menu_menu(app_name,menu_id,is_delete)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关系表';
