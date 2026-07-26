package io.yak.framework.security.bootstrap;

import io.yak.framework.common.Result;
import io.yak.framework.security.common.entity.Permission;
import io.yak.framework.security.common.vo.role.RoleBriefVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.dao.PermissionDao;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.service.RoleService;
import io.yak.framework.security.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class YakSecurityBootstrapInitializerTest {
  private final YakSecurityProperties properties = new YakSecurityProperties();
  private final UserService userService = mock(UserService.class);
  private final RoleService roleService = mock(RoleService.class);
  private final RolePermissionService rolePermissionService = mock(RolePermissionService.class);
  private final PermissionDao permissionDao = mock(PermissionDao.class);
  private final YakSecurityBootstrapInitializer initializer =
          new YakSecurityBootstrapInitializer(properties, userService, roleService,
                  rolePermissionService, permissionDao);

  @Test
  void doesNothingWhenAnApplicationAlreadyHasAUser() throws Exception {
    when(userService.getAllUserBriefList()).thenReturn(List.of(new UserBriefVO()));
    initializer.run(null);
    verifyNoInteractions(roleService, rolePermissionService, permissionDao);
  }

  @Test
  void requiresAnExternallyConfiguredPasswordForAnEmptyApplication() {
    when(userService.getAllUserBriefList()).thenReturn(List.of());
    assertThrows(IllegalStateException.class, () -> initializer.run(null));
    verifyNoInteractions(roleService, rolePermissionService, permissionDao);
  }

  @Test
  void createsAdministratorRoleWithEveryPermissionAndCreatesUser() throws Exception {
    properties.getBootstrap().setPassword("environment-secret");
    Permission permission = new Permission();
    permission.setId(7L);
    RoleBriefVO role = new RoleBriefVO();
    role.setId(9L);
    when(userService.getAllUserBriefList()).thenReturn(List.of());
    when(permissionDao.selectAllAndAscOrderByLevel()).thenReturn(List.of(permission));
    when(roleService.getRoleBriefListByRoleName("系统管理员"))
            .thenReturn(List.of(), List.of(role));
    when(userService.addUser(any(), eq("yak-security-bootstrap")))
            .thenReturn(Result.success());

    initializer.run(null);

    verify(roleService).createRole(any(), eq("yak-security-bootstrap"));
    verify(rolePermissionService).updateRolePermission(9L, List.of(7L));
    verify(userService).addUser(argThat(user ->
            "admin".equals(user.getUserName())
                    && "environment-secret".equals(user.getPw())
                    && List.of(9L).equals(user.getRoleIds())),
            eq("yak-security-bootstrap"));
  }
}
