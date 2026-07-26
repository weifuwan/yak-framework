package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.RolePermission;
import io.yak.framework.security.dao.RolePermissionDao;
import io.yak.framework.security.service.RolePermissionService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value = "yakSecurityRolePermissionServiceImpl")
public class RolePermissionServiceImpl implements RolePermissionService {
  @Autowired private RolePermissionDao rolePermissionDao;

  @Override
  public void saveRolePermission(Long roleId,
                                 List<Long> permissionIdList) {
    if (roleId == null || CollectionUtils.isEmpty(permissionIdList)) {
      return;
    }
    this.rolePermissionDao.insertBatch(
        this.getRolePermissionList(roleId, permissionIdList));
  }

  @Override
  public void updateRolePermission(Long roleId,
                                   List<Long> permissionIdList) {
    this.deleteRolePermissionByRoleId(roleId);
    this.saveRolePermission(roleId, permissionIdList);
  }

  @Override
  public void deleteRolePermissionByRoleId(Long roleId) {
    if (roleId == null) {
      return;
    }
    this.rolePermissionDao.deleteByRoleId(roleId);
  }

  @Override
  public List<Long> getPermissionIdListByRoleId(Long roleId) {
    if (roleId == null) {
      return new ArrayList<Long>();
    }
    return this.rolePermissionDao.selectPermissionIdListByRoleId(roleId);
  }

  @Override
  public List<Long>
  getPermissionIdListByRoleIdList(List<Long> roleIdList) {
    if (CollectionUtils.isEmpty(roleIdList)) {
      return new ArrayList<Long>();
    }
    return this.rolePermissionDao.selectPermissionIdListByRoleIdList(
        roleIdList);
  }

  private List<RolePermission>
  getRolePermissionList(Long roleId, List<Long> permissionIdList) {
    ArrayList<RolePermission> rolePermissionList =
        new ArrayList<RolePermission>();
    for (Long permissionId : permissionIdList) {
      RolePermission rolePermission = new RolePermission();
      rolePermission.setRoleId(roleId);
      rolePermission.setPermissionId(permissionId);
      rolePermissionList.add(rolePermission);
    }
    return rolePermissionList;
  }
}
