package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.UserRole;
import io.yak.framework.security.common.po.UserRolePO;
import java.util.List;

public interface UserRoleDao {
  public List<Long> selectUserIdListByRoleId(Long roleId);

  public List<Long> selectRoleIdListByUserId(Long userId);

  public void insertBatch(List<UserRole> var1);

  public int deleteByUserIdOrRoleId(Long userId, Long roleId);

  public int selectCountByRoleId(Long roleId);

  public List<UserRolePO> selectByRoleIds(List<Long> var1);

  public List<UserRolePO> getRoleIdListByUserIds(List<Long> var1);
}
