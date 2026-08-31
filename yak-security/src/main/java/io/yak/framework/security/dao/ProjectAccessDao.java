package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.project.Project;

import java.util.List;

/**
 * 当前身份工作空间访问查询。
 */
public interface ProjectAccessDao {

  /**
   * 查询启用工作空间简要信息。
   *
   * @param projectIds 项目范围；null 表示当前应用全部项目，空列表表示无项目
   * @return 启用工作空间列表
   */
  List<Project> selectRunningProjects(
          List<Long> projectIds);
}
