package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.entity.project.ProjectBrief;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.common.vo.user.CurrentUserVO;
import io.yak.framework.security.dao.ProjectDao;
import io.yak.framework.security.service.UserProjectService;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;

/** Resolves the enabled projects visible in the authenticated identity context. */
@Service
public class CurrentUserProjectResolver {

  private final ProjectDao projectDao;
  private final UserProjectService userProjectService;

  public CurrentUserProjectResolver(
          ProjectDao projectDao,
          UserProjectService userProjectService) {
    this.projectDao = projectDao;
    this.userProjectService = userProjectService;
  }

  public List<ProjectBriefVO> resolve(CurrentUserVO user) {
    if (user == null || user.getId() == null) {
      return Collections.emptyList();
    }

    Set<Long> projectIds = isRoot(user)
            ? allProjectIds()
            : new LinkedHashSet<>(
                    userProjectService.getProjectIdListByUserIdList(
                            Collections.singletonList(user.getId())));

    if (projectIds.isEmpty()) {
      return Collections.emptyList();
    }

    List<ProjectBriefVO> projects = new ArrayList<>();
    for (Long projectId : projectIds) {
      if (projectId == null) {
        continue;
      }
      Project project = projectDao.selectByProjectId(projectId);
      if (project == null || !Boolean.TRUE.equals(project.getRunning())) {
        continue;
      }
      ProjectBriefVO brief = CopyBeanUtil.copy(project, ProjectBriefVO.class);
      if (brief != null) {
        projects.add(brief);
      }
    }
    return projects;
  }

  private boolean isRoot(CurrentUserVO user) {
    return user.getPermissionCodes() != null
            && user.getPermissionCodes().contains(SecurityPermissionCode.ROOT);
  }

  private Set<Long> allProjectIds() {
    List<ProjectBrief> projects = projectDao.selectAllBriefList();
    if (projects == null || projects.isEmpty()) {
      return Collections.emptySet();
    }
    Set<Long> ids = new LinkedHashSet<>();
    projects.stream()
            .filter(Objects::nonNull)
            .map(ProjectBrief::getId)
            .filter(Objects::nonNull)
            .forEach(ids::add);
    return ids;
  }
}
