package io.yak.framework.security.service;

import io.yak.framework.security.common.vo.project.ProjectBriefVO;

import java.util.List;

/**
 * 当前身份可访问工作空间解析服务。
 */
public interface ProjectAccessService {

  /**
   * 查询当前身份可以切换进入的启用工作空间。
   *
   * <p>超级管理员不受用户项目关系限制，返回当前应用全部启用工作空间；普通用户仅返回其作为
   * 项目负责人或普通成员关联的启用工作空间。
   *
   * @param userId 当前用户 ID
   * @param root 是否拥有 {@code security:root}
   * @return 可切换的工作空间列表
   */
  List<ProjectBriefVO> getSwitchableProjects(
          Long userId,
          boolean root);
}
