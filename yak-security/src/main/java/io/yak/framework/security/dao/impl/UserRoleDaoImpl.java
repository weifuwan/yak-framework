package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.common.entity.UserRole;
import io.yak.framework.security.common.po.UserRolePO;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.UserRoleMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserRoleDaoImpl
    extends BaseDaoImpl<UserRolePO> implements UserRoleDao {
  @Autowired private UserRoleMapper userRoleMapper;

  @Override
  public List<Long> selectUserIdListByRoleId(Long roleId) {
    if (roleId == null) {
      return new ArrayList<Long>();
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"user_id"})
        .eq((Object) "role_id", (Object)roleId);
    List userIdList = this.userRoleMapper.selectObjs((Wrapper)queryWrapper);
    if (CollectionUtils.isEmpty((Collection)userIdList)) {
      return new ArrayList<Long>();
    }
    return userIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }

  @Override
  public List<Long> selectRoleIdListByUserId(Long userId) {
    if (userId == null) {
      return new ArrayList<Long>();
    }
    QueryWrapper userRoleWrapper = this.getQueryWrapperWithAppName();
    userRoleWrapper.select(new String[] {"role_id"})
        .eq((Object) "user_id", (Object)userId);
    List roleIdList = this.userRoleMapper.selectObjs((Wrapper)userRoleWrapper);
    return roleIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }

  @Override
  public void insertBatch(List<UserRole> userRoleList) {
    if (!CollectionUtils.isEmpty(userRoleList)) {
      List<UserRolePO> userRolePOList =
          CopyBeanUtil.copyList(userRoleList, UserRolePO.class);
      for (UserRolePO userRolePO : userRolePOList) {
        userRolePO.setAppName(this.yakSecurityProperties.getApplicationName());
        this.userRoleMapper.insert(userRolePO);
      }
    }
  }

  @Override
  public int deleteByUserIdOrRoleId(Long userId, Long roleId) {
    if (userId == null && roleId == null) {
      return 0;
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    ((QueryWrapper)queryWrapper.eq(userId != null, (Object) "user_id",
                                   (Object)userId))
        .eq(roleId != null, (Object) "role_id", (Object)roleId);
    return this.userRoleMapper.delete((Wrapper)queryWrapper);
  }

  @Override
  public int selectCountByRoleId(Long roleId) {
    if (roleId == null) {
      return 0;
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "role_id", (Object)roleId);
    return this.userRoleMapper.selectCount((Wrapper)queryWrapper);
  }

  @Override
  public List<UserRolePO> selectByRoleIds(List<Long> roleIds) {
    if (CollectionUtils.isEmpty(roleIds)) {
      return Collections.emptyList();
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.in((Object) "role_id", roleIds);
    return this.userRoleMapper.selectList((Wrapper)queryWrapper);
  }

  @Override
  public List<UserRolePO> getRoleIdListByUserIds(List<Long> userIds) {
    if (CollectionUtils.isEmpty(userIds)) {
      return Collections.emptyList();
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.in((Object) "user_id", userIds);
    return this.userRoleMapper.selectList((Wrapper)queryWrapper);
  }
}
