package io.yak.framework.security.service;

import io.yak.framework.security.common.entity.UserRole;
import java.util.List;

public interface UserRoleService {
  public List<Integer> getUserIdListByRoleId(Integer var1);

  public List<Integer> getRoleIdListByUserId(Integer var1);

  public void updateUserRoleByUserId(Integer var1, List<Integer> var2);

  public void updateUserRoleByRoleId(Integer var1, List<Integer> var2);

  public int getUserRoleCountByRoleId(Integer var1);

  public int deleteByUserIdOrRoleId(Integer var1, Integer var2);

  public List<UserRole> getByRoleIds(List<Integer> var1);

  public List<UserRole> getRoleIdListByUserIds(List<Integer> var1);
}
