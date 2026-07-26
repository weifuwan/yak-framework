package io.yak.framework.security.service;

import io.yak.framework.security.common.entity.UserRole;
import java.util.List;

public interface UserRoleService {
  public List<Long> getUserIdListByRoleId(Integer var1);

  public List<Long> getRoleIdListByUserId(Integer var1);

  public void updateUserRoleByUserId(Integer var1, List<Long> var2);

  public void updateUserRoleByRoleId(Integer var1, List<Long> var2);

  public int getUserRoleCountByRoleId(Integer var1);

  public int deleteByUserIdOrRoleId(Integer var1, Integer var2);

  public List<UserRole> getByRoleIds(List<Long> var1);

  public List<UserRole> getRoleIdListByUserIds(List<Long> var1);
}
