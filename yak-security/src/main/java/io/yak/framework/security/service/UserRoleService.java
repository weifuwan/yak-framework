package io.yak.framework.security.service;

import io.yak.framework.security.common.entity.UserRole;
import java.util.List;

/**
 * 用户角色关系服务接口。
 */
public interface UserRoleService {
  List<Long> getUserIdListByRoleId(Long var1);

  List<Long> getRoleIdListByUserId(Long var1);

  void updateUserRoleByUserId(Long var1, List<Long> var2);

  void updateUserRoleByRoleId(Long var1, List<Long> var2);

  int getUserRoleCountByRoleId(Long var1);

  int deleteByUserIdOrRoleId(Long var1, Long var2);

  List<UserRole> getByRoleIds(List<Long> var1);

  List<UserRole> getRoleIdListByUserIds(List<Long> var1);
}
