package io.yak.framework.security.service;

import java.util.List;

public interface RolePermissionService {
  public void saveRolePermission(Integer var1, List<Long> var2);

  public void updateRolePermission(Integer var1, List<Long> var2);

  public void deleteRolePermissionByRoleId(Integer var1);

  public List<Long> getPermissionIdListByRoleId(Integer var1);

  public List<Long> getPermissionIdListByRoleIdList(List<Long> var1);
}
