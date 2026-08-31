package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.entity.project.ProjectBrief;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.dao.ProjectDao;
import io.yak.framework.security.dao.UserProjectDao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ProjectAccessServiceImplTest {

  private final ProjectDao projectDao = mock(ProjectDao.class);
  private final UserProjectDao userProjectDao = mock(UserProjectDao.class);
  private final ProjectAccessServiceImpl service =
          new ProjectAccessServiceImpl(projectDao, userProjectDao);

  @Test
  void rootSeesEveryRunningWorkspaceWithoutMembership() {
    when(projectDao.selectAllBriefList()).thenReturn(List.of(
            projectBrief(1L),
            projectBrief(2L)));
    when(projectDao.selectByProjectId(1L))
            .thenReturn(project(1L, "p1", "启用空间", true));
    when(projectDao.selectByProjectId(2L))
            .thenReturn(project(2L, "p2", "停用空间", false));

    List<ProjectBriefVO> result =
            service.getSwitchableProjects(99L, true);

    assertThat(result)
            .extracting(ProjectBriefVO::getId)
            .containsExactly(1L);
    verifyNoInteractions(userProjectDao);
  }

  @Test
  void ordinaryUserSeesOnlyRunningRelatedWorkspaces() {
    when(userProjectDao.selectProjectIdListByUserIdList(
            List.of(7L)))
            .thenReturn(List.of(3L, 4L, 3L));
    when(projectDao.selectByProjectId(3L))
            .thenReturn(project(3L, "p3", "成员空间", true));
    when(projectDao.selectByProjectId(4L))
            .thenReturn(project(4L, "p4", "停用成员空间", false));

    List<ProjectBriefVO> result =
            service.getSwitchableProjects(7L, false);

    assertThat(result)
            .extracting(ProjectBriefVO::getId)
            .containsExactly(3L);
  }

  @Test
  void ordinaryUserWithoutIdentityHasNoWorkspace() {
    assertThat(service.getSwitchableProjects(null, false))
            .isEmpty();
    verifyNoInteractions(projectDao, userProjectDao);
  }

  private static ProjectBrief projectBrief(Long id) {
    ProjectBrief project = new ProjectBrief();
    project.setId(id);
    return project;
  }

  private static Project project(
          Long id,
          String code,
          String name,
          boolean running) {
    Project project = new Project();
    project.setId(id);
    project.setProjectCode(code);
    project.setProjectName(name);
    project.setRunning(running);
    return project;
  }
}
