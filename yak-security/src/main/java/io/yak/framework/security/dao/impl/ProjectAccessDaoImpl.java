package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.entity.project.Project;
import io.yak.framework.security.common.po.ProjectPO;
import io.yak.framework.security.dao.ProjectAccessDao;
import io.yak.framework.security.dao.mapper.ProjectMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 当前身份工作空间访问查询实现。
 */
@Repository
@RequiredArgsConstructor
public class ProjectAccessDaoImpl
        implements ProjectAccessDao {

  private final ProjectMapper projectMapper;

  @Override
  public List<Project> selectRunningProjects(
          List<Long> projectIds) {

    if (projectIds != null
            && projectIds.isEmpty()) {
      return Collections.emptyList();
    }

    LambdaQueryWrapper<ProjectPO> wrapper =
            Wrappers.<ProjectPO>lambdaQuery()
                    .select(
                            ProjectPO::getId,
                            ProjectPO::getProjectCode,
                            ProjectPO::getProjectName)
                    .eq(ProjectPO::getRunning, true)
                    .in(
                            projectIds != null,
                            ProjectPO::getId,
                            projectIds)
                    .orderByAsc(ProjectPO::getProjectName);

    return CopyBeanUtil.copyList(
            projectMapper.selectList(wrapper),
            Project.class);
  }
}
