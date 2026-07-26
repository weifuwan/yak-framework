package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.common.entity.RolePermission;
import io.yak.framework.security.common.po.RolePermissionPO;
import io.yak.framework.security.dao.RolePermissionDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.RolePermissionMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RolePermissionDaoImpl
    extends BaseDaoImpl<RolePermissionPO> implements RolePermissionDao {
  @Autowired private RolePermissionMapper rolePermissionMapper;

  @Override
  public void insertBatch(List<RolePermission> rolePermissionList) {
    if (CollectionUtils.isEmpty(rolePermissionList)) {
      return;
    }
    List<RolePermissionPO> rolePermissionPOList =
        CopyBeanUtil.copyList(rolePermissionList, RolePermissionPO.class);
    for (RolePermissionPO rolePermissionPO : rolePermissionPOList) {
      rolePermissionPO.setAppName(this.yakSecurityProperties.getAppName());
      this.rolePermissionMapper.insert(rolePermissionPO);
    }
  }

  @Override
  public void deleteByRoleId(Integer roleId) {
    if (roleId == null) {
      return;
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "role_id", (Object)roleId);
    this.rolePermissionMapper.delete((Wrapper)queryWrapper);
  }

  @Override
  public List<Integer> selectPermissionIdListByRoleId(Integer roleId) {
    if (roleId == null) {
      return new ArrayList<Integer>();
    }
    ArrayList<Integer> roleIdList = new ArrayList<Integer>();
    roleIdList.add(roleId);
    return this.selectPermissionIdListByRoleIdList(roleIdList);
  }

  @Override
  public List<Integer>
  selectPermissionIdListByRoleIdList(List<Integer> roleIdList) {
    if (CollectionUtils.isEmpty(roleIdList)) {
      return new ArrayList<Integer>();
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"permission_id"})
        .in((Object) "role_id", roleIdList);
    List permissionIdList =
        this.rolePermissionMapper.selectObjs((Wrapper)queryWrapper);
    return permissionIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }
}
