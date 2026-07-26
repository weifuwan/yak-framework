package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.RolePermission;
import java.util.List;

public interface RolePermissionDao {
  public void insertBatch(List<RolePermission> var1);

  public void deleteByRoleId(Integer var1);

  public List<Integer> selectPermissionIdListByRoleId(Integer var1);

  public List<Integer> selectPermissionIdListByRoleIdList(List<Integer> var1);
}
