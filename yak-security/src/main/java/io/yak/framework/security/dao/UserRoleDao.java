package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.UserRole;
import io.yak.framework.security.common.po.UserRolePO;
import java.util.List;

public interface UserRoleDao {
  public List<Long> selectUserIdListByRoleId(Integer var1);

  public List<Long> selectRoleIdListByUserId(Integer var1);

  public void insertBatch(List<UserRole> var1);

  public int deleteByUserIdOrRoleId(Integer var1, Integer var2);

  public int selectCountByRoleId(Integer var1);

  public List<UserRolePO> selectByRoleIds(List<Long> var1);

  public List<UserRolePO> getRoleIdListByUserIds(List<Long> var1);
}
