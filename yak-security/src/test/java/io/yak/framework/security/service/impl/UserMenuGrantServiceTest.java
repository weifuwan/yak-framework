package io.yak.framework.security.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.yak.framework.security.common.po.MenuPO;
import io.yak.framework.security.common.po.RoleMenuPO;
import io.yak.framework.security.dao.mapper.MenuMapper;
import io.yak.framework.security.dao.mapper.RoleMenuMapper;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.service.UserRoleService;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class UserMenuGrantServiceTest {

  @Test
  void expandsGrantedDirectoryToActiveDescendants() {
    Fixture fixture = new Fixture();
    MenuPO integration = menu(
        10L, "integration", null, null, true, true);
    MenuPO batch = menu(
        11L, "batch-link-up", "integration",
        "task:batch:read", true, true);
    MenuPO realtime = menu(
        12L, "realtime-link-up", "integration",
        "task:realtime:read", true, true);
    MenuPO hiddenKnowledge = menu(
        13L, "knowledge-management", null,
        "knowledge:read", true, false);

    when(fixture.userRoleService.getRoleIdListByUserId(7L))
        .thenReturn(Collections.singletonList(3L));
    when(fixture.roleMenuMapper.selectList(any()))
        .thenReturn(Arrays.asList(
            relation(3L, 10L),
            relation(3L, 13L)));
    when(fixture.menuMapper.selectList(any()))
        .thenReturn(Arrays.asList(
            integration,
            batch,
            realtime,
            hiddenKnowledge));

    UserMenuGrantService.MenuGrant grant = fixture.service.resolve(7L);

    assertThat(grant.getMenuCodes()).containsExactlyInAnyOrder(
        "integration",
        "batch-link-up",
        "realtime-link-up",
        "knowledge-management");
    assertThat(grant.getPermissionCodes()).containsExactlyInAnyOrder(
        "task:batch:read",
        "task:realtime:read",
        "knowledge:read");
  }

  @Test
  void addsParentDirectoryForGrantedLeafWithoutGrantingSiblings() {
    Fixture fixture = new Fixture();
    when(fixture.userRoleService.getRoleIdListByUserId(8L))
        .thenReturn(Collections.singletonList(4L));
    when(fixture.roleMenuMapper.selectList(any()))
        .thenReturn(Collections.singletonList(
            relation(4L, 11L)));
    when(fixture.menuMapper.selectList(any()))
        .thenReturn(Arrays.asList(
            menu(10L, "integration", null, null, true, true),
            menu(11L, "batch-link-up", "integration",
                "task:batch:read", true, true),
            menu(12L, "realtime-link-up", "integration",
                "task:realtime:read", true, true)));

    UserMenuGrantService.MenuGrant grant = fixture.service.resolve(8L);

    assertThat(grant.getMenuCodes())
        .containsExactlyInAnyOrder(
            "integration",
            "batch-link-up")
        .doesNotContain("realtime-link-up");
    assertThat(grant.getPermissionCodes())
        .containsExactly("task:batch:read");
  }

  @Test
  void actionPermissionGrantsMenuEvenWithoutRoleMenuRelation() {
    Fixture fixture = new Fixture();
    when(fixture.userRoleService.getRoleIdListByUserId(9L))
        .thenReturn(Collections.singletonList(5L));
    when(fixture.roleMenuMapper.selectList(any()))
        .thenReturn(Collections.emptyList());
    when(fixture.rolePermissionService.getPermissionIdListByRoleIdList(
        Collections.singletonList(5L)))
        .thenReturn(Collections.singletonList(90L));
    when(fixture.permissionMenuRelationService.inferMenuIds(
        Collections.singletonList(90L)))
        .thenReturn(Arrays.asList(11L, 10L));
    when(fixture.menuMapper.selectList(any()))
        .thenReturn(Arrays.asList(
            menu(10L, "integration", null, null, true, true),
            menu(11L, "batch-link-up", "integration",
                "task:batch:read", true, true)));

    UserMenuGrantService.MenuGrant grant = fixture.service.resolve(9L);

    assertThat(grant.getMenuCodes())
        .containsExactlyInAnyOrder("integration", "batch-link-up");
    assertThat(grant.getPermissionCodes())
        .containsExactly("task:batch:read");
  }

  private static final class Fixture {
    private final MenuMapper menuMapper = mock(MenuMapper.class);
    private final RoleMenuMapper roleMenuMapper = mock(RoleMenuMapper.class);
    private final UserRoleService userRoleService = mock(UserRoleService.class);
    private final RolePermissionService rolePermissionService =
        mock(RolePermissionService.class);
    private final PermissionMenuRelationService permissionMenuRelationService =
        mock(PermissionMenuRelationService.class);
    private final UserMenuGrantService service;

    private Fixture() {
      when(rolePermissionService.getPermissionIdListByRoleIdList(any()))
          .thenReturn(Collections.emptyList());
      when(permissionMenuRelationService.inferMenuIds(any()))
          .thenReturn(Collections.emptyList());
      service = new UserMenuGrantService(
          menuMapper,
          roleMenuMapper,
          userRoleService,
          rolePermissionService,
          permissionMenuRelationService);
    }
  }

  private static MenuPO menu(
      Long id,
      String code,
      String parentCode,
      String permissionCode,
      boolean active,
      boolean visible) {
    MenuPO menu = new MenuPO();
    menu.setId(id);
    menu.setMenuCode(code);
    menu.setMenuName(code);
    menu.setParentCode(parentCode);
    menu.setRequiredPermissionCode(permissionCode);
    menu.setActive(active);
    menu.setVisible(visible);
    return menu;
  }

  private static RoleMenuPO relation(Long roleId, Long menuId) {
    RoleMenuPO relation = new RoleMenuPO();
    relation.setRoleId(roleId);
    relation.setMenuId(menuId);
    return relation;
  }
}
