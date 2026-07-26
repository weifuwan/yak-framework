-- 仅初始化系统运行所需的权限目录与资源类型；不创建用户或密码。
INSERT INTO yak_security_permission(permission_code,permission_name,parent_id,leaf,level,description,app_name)
VALUES ('security:root','安全管理',0,0,1,'安全模块权限根节点','default'),
       ('security:user:read','用户查看',1,1,2,'查看用户信息','default'),
       ('security:role:read','角色查看',1,1,2,'查看角色信息','default'),
       ('security:permission:read','权限查看',1,1,2,'查看权限信息','default');
INSERT INTO yak_security_resource_type(type_name,app_name)
VALUES ('PROJECT','default'),('SECURITY_RESOURCE','default');
