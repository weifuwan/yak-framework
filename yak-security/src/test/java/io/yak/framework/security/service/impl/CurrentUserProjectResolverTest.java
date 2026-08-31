package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.entity.project.ProjectBrief;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.common.vo.user.CurrentUserVO;
import io.yak.framework.security.dao.ProjectDao;
import io.yak.framework.security.service.UserProjectService;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrentUserProjectResolverTest {

  private final ProjectDao projectDao = mock(ProjectDao.class);
  private final UserProjectService userProjectService = mock(UserProjectService.class);
  private final CurrentUserProjectResolver resolver =
          new CurrentUserProjectResolver(projectDao, userProjectService);

  @Test
  void rootSeesEveryEnabledProjectWithoutMembershipRelation() {
    CurrentUserVO user = new CurrentUserVO();
    user.setId(1L);
    user.setPermissionCodes(List.of(SecurityPermissionCode.ROOT));

    ProjectBrief first = brief(10L);
    ProjectBrief second = brief(20L);
    when(projectDao.selectAllBriefList()).thenReturn(List.of(first, second));
    when(projectDao.selectByProjectId(10L)).thenReturn(project(10L, "默认空间", true));
    when(projectDao.selectByProjectId(20L)).thenReturn(project(20L, "停用空间", false));

    List<ProjectBriefVO> result = resolver.resolve(user);

    assertThat(result).extracting(ProjectBriefVO::getId).containsExactly(10L);
  }

  @Test
  void ordinaryUserOnlySeesAssignedEnabledProjects() {
    CurrentUserVO user = new CurrentUserVO();
    user.setId(2L);
    user.setPermissionCodes(List.of());

    when(userProjectService.getProjectIdListByUserIdList(List.of(2L)))
            .thenReturn(List.of(10L, 20L));
    when(projectDao.selectByProjectId(10L)).thenReturn(project(10L, "成员空间", true));
    when(projectDao.selectByProjectId(20L)).thenReturn(project(20L, "停用空间", false));

    List<ProjectBriefVO> result = resolver.resolve(user);

    assertThat(result).extracting(ProjectBriefVO::getId).containsExactly(10L);
  }

  private static ProjectBrief brief(Long id) {
    ProjectBrief project = new ProjectBrief();
    project.setId(id);
    return project;
  }

  private static Project project(Long id, String name, boolean running) {
    Project project = new Project();
    project.setId(id);
    project.setProjectCode("p" + id);
    project.setProjectName(name);
    project.setRunning(running);
    return project;
  }
}
