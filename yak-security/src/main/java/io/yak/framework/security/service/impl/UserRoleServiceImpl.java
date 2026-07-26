package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.UserRole;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.service.UserRoleService;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value = "yakSecurityUserRoleServiceImpl")
public class UserRoleServiceImpl implements UserRoleService {
  @Autowired private UserRoleDao userRoleDao;

  @Override
  public List<Integer> getUserIdListByRoleId(Integer roleId) {
    if (roleId == null) {
      return new ArrayList<Integer>();
    }
    return this.userRoleDao.selectUserIdListByRoleId(roleId);
  }

  @Override
  public List<Integer> getRoleIdListByUserId(Integer userId) {
    if (userId == null) {
      return new ArrayList<Integer>();
    }
    return this.userRoleDao.selectRoleIdListByUserId(userId);
  }

  @Override
  public void updateUserRoleByUserId(Integer userId, List<Integer> roleIdList) {
    if (userId == null || CollectionUtils.isEmpty(roleIdList)) {
      return;
    }
    this.userRoleDao.deleteByUserIdOrRoleId(userId, null);
    this.userRoleDao.insertBatch(
        this.getUserRoleList(true, userId, roleIdList));
  }

  @Override
  public void updateUserRoleByRoleId(Integer roleId, List<Integer> userIdList) {
    if (roleId == null) {
      return;
    }
    this.userRoleDao.deleteByUserIdOrRoleId(null, roleId);
    if (CollectionUtils.isEmpty(userIdList)) {
      return;
    }
    this.userRoleDao.insertBatch(
        this.getUserRoleList(false, roleId, userIdList));
  }

  @Override
  public int getUserRoleCountByRoleId(Integer roleId) {
    if (roleId == 0) {
      return 0;
    }
    return this.userRoleDao.selectCountByRoleId(roleId);
  }

  @Override
  public int deleteByUserIdOrRoleId(Integer userId, Integer roleId) {
    return this.userRoleDao.deleteByUserIdOrRoleId(userId, roleId);
  }

  @Override
  public List<UserRole> getByRoleIds(List<Integer> roleIds) {
    return CopyBeanUtil.copyList(this.userRoleDao.selectByRoleIds(roleIds),
                                 UserRole.class);
  }

  @Override
  public List<UserRole> getRoleIdListByUserIds(List<Integer> userId) {
    return CopyBeanUtil.copyList(
        this.userRoleDao.getRoleIdListByUserIds(userId), UserRole.class);
  }

  private List<UserRole> getUserRoleList(boolean isUserId, Integer id,
                                         List<Integer> idList) {
    ArrayList<UserRole> result = new ArrayList<UserRole>();
    for (Integer id2 : idList) {
      result.add(isUserId ? new UserRole(id, id2) : new UserRole(id2, id));
    }
    return result;
  }
}
