package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.extend.PermissionExtend;
import io.yak.framework.security.service.PermissionCache;
import io.yak.framework.security.service.RbacPermissionService;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Default database-backed RBAC permission checker.
 */
@Service
public class RbacPermissionServiceImpl implements RbacPermissionService {

  private static final String ROOT_PERMISSION =
          "security:root";

  private final UserService userService;
  private final UserRoleDao userRoleDao;
  private final RolePermissionService rolePermissionService;
  private final PermissionDao permissionDao;
  private final PermissionExtend permissionExtend;
  private final PermissionCache permissionCache;

  public RbacPermissionServiceImpl(
          UserService userService,
          UserRoleDao userRoleDao,
          RolePermissionService rolePermissionService,
          PermissionDao permissionDao,
          PermissionExtend permissionExtend,
          PermissionCache permissionCache) {
    this.userService = userService;
    this.userRoleDao = userRoleDao;
    this.rolePermissionService = rolePermissionService;
    this.permissionDao = permissionDao;
    this.permissionExtend = permissionExtend;
    this.permissionCache = permissionCache;
  }

  @Override
  public boolean hasPermission(
          String userName,
          String permissionCode) {

    if (!StringUtils.hasText(userName)
            || !StringUtils.hasText(permissionCode)) {
      return false;
    }

    User user = userService.getUserByUsername(userName);
    if (user != null && user.getId() != null) {
      Set<String> permissionCodes = permissionCache.get(
              user.getId(),
              () -> loadPermissionCodes(user.getId()));
      if (permissionCodes.contains(ROOT_PERMISSION)
              || permissionCodes.contains(permissionCode)) {
        return true;
      }
    }

    return permissionExtend.hasPermission(
            userName,
            permissionCode);
  }

  private Set<String> loadPermissionCodes(Long userId) {
    List<Long> roleIds = userRoleDao
            .selectRoleIdListByUserId(userId);
    List<Long> permissionIds = rolePermissionService
            .getPermissionIdListByRoleIdList(roleIds);
    Set<String> permissionCodes = new HashSet<>();

    if (!permissionIds.isEmpty()) {
      Set<Long> grantedIds = new HashSet<>(permissionIds);
      for (Permission permission
              : permissionDao.selectAllAndAscOrderByLevel()) {
        if (permission != null
                && grantedIds.contains(permission.getId())
                && Boolean.TRUE.equals(permission.getActive())
                && StringUtils.hasText(
                permission.getPermissionCode())) {
          permissionCodes.add(
                  permission.getPermissionCode());
        }
      }
    }
    return permissionCodes;
  }
}
