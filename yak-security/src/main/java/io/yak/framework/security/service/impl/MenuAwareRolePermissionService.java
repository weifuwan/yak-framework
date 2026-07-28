package io.yak.framework.security.service.impl;

import io.yak.framework.security.service.RolePermissionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 将角色保存请求中的普通权限和菜单授权拆分到各自关系表。
 *
 * <p>菜单绑定的读取权限由菜单授权推导，避免菜单可见性与页面读取权限
 * 出现两套互相矛盾的配置来源。
 */
@Primary
@Service("yakSecurityMenuAwareRolePermissionService")
public class MenuAwareRolePermissionService
        implements RolePermissionService {

  private final RolePermissionService delegate;
  private final MenuAuthorizationService menuAuthorizationService;

  public MenuAwareRolePermissionService(
          @Qualifier("yakSecurityRolePermissionServiceImpl")
                  RolePermissionService delegate,
          MenuAuthorizationService menuAuthorizationService) {

    this.delegate = delegate;
    this.menuAuthorizationService = menuAuthorizationService;
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void saveRolePermission(
          Long roleId,
          List<Long> permissionIdList) {

    delegate.saveRolePermission(
            roleId,
            normalPermissions(permissionIdList));
    menuAuthorizationService.saveRoleMenus(
            roleId,
            MenuSelectionCodec.extractMenuIds(permissionIdList));
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void updateRolePermission(
          Long roleId,
          List<Long> permissionIdList) {

    delegate.updateRolePermission(
            roleId,
            normalPermissions(permissionIdList));
    menuAuthorizationService.updateRoleMenus(
            roleId,
            MenuSelectionCodec.extractMenuIds(permissionIdList));
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void deleteRolePermissionByRoleId(Long roleId) {
    delegate.deleteRolePermissionByRoleId(roleId);
    menuAuthorizationService.deleteRoleMenus(roleId);
  }

  @Override
  public void deleteRolePermissionByPermissionId(
          Long permissionId) {
    delegate.deleteRolePermissionByPermissionId(permissionId);
  }

  @Override
  public List<Long> getPermissionIdListByRoleId(Long roleId) {
    List<Long> result = new ArrayList<>(
            delegate.getPermissionIdListByRoleId(roleId));
    reconcileMenuManagedPermissions(
            result,
            menuAuthorizationService
                    .getRequiredPermissionIdsByRoleId(roleId));
    return result;
  }

  @Override
  public List<Long> getPermissionIdListByRoleIdList(
          List<Long> roleIdList) {

    List<Long> result = new ArrayList<>(
            delegate.getPermissionIdListByRoleIdList(roleIdList));
    reconcileMenuManagedPermissions(
            result,
            menuAuthorizationService
                    .getRequiredPermissionIdsByRoleIds(roleIdList));
    return result;
  }

  private List<Long> normalPermissions(
          Collection<Long> submittedIds) {

    List<Long> result = MenuSelectionCodec.extractPermissionIds(
            submittedIds);
    Set<Long> menuManagedPermissionIds =
            menuAuthorizationService.getMenuBoundPermissionIds();
    result.removeIf(menuManagedPermissionIds::contains);
    return result;
  }

  private void reconcileMenuManagedPermissions(
          List<Long> permissionIds,
          Collection<Long> impliedPermissionIds) {

    Set<Long> menuManagedPermissionIds =
            menuAuthorizationService.getMenuBoundPermissionIds();
    permissionIds.removeIf(menuManagedPermissionIds::contains);

    Set<Long> unique = new LinkedHashSet<>(permissionIds);
    if (impliedPermissionIds != null) {
      unique.addAll(impliedPermissionIds);
    }

    permissionIds.clear();
    permissionIds.addAll(unique);
  }
}
