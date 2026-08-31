package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.common.vo.role.RoleBriefVO;
import io.yak.framework.security.common.vo.user.CurrentUserVO;
import io.yak.framework.security.context.CurrentUser;
import io.yak.framework.security.service.LoginService;
import io.yak.framework.security.service.ProjectAccessService;
import io.yak.framework.security.service.RoleService;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.service.impl.UserMenuGrantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginControllerCurrentUserTest {

  @Test
  void rootCurrentIdentityIncludesRolesAndAllSwitchableWorkspaces() {
    LoginService loginService = mock(LoginService.class);
    UserService userService = mock(UserService.class);
    RoleService roleService = mock(RoleService.class);
    ProjectAccessService projectAccessService =
            mock(ProjectAccessService.class);
    CurrentUser currentUser = mock(CurrentUser.class);
    @SuppressWarnings("unchecked")
    ObjectProvider<UserMenuGrantService> menuGrantProvider =
            mock(ObjectProvider.class);

    CurrentUserVO identity = new CurrentUserVO();
    identity.setId(7L);
    identity.setUserName("root");
    identity.setPermissionCodes(
            List.of(SecurityPermissionCode.ROOT));

    RoleBriefVO role = new RoleBriefVO();
    role.setId(1L);
    role.setRoleName("系统管理员");

    ProjectBriefVO project = new ProjectBriefVO();
    project.setId(9L);
    project.setProjectCode("p9");
    project.setProjectName("默认空间");

    when(currentUser.isAuthenticated()).thenReturn(true);
    when(currentUser.getUsername()).thenReturn("root");
    when(userService.getCurrentUserByUsername("root"))
            .thenReturn(identity);
    when(roleService.getRoleBriefListByUserId(7L))
            .thenReturn(List.of(role));
    when(projectAccessService.getSwitchableProjects(7L, true))
            .thenReturn(List.of(project));
    when(menuGrantProvider.getIfAvailable()).thenReturn(null);

    LoginController controller = new LoginController(
            loginService,
            userService,
            roleService,
            projectAccessService,
            currentUser,
            menuGrantProvider);

    controller.current();

    assertThat(identity.getRoleList())
            .extracting(RoleBriefVO::getRoleName)
            .containsExactly("系统管理员");
    assertThat(identity.getProjectList())
            .extracting(ProjectBriefVO::getProjectName)
            .containsExactly("默认空间");
    verify(projectAccessService)
            .getSwitchableProjects(7L, true);
  }
}
