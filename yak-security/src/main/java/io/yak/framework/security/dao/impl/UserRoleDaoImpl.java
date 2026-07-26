package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.entity.UserRole;
import io.yak.framework.security.common.po.UserRolePO;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.dao.mapper.UserRoleMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserRoleDaoImpl extends BaseDaoImpl<UserRolePO> implements UserRoleDao {
  private final UserRoleMapper userRoleMapper;

  public UserRoleDaoImpl(UserRoleMapper userRoleMapper) { this.userRoleMapper = userRoleMapper; }

  @Override
  public List<Long> selectUserIdListByRoleId(Long roleId) {
    if (roleId == null) return Collections.emptyList();
    return ids(UserRolePO::getUserId, UserRolePO::getRoleId, roleId);
  }

  @Override
  public List<Long> selectRoleIdListByUserId(Long userId) {
    if (userId == null) return Collections.emptyList();
    return ids(UserRolePO::getRoleId, UserRolePO::getUserId, userId);
  }

  private List<Long> ids(com.baomidou.mybatisplus.core.toolkit.support.SFunction<UserRolePO, ?> selected,
                         com.baomidou.mybatisplus.core.toolkit.support.SFunction<UserRolePO, ?> condition,
                         Long value) {
    return userRoleMapper.selectObjs(Wrappers.<UserRolePO>lambdaQuery().select(selected).eq(condition, value))
        .stream().map(id -> ((Number) id).longValue()).collect(Collectors.toList());
  }

  @Override
  public void insertBatch(List<UserRole> userRoleList) {
    if (CollectionUtils.isEmpty(userRoleList)) return;
    CopyBeanUtil.copyList(userRoleList, UserRolePO.class).forEach(userRoleMapper::insert);
  }

  @Override
  public int deleteByUserIdOrRoleId(Long userId, Long roleId) {
    if (userId == null && roleId == null) return 0;
    return userRoleMapper.delete(Wrappers.<UserRolePO>lambdaQuery()
        .eq(userId != null, UserRolePO::getUserId, userId)
        .eq(roleId != null, UserRolePO::getRoleId, roleId));
  }

  @Override
  public int selectCountByRoleId(Long roleId) {
    return roleId == null ? 0 : userRoleMapper.selectCount(Wrappers.<UserRolePO>lambdaQuery()
        .eq(UserRolePO::getRoleId, roleId));
  }

  @Override
  public List<UserRolePO> selectByRoleIds(List<Long> roleIds) {
    if (CollectionUtils.isEmpty(roleIds)) return Collections.emptyList();
    return userRoleMapper.selectList(Wrappers.<UserRolePO>lambdaQuery().in(UserRolePO::getRoleId, roleIds));
  }

  @Override
  public List<UserRolePO> getRoleIdListByUserIds(List<Long> userIds) {
    if (CollectionUtils.isEmpty(userIds)) return Collections.emptyList();
    return userRoleMapper.selectList(Wrappers.<UserRolePO>lambdaQuery().in(UserRolePO::getUserId, userIds));
  }
}
