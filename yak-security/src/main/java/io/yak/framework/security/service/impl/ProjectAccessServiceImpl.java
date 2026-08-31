package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.dao.ProjectAccessDao;
import io.yak.framework.security.dao.UserProjectDao;
import io.yak.framework.security.service.ProjectAccessService;
import io.yak.framework.security.util.CopyBeanUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 基于 Yak Security 项目关系解析当前身份可切换工作空间。
 */
@Service
public class ProjectAccessServiceImpl
        implements ProjectAccessService {

  private final ProjectAccessDao projectAccessDao;
  private final UserProjectDao userProjectDao;

  public ProjectAccessServiceImpl(
          ProjectAccessDao projectAccessDao,
          UserProjectDao userProjectDao) {
    this.projectAccessDao = projectAccessDao;
    this.userProjectDao = userProjectDao;
  }

  @Override
  public List<ProjectBriefVO> getSwitchableProjects(
          Long userId,
          boolean root) {

    List<Long> projectIds =
            root ? null : getRelatedProjectIds(userId);

    if (!root
            && CollectionUtils.isEmpty(projectIds)) {
      return new ArrayList<>();
    }

    List<Project> projects =
            projectAccessDao.selectRunningProjects(
                    projectIds);

    return new ArrayList<>(
            CopyBeanUtil.copyList(
                    projects,
                    ProjectBriefVO.class));
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
