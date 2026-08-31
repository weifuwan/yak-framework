package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.dao.ProjectAccessDao;
import io.yak.framework.security.dao.UserProjectDao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ProjectAccessServiceImplTest {

  private final ProjectAccessDao projectAccessDao =
          mock(ProjectAccessDao.class);
  private final UserProjectDao userProjectDao =
          mock(UserProjectDao.class);
  private final ProjectAccessServiceImpl service =
          new ProjectAccessServiceImpl(
                  projectAccessDao,
                  userProjectDao);

  @Test
  void rootQueriesEveryRunningWorkspaceWithoutMembership() {
    when(projectAccessDao.selectRunningProjects(null))
            .thenReturn(List.of(
                    project(1L, "p1", "启用空间"),
                    project(2L, "p2", "数据空间")));

    List<ProjectBriefVO> result =
            service.getSwitchableProjects(99L, true);

    assertThat(result)
            .extracting(ProjectBriefVO::getId)
            .containsExactly(1L, 2L);
    verify(projectAccessDao)
            .selectRunningProjects(null);
    verifyNoInteractions(userProjectDao);
  }

  @Test
  void ordinaryUserQueriesOnlyRelatedRunningWorkspaces() {
    List<Long> relatedIds =
            List.of(3L, 4L, 3L);
    when(userProjectDao.selectProjectIdListByUserIdList(
            List.of(7L)))
            .thenReturn(relatedIds);
    when(projectAccessDao.selectRunningProjects(relatedIds))
            .thenReturn(List.of(
                    project(3L, "p3", "成员空间")));

    List<ProjectBriefVO> result =
            service.getSwitchableProjects(7L, false);

    assertThat(result)
            .extracting(ProjectBriefVO::getId)
            .containsExactly(3L);
    verify(projectAccessDao)
            .selectRunningProjects(relatedIds);
  }

  @Test
  void ordinaryUserWithoutIdentityHasNoWorkspace() {
    assertThat(service.getSwitchableProjects(null, false))
            .isEmpty();
    verifyNoInteractions(
            projectAccessDao,
            userProjectDao);
  }

  private static Project project(
          Long id,
          String code,
          String name) {
    Project project = new Project();
    project.setId(id);
    project.setProjectCode(code);
    project.setProjectName(name);
    project.setRunning(true);
    return project;
  }
}
