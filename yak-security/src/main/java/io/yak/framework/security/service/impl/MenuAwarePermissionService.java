package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.dto.permission.PermissionDTO;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import io.yak.framework.security.service.PermissionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 在现有权限树中增加独立的“菜单权限”分组。
 */
@Primary
@Service("yakSecurityMenuAwarePermissionService")
public class MenuAwarePermissionService
        implements PermissionService {

  private final PermissionServiceImpl delegate;
  private final MenuAuthorizationService menuAuthorizationService;

  public MenuAwarePermissionService(
          PermissionServiceImpl delegate,
          MenuAuthorizationService menuAuthorizationService) {

    this.delegate = delegate;
    this.menuAuthorizationService = menuAuthorizationService;
  }

  @Override
  public PermissionTreeVO buildPermissionTreeWithHas(
          List<Long> permissionIdList) {

    List<Long> normalPermissionIds =
            MenuSelectionCodec.extractPermissionIds(
                    permissionIdList);
    Set<Long> menuManagedPermissionIds =
            menuAuthorizationService
                    .getMenuBoundPermissionIds();
    normalPermissionIds.removeIf(
            menuManagedPermissionIds::contains);

    PermissionTreeVO root = delegate
            .buildPermissionTreeWithHas(normalPermissionIds);
    return mergeMenuTree(
            root,
            MenuSelectionCodec.extractMenuIds(
                    permissionIdList));
  }

  @Override
  public PermissionTreeVO buildPermissionTree() {
    return mergeMenuTree(
            delegate.buildPermissionTree(),
            Collections.emptyList());
  }

  @Override
  public PermissionTreeVO buildPermissionTreeByRoleId(
          Long roleId) {

    PermissionTreeVO root = delegate
            .buildPermissionTreeByRoleId(roleId);
    return mergeMenuTree(
            root,
            menuAuthorizationService
                    .getMenuIdsByRoleId(roleId));
  }

  @Override
  public void savePermission(
          List<PermissionDTO> permissionDTOList) {
    delegate.savePermission(permissionDTOList);
  }

  @Override
  public void deletePermissionById(Long permissionId) {
    delegate.deletePermissionById(permissionId);
  }

  private PermissionTreeVO mergeMenuTree(
          PermissionTreeVO root,
          Collection<Long> selectedMenuIds) {

    PermissionTreeVO result = root == null
            ? PermissionTreeVO.builder()
                    .id(0L)
                    .leaf(Boolean.FALSE)
                    .has(Boolean.TRUE)
                    .childList(new ArrayList<>())
                    .build()
            : root;

    Set<String> menuBoundPermissionCodes =
            menuAuthorizationService
                    .getMenuBoundPermissionCodes();
    filterMenuBoundPermissionNodes(
            result,
            menuBoundPermissionCodes,
            true);

    if (result.getChildList() == null) {
      result.setChildList(new ArrayList<>());
    }
    result.getChildList().add(0,
            menuAuthorizationService
                    .buildMenuTree(selectedMenuIds));
    result.setLeaf(Boolean.FALSE);
    return result;
  }

  private boolean filterMenuBoundPermissionNodes(
          PermissionTreeVO node,
          Set<String> menuBoundPermissionCodes,
          boolean root) {

    if (node == null) {
      return false;
    }
    if (menuBoundPermissionCodes.contains(
            node.getPermissionCode())) {
      return false;
    }

    List<PermissionTreeVO> children =
            node.getChildList();
    if (children != null) {
      children.removeIf(child ->
              !filterMenuBoundPermissionNodes(
                      child,
                      menuBoundPermissionCodes,
                      false));
    }

    return root
            || Boolean.TRUE.equals(node.getLeaf())
            || (node.getChildList() != null
            && !node.getChildList().isEmpty());
  }
}
