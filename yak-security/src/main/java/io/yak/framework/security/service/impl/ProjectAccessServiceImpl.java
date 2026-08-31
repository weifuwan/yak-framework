package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.entity.project.ProjectBrief;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.dao.ProjectDao;
import io.yak.framework.security.dao.UserProjectDao;
import io.yak.framework.security.service.ProjectAccessService;
import io.yak.framework.security.util.CopyBeanUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 基于 Yak Security 项目关系解析当前身份可切换工作空间。
 */
@Service
public class ProjectAccessServiceImpl
        implements ProjectAccessService {

  private final ProjectDao projectDao;
  private final UserProjectDao userProjectDao;

  public ProjectAccessServiceImpl(
          ProjectDao projectDao,
          UserProjectDao userProjectDao) {
    this.projectDao = projectDao;
    this.userProjectDao = userProjectDao;
  }

  @Override
  public List<ProjectBriefVO> getSwitchableProjects(
          Long userId,
          boolean root) {

    List<Long> projectIds = root
            ? getAllProjectIds()
            : getRelatedProjectIds(userId);

    if (CollectionUtils.isEmpty(projectIds)) {
      return new ArrayList<>();
    }

    List<ProjectBriefVO> result =
            new ArrayList<>();

    for (Long projectId
            : new LinkedHashSet<>(projectIds)) {
      if (projectId == null) {
        continue;
      }

      Project project =
              projectDao.selectByProjectId(projectId);
      if (project == null
              || !Boolean.TRUE.equals(
              project.getRunning())) {
        continue;
      }

      ProjectBriefVO projectBrief =
              CopyBeanUtil.copy(
                      project,
                      ProjectBriefVO.class);
      if (projectBrief != null) {
        result.add(projectBrief);
      }
    }

    return result;
  }

  private List<Long> getAllProjectIds() {
    List<ProjectBrief> projects =
            projectDao.selectAllBriefList();
    if (CollectionUtils.isEmpty(projects)) {
      return Collections.emptyList();
    }

    return projects.stream()
            .filter(Objects::nonNull)
            .map(ProjectBrief::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
  }

  private List<Long> getRelatedProjectIds(
          Long userId) {
    if (userId == null) {
      return Collections.emptyList();
    }

    List<Long> projectIds =
            userProjectDao
                    .selectProjectIdListByUserIdList(
                            Collections.singletonList(userId));
    return projectIds == null
            ? Collections.emptyList()
            : projectIds;
  }
}
