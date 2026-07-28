package io.yak.framework.security.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuAwareRolePermissionServiceTest {

  @Mock
  private RolePermissionServiceImpl delegate;

  @Mock
  private MenuAuthorizationService menuAuthorizationService;

  private MenuAwareRolePermissionService service;

  @BeforeEach
  void setUp() {
    service = new MenuAwareRolePermissionService(
            delegate,
            menuAuthorizationService);
  }

  @Test
  void separatesMenuSelectionsWhenUpdatingRole() {
    long encodedMenuId = MenuSelectionCodec.encodeMenuId(8L);
    when(menuAuthorizationService.getMenuBoundPermissionIds())
            .thenReturn(new LinkedHashSet<>(
                    Collections.singletonList(20L)));

    service.updateRolePermission(
            3L,
            Arrays.asList(10L, 20L, encodedMenuId));

    verify(delegate).updateRolePermission(
            3L,
            Collections.singletonList(10L));
    verify(menuAuthorizationService).updateRoleMenus(
            3L,
            Collections.singletonList(8L));
  }

  @Test
  void replacesDirectReadGrantsWithMenuDerivedGrants() {
    when(delegate.getPermissionIdListByRoleIdList(
            Arrays.asList(1L, 2L)))
            .thenReturn(Arrays.asList(10L, 20L));
    when(menuAuthorizationService.getMenuBoundPermissionIds())
            .thenReturn(new LinkedHashSet<>(
                    Collections.singletonList(20L)));
    when(menuAuthorizationService.getRequiredPermissionIdsByRoleIds(
            Arrays.asList(1L, 2L)))
            .thenReturn(Arrays.asList(30L, 30L));

    List<Long> result = service.getPermissionIdListByRoleIdList(
            Arrays.asList(1L, 2L));

    assertThat(result).containsExactly(10L, 30L);
  }
}
