package io.yak.framework.security.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.context.AuthorizationSnapshot;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.service.PermissionCache;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.service.UserProjectService;
import io.yak.framework.security.service.UserRoleService;
import java.util.List;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class AuthorizationSnapshotServiceTest {

  @Test
  @SuppressWarnings("unchecked")
  void loadsAuthorizationFactsOnceWithoutRequeryingRolesForMenus() {
    PermissionCache permissionCache = mock(PermissionCache.class);
    when(permissionCache.getAuthorizationSnapshot(eq(7L), any()))
            .thenAnswer(invocation ->
                    ((Supplier<AuthorizationSnapshot>)
                            invocation.getArgument(1)).get());

    UserRoleService userRoleService = mock(UserRoleService.class);
    when(userRoleService.getRoleIdListByUserId(7L))
            .thenReturn(List.of(3L));
    RolePermissionService rolePermissionService =
            mock(RolePermissionService.class);
    when(rolePermissionService.getPermissionIdListByRoleIdList(
            List.of(3L)))
            .thenReturn(List.of(5L));

    Permission permission = mock(Permission.class);
    when(permission.getId()).thenReturn(5L);
    when(permission.getActive()).thenReturn(true);
    when(permission.getPermissionCode()).thenReturn("dataset:read");
    PermissionDao permissionDao = mock(PermissionDao.class);
    when(permissionDao.selectAllAndAscOrderByLevel())
            .thenReturn(List.of(permission));

    UserMenuGrantService userMenuGrantService =
            mock(UserMenuGrantService.class);
    when(userMenuGrantService.resolve(
            List.of(3L),
            List.of(5L)))
            .thenReturn(UserMenuGrantService.MenuGrant.empty());

    UserProjectService userProjectService =
            mock(UserProjectService.class);
    when(userProjectService.getProjectIdListByUserIdList(
            List.of(7L)))
            .thenReturn(List.of(9L));

    AuthorizationSnapshotService service =
            new AuthorizationSnapshotService(
                    permissionCache,
                    userRoleService,
                    rolePermissionService,
                    permissionDao,
                    userMenuGrantService,
                    userProjectService);

    AuthorizationSnapshot snapshot = service.get(7L);

    assertThat(snapshot.getRoleIds()).containsExactly(3L);
    assertThat(snapshot.getPermissionCodes()).containsExactly("dataset:read");
    assertThat(snapshot.getProjectIds()).containsExactly(9L);
    verify(userRoleService).getRoleIdListByUserId(7L);
    verify(rolePermissionService)
            .getPermissionIdListByRoleIdList(List.of(3L));
    verify(userMenuGrantService)
            .resolve(List.of(3L), List.of(5L));
  }
}
