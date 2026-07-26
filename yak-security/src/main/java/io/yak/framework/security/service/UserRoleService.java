package io.yak.framework.security.service;

import io.yak.framework.security.common.entity.UserRole;
import java.util.List;

public interface UserRoleService {
  public List<Long> getUserIdListByRoleId(Long var1);

  public List<Long> getRoleIdListByUserId(Long var1);

  public void updateUserRoleByUserId(Long var1, List<Long> var2);

  public void updateUserRoleByRoleId(Long var1, List<Long> var2);

  public int getUserRoleCountByRoleId(Long var1);

  public int deleteByUserIdOrRoleId(Long var1, Long var2);

  public List<UserRole> getByRoleIds(List<Long> var1);

  public List<UserRole> getRoleIdListByUserIds(List<Long> var1);
}
