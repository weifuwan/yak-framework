package io.yak.framework.security.service;

import java.util.List;

public interface RolePermissionService {
  public void saveRolePermission(Long var1, List<Long> var2);

  public void updateRolePermission(Long var1, List<Long> var2);

  public void deleteRolePermissionByRoleId(Long var1);

  public List<Long> getPermissionIdListByRoleId(Long var1);

  public List<Long> getPermissionIdListByRoleIdList(List<Long> var1);
}
