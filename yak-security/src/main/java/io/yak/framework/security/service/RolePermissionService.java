package io.yak.framework.security.service;

import java.util.List;

/**
 * 角色权限关系服务接口。
 */
public interface RolePermissionService {
  void saveRolePermission(Long var1, List<Long> var2);

  void updateRolePermission(Long var1, List<Long> var2);

  void deleteRolePermissionByRoleId(Long var1);

  List<Long> getPermissionIdListByRoleId(Long var1);

  List<Long> getPermissionIdListByRoleIdList(List<Long> var1);
}
