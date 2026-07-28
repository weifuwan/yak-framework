-- 菜单定义与角色菜单授权。前端组件仍由代码注册，数据库负责菜单元数据和角色可见范围。
CREATE TABLE yak_security_menu (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  menu_code VARCHAR(128) NOT NULL COMMENT '稳定菜单编码，与前端路由 id 对应',
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

INSERT INTO yak_security_menu
(menu_code,menu_name,parent_code,route_path,icon_key,menu_type,sort_order,visible,active,required_permission_code,description,app_name)
VALUES
('integration','数据集成',NULL,NULL,'sync',1,10,1,1,NULL,'数据同步任务入口','${appName}'),
('batch-link-up','离线同步','integration','/sync/batch-link-up','sync',2,10,1,1,'task:batch:read','离线数据同步管理','${appName}'),
('realtime-link-up','实时同步','integration','/sync/realtime-link-up','realtime',2,20,1,1,'task:realtime:read','实时数据同步管理','${appName}'),
('workflow','流程编排',NULL,NULL,'workflow',1,20,1,1,NULL,'工作流编排入口','${appName}'),
('workflow-project','工作流项目','workflow','/workflow-project','project',2,10,1,1,'workflow:project:read','工作流项目管理','${appName}'),
('workflow-management','工作流管理','workflow','/workflow-management','workflow',2,20,1,1,'workflow:definition:read','工作流定义管理','${appName}'),
('workflow-instance','工作流实例','workflow','/workflow-instance','instance',2,30,1,1,'workflow:instance:read','工作流实例管理','${appName}'),
('resources','资源管理',NULL,NULL,'database',1,30,1,1,NULL,'数据资源入口','${appName}'),
('data-source','数据源管理','resources','/data-source','database',2,10,1,1,'resource:data-source:read','数据源管理','${appName}'),
('client','客户端管理','resources','/client','client',2,20,1,1,'resource:client:read','客户端管理','${appName}'),
('connector','连接器管理','resources','/connector','connector',2,30,1,1,'resource:connector:read','连接器管理','${appName}'),
('quality','数据质量',NULL,NULL,'quality',1,40,1,1,NULL,'数据质量入口','${appName}'),
('data-quality','质量规则','quality','/data-quality','quality',2,10,1,1,'quality:rule:read','质量规则管理','${appName}'),
('data-quality-report','质量报告','quality','/data-quality/report','report',2,20,1,1,'quality:report:read','质量报告查询','${appName}'),
('operations','运维中心',NULL,NULL,'monitor',1,50,1,1,NULL,'运行运维入口','${appName}'),
('metrics','运行监控','operations','/metrics','monitor',2,10,1,1,'operations:metrics:read','运行指标监控','${appName}'),
('alarm','告警管理','operations','/alarm','alarm',2,20,1,1,'operations:alarm:read','告警管理','${appName}'),
('system','系统管理',NULL,NULL,'system',1,60,1,1,NULL,'安全与系统管理入口','${appName}'),
('system-users','用户管理','system','/system/users','system',2,10,1,1,'security:user:read','用户管理','${appName}'),
('system-roles','角色管理','system','/system/roles','system',2,20,1,1,'security:role:read','角色及授权管理','${appName}'),
('system-permissions','权限管理','system','/system/permissions','system',2,30,1,1,'security:permission:read','操作权限管理','${appName}'),
('system-departments','部门管理','system','/system/departments','system',2,40,1,1,'security:department:read','部门管理','${appName}'),
('system-security-projects','Security 授权项目','system','/system/projects','system',2,50,1,1,'security:project:read','安全项目管理','${appName}'),
('system-resource-permissions','资源授权','system','/system/resource-permissions','system',2,60,1,1,'security:resource-permission:read','资源级授权管理','${appName}'),
('system-configs','系统配置','system','/system/configs','system',2,70,1,1,'security:config:read','系统配置管理','${appName}'),
('system-operation-logs','操作日志','system','/system/oplogs','system',2,80,1,1,'security:operation-log:read','操作日志查询','${appName}');

-- 兼容已有角色：按已有读取权限回填角色菜单关系，升级后不会突然丢失菜单。
INSERT INTO yak_security_role_menu(role_id,menu_id,app_name)
SELECT DISTINCT rp.role_id,m.id,rp.app_name
FROM yak_security_role_permission rp
JOIN yak_security_permission p
  ON p.id=rp.permission_id
 AND p.app_name=rp.app_name
 AND p.is_delete=0
JOIN yak_security_menu m
  ON m.required_permission_code=p.permission_code
 AND m.app_name=rp.app_name
 AND m.is_delete=0
WHERE rp.app_name='${appName}'
  AND rp.is_delete=0;
