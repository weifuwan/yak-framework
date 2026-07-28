package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.po.MenuPO;
import io.yak.framework.security.common.po.RoleMenuPO;
import io.yak.framework.security.dao.mapper.MenuMapper;
import io.yak.framework.security.dao.mapper.RoleMenuMapper;
import io.yak.framework.security.service.UserRoleService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Resolves effective menu grants for one user.
 *
 * <p>A checked directory grants its active descendants. A checked page also
 * brings its parent directories into the returned menu-code set. This makes
 * the backend authoritative even when a tree client submits only directory
 * keys or only leaf keys.</p>
 */
@Service("yakSecurityUserMenuGrantService")
public class UserMenuGrantService {

  private final MenuMapper menuMapper;
  private final RoleMenuMapper roleMenuMapper;
  private final UserRoleService userRoleService;

  public UserMenuGrantService(
          MenuMapper menuMapper,
          RoleMenuMapper roleMenuMapper,
          UserRoleService userRoleService) {
    this.menuMapper = menuMapper;
    this.roleMenuMapper = roleMenuMapper;
    this.userRoleService = userRoleService;
  }

  /** Resolve effective menu codes and menu-implied permission codes. */
  public MenuGrant resolve(Long userId) {
    if (userId == null) {
      return MenuGrant.empty();
    }

    List<Long> roleIds = normalizeIds(
            userRoleService.getRoleIdListByUserId(userId));
    if (roleIds.isEmpty()) {
      return MenuGrant.empty();
    }

    List<RoleMenuPO> relations = roleMenuMapper.selectList(
            Wrappers.<RoleMenuPO>lambdaQuery()
                    .in(RoleMenuPO::getRoleId, roleIds));
    if (CollectionUtils.isEmpty(relations)) {
      return MenuGrant.empty();
    }

    List<MenuPO> menus = menuMapper.selectList(
            Wrappers.<MenuPO>lambdaQuery()
                    .orderByAsc(MenuPO::getSortOrder)
                    .orderByAsc(MenuPO::getId));
    if (CollectionUtils.isEmpty(menus)) {
      return MenuGrant.empty();
    }

    Map<Long, MenuPO> byId = new LinkedHashMap<>();
    Map<String, MenuPO> byCode = new LinkedHashMap<>();
    Map<String, List<MenuPO>> childrenByParentCode = new HashMap<>();

    for (MenuPO menu : menus) {
      if (menu == null
              || menu.getId() == null
              || !StringUtils.hasText(menu.getMenuCode())) {
        continue;
      }
      byId.put(menu.getId(), menu);
      byCode.put(menu.getMenuCode(), menu);
      if (StringUtils.hasText(menu.getParentCode())) {
        childrenByParentCode
                .computeIfAbsent(
                        menu.getParentCode(),
                        ignored -> new ArrayList<>())
                .add(menu);
      }
    }

    Set<String> effectiveCodes = new LinkedHashSet<>();
    for (RoleMenuPO relation : relations) {
      if (relation == null || relation.getMenuId() == null) {
        continue;
      }
      MenuPO selected = byId.get(relation.getMenuId());
      if (!isActive(selected)) {
        continue;
      }
      addDescendants(
              selected,
              childrenByParentCode,
              effectiveCodes,
              new HashSet<>());
    }

    Set<String> snapshot = new LinkedHashSet<>(effectiveCodes);
    for (String code : snapshot) {
      addParents(
              byCode.get(code),
              byCode,
              effectiveCodes,
              new HashSet<>());
    }

    Set<String> permissionCodes = new LinkedHashSet<>();
    for (String code : effectiveCodes) {
      MenuPO menu = byCode.get(code);
      if (isActive(menu)
              && StringUtils.hasText(
              menu.getRequiredPermissionCode())) {
        permissionCodes.add(
                menu.getRequiredPermissionCode());
      }
    }

    return new MenuGrant(
            new ArrayList<>(effectiveCodes),
            new ArrayList<>(permissionCodes));
  }

  public List<String> getMenuCodesByUserId(Long userId) {
    return resolve(userId).getMenuCodes();
  }

  public List<String> getPermissionCodesByUserId(Long userId) {
    return resolve(userId).getPermissionCodes();
  }

  private void addDescendants(
          MenuPO menu,
          Map<String, List<MenuPO>> childrenByParentCode,
          Set<String> result,
          Set<String> visited) {
    if (!isActive(menu)
            || !visited.add(menu.getMenuCode())) {
      return;
    }

    result.add(menu.getMenuCode());
    for (MenuPO child : childrenByParentCode.getOrDefault(
            menu.getMenuCode(),
            Collections.emptyList())) {
      addDescendants(
              child,
              childrenByParentCode,
              result,
              visited);
    }
  }

  private void addParents(
          MenuPO menu,
          Map<String, MenuPO> byCode,
          Set<String> result,
          Set<String> visited) {
    MenuPO current = menu;
    while (isActive(current)
            && visited.add(current.getMenuCode())) {
      result.add(current.getMenuCode());
      current = StringUtils.hasText(current.getParentCode())
              ? byCode.get(current.getParentCode())
              : null;
    }
  }

  /**
   * Visibility controls sidebar rendering, not authorization. Hidden pages such
   * as knowledge management must still be resolvable when explicitly granted.
   */
  private boolean isActive(MenuPO menu) {
    return menu != null
            && Boolean.TRUE.equals(menu.getActive())
            && StringUtils.hasText(menu.getMenuCode());
  }

  private List<Long> normalizeIds(Collection<Long> values) {
    if (CollectionUtils.isEmpty(values)) {
      return new ArrayList<>();
    }
    return values.stream()
            .filter(Objects::nonNull)
            .filter(value -> value > 0L)
            .distinct()
            .collect(Collectors.toList());
  }

  /** Effective grants returned as immutable value lists. */
  public static final class MenuGrant {
    private final List<String> menuCodes;
    private final List<String> permissionCodes;

    private MenuGrant(
            List<String> menuCodes,
            List<String> permissionCodes) {
      this.menuCodes = Collections.unmodifiableList(
              new ArrayList<>(menuCodes));
      this.permissionCodes = Collections.unmodifiableList(
              new ArrayList<>(permissionCodes));
    }

    public static MenuGrant empty() {
      return new MenuGrant(
              Collections.emptyList(),
              Collections.emptyList());
    }

    public List<String> getMenuCodes() {
      return menuCodes;
    }

    public List<String> getPermissionCodes() {
      return permissionCodes;
    }
  }
}
