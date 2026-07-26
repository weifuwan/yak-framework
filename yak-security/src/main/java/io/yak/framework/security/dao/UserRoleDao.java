package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.UserRole;
import io.yak.framework.security.common.po.UserRolePO;
import java.util.List;

public interface UserRoleDao {
  public List<Integer> selectUserIdListByRoleId(Integer var1);

  public List<Integer> selectRoleIdListByUserId(Integer var1);

  public void insertBatch(List<UserRole> var1);

  public int deleteByUserIdOrRoleId(Integer var1, Integer var2);

  public int selectCountByRoleId(Integer var1);

  public List<UserRolePO> selectByRoleIds(List<Integer> var1);

  public List<UserRolePO> getRoleIdListByUserIds(List<Integer> var1);
}
