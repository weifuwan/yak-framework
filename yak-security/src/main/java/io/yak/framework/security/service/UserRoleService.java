package io.yak.framework.security.service;

import io.yak.framework.security.common.entity.UserRole;

import java.util.List;

/**
 * 用户角色关系服务接口。
 *
 * @author weifuwan
 */
public interface UserRoleService {

  /**
   * 根据角色 ID 查询用户 ID 集合。
   */
  List<Long> getUserIdListByRoleId(Long roleId);

  /**
   * 根据用户 ID 查询角色 ID 集合。
   */
  List<Long> getRoleIdListByUserId(Long userId);

  /**
   * 根据用户 ID 更新用户角色关系。
   */
  void updateUserRoleByUserId(
          Long userId,
          List<Long> roleIdList);

  /**
   * 根据角色 ID 更新用户角色关系。
   */
  void updateUserRoleByRoleId(
          Long roleId,
          List<Long> userIdList);

  /**
   * 根据角色 ID 查询用户角色关系数量。
   */
  int getUserRoleCountByRoleId(Long roleId);

  /**
   * 根据用户 ID 或角色 ID 删除用户角色关系。
   */
  int deleteByUserIdOrRoleId(
          Long userId,
          Long roleId);

  /**
   * 根据角色 ID 集合查询用户角色关系。
   */
  List<UserRole> getByRoleIds(List<Long> roleIdList);

  /**
   * 根据用户 ID 集合查询用户角色关系。
   */
  List<UserRole> getRoleIdListByUserIds(List<Long> userIdList);
}
