package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.entity.RolePermission;
import io.yak.framework.security.common.po.RolePermissionPO;
import io.yak.framework.security.dao.RolePermissionDao;
import io.yak.framework.security.dao.mapper.RolePermissionMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RolePermissionDaoImpl extends BaseDaoImpl<RolePermissionPO> implements RolePermissionDao {
  private final RolePermissionMapper rolePermissionMapper;

  public RolePermissionDaoImpl(RolePermissionMapper rolePermissionMapper) {
    this.rolePermissionMapper = rolePermissionMapper;
  }

  @Override
  public void insertBatch(List<RolePermission> items) {
    if (CollectionUtils.isEmpty(items)) return;
    CopyBeanUtil.copyList(items, RolePermissionPO.class).forEach(rolePermissionMapper::insert);
  }

  @Override
  public void deleteByRoleId(Long roleId) {
    if (roleId != null) rolePermissionMapper.delete(Wrappers.<RolePermissionPO>lambdaQuery()
        .eq(RolePermissionPO::getRoleId, roleId));
  }

  @Override
  public List<Long> selectPermissionIdListByRoleId(Long roleId) {
    return roleId == null ? Collections.emptyList()
        : selectPermissionIdListByRoleIdList(Collections.singletonList(roleId));
  }

  @Override
  public List<Long> selectPermissionIdListByRoleIdList(List<Long> roleIds) {
    if (CollectionUtils.isEmpty(roleIds)) return Collections.emptyList();
    return rolePermissionMapper.selectObjs(Wrappers.<RolePermissionPO>lambdaQuery()
        .select(RolePermissionPO::getPermissionId).in(RolePermissionPO::getRoleId, roleIds))
        .stream().map(id -> ((Number) id).longValue()).collect(Collectors.toList());
  }
}
