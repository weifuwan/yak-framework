-- 仅初始化系统运行所需的权限目录与资源类型；不创建用户或密码。
INSERT INTO yak_security_permission(permission_code,permission_name,parent_id,leaf,level,description,app_name)
VALUES ('security:root','安全管理',0,0,1,'安全模块权限根节点','${appName}');

INSERT INTO yak_security_permission(permission_code,permission_name,parent_id,leaf,level,description,app_name)
SELECT child.permission_code,
       child.permission_name,
       parent.id,
       1,
       2,
       child.description,
       parent.app_name
FROM yak_security_permission parent
JOIN (
    SELECT 'security:user:read' AS permission_code, '用户查看' AS permission_name, '查看用户信息' AS description
    UNION ALL
    SELECT 'security:role:read', '角色查看', '查看角色信息'
    UNION ALL
    SELECT 'security:permission:read', '权限查看', '查看权限信息'
) child
WHERE parent.permission_code = 'security:root'
  AND parent.app_name = '${appName}'
  AND parent.is_delete = 0;

INSERT INTO yak_security_resource_type(type_name,app_name)
VALUES ('PROJECT','${appName}'),('SECURITY_RESOURCE','${appName}');
