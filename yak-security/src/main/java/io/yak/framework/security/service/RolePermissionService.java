package io.yak.framework.security.service;

import java.util.List;

/**
 * 角色权限关系服务接口。
 */
public interface RolePermissionService {
  /**
   * 保存角色权限关系。
   */
  void saveRolePermission(Long var1, List<Long> var2);

  /**
   * 更新角色权限关系。
   */
  void updateRolePermission(Long var1, List<Long> var2);

  /**
   * 根据角色 ID 删除角色权限关系。
   */
  void deleteRolePermissionByRoleId(Long var1);

  /**
   * 根据角色 ID 查询权限 ID 集合。
   */
  List<Long> getPermissionIdListByRoleId(Long var1);

  /**
   * 根据角色 ID 集合查询权限 ID 集合。
   */
  List<Long> getPermissionIdListByRoleIdList(List<Long> var1);
}
