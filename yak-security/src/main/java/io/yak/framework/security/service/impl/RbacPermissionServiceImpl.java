package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.dao.RolePermissionDao;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.extend.PermissionExtend;
import io.yak.framework.security.service.RbacPermissionService;
import io.yak.framework.security.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default database-backed RBAC permission checker.
 */
@Service
public class RbacPermissionServiceImpl implements RbacPermissionService {

  private final UserService userService;
  private final UserRoleDao userRoleDao;
  private final RolePermissionDao rolePermissionDao;
  private final PermissionDao permissionDao;
  private final PermissionExtend permissionExtend;

  public RbacPermissionServiceImpl(
          UserService userService,
          UserRoleDao userRoleDao,
          RolePermissionDao rolePermissionDao,
          PermissionDao permissionDao,
          PermissionExtend permissionExtend) {
    this.userService = userService;
    this.userRoleDao = userRoleDao;
    this.rolePermissionDao = rolePermissionDao;
    this.permissionDao = permissionDao;
    this.permissionExtend = permissionExtend;
  }

  @Override
  public boolean hasPermission(String userName, String permissionCode) {
    if (!StringUtils.hasText(userName)
            || !StringUtils.hasText(permissionCode)) {
      return false;
    }

    User user = userService.getUserByUsername(userName);
    if (user != null && user.getId() != null) {
      List<Long> roleIds = userRoleDao.selectRoleIdListByUserId(user.getId());
      List<Long> permissionIds =
              rolePermissionDao.selectPermissionIdListByRoleIdList(roleIds);
      if (!permissionIds.isEmpty()) {
        Set<Long> grantedIds = new HashSet<>(permissionIds);
        for (Permission permission : permissionDao.selectAllAndAscOrderByLevel()) {
          if (permission != null
                  && grantedIds.contains(permission.getId())
                  && permissionCode.equals(permission.getPermissionCode())) {
            return true;
          }
        }
      }
    }

    return permissionExtend.hasPermission(userName, permissionCode);
  }
}
