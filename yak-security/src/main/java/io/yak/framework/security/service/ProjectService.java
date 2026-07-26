package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.project.ProjectBriefQueryDTO;
import io.yak.framework.security.common.dto.project.ProjectQueryDTO;
import io.yak.framework.security.common.dto.project.ProjectSaveDTO;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.common.vo.project.ProjectBriefVOWithUser;
import io.yak.framework.security.common.vo.project.ProjectDeleteCheckVO;
import io.yak.framework.security.common.vo.project.ProjectVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import java.util.List;

/**
 * 项目服务接口。
 */
public interface ProjectService {
  /**
   * 创建项目。
   */
  ProjectVO createProject(ProjectSaveDTO var1, String var2)
      throws YakSecurityException;

  /**
   * 根据项目 ID 查询项目详情。
   */
  ProjectVO getProjectDetailByProjectId(Long var1)
      throws YakSecurityException;

  /**
   * 根据项目 ID 查询项目简要信息。
   */
  ProjectBriefVO getProjectBriefByProjectId(Long var1);

  /**
   * 分页查询项目。
   */
  PagingData<ProjectVO> getProjectPage(ProjectQueryDTO var1);

  /**
   * 分页查询项目。
   */
  PagingData<ProjectVO> getProjectPage(ProjectQueryDTO var1,
                                              List<Long> var2);

  /**
   * 根据项目 ID 删除项目。
   */
  void deleteProjectByProjectId(Long var1, String var2);

  /**
   * 更新项目。
   */
  void updateProject(ProjectSaveDTO var1, String var2)
      throws YakSecurityException;

  /**
   * 变更项目状态。
   */
  void changeProjectStatus(Long var1, String var2);

  /**
   * 添加项目用户。
   */
  void addProjectUser(Long var1, Long var2, String var3);

  /**
   * 删除项目用户。
   */
  void delProjectUser(Long var1, Long var2, String var3);

  /**
   * 添加项目负责人。
   */
  void addProjectOwner(Long var1, Long var2, String var3);

  /**
   * 删除项目负责人。
   */
  void delProjectOwner(Long var1, Long var2, String var3);

  /**
   * 查询全部项目简要信息。
   */
  List<ProjectBriefVO> getProjectBriefList();

  /**
   * 执行删除前校验。
   */
  ProjectDeleteCheckVO checkBeforeDelete(Long var1);

  /**
   * 分页查询项目简要信息。
   */
  PagingData<ProjectBriefVO>
  getProjectBriefPage(ProjectBriefQueryDTO var1);

  /**
   * 校验项目是否存在。
   */
  boolean checkProjectExist(Long var1);

  /**
   * 根据项目 ID 查询未分配用户。
   */
  Result<List<UserBriefVO>> unassignedByProjectId(Long var1)
      throws YakSecurityException;

  /**
   * 根据用户 ID 查询项目简要信息。
   */
  Result<List<ProjectBriefVO>> getProjectBriefByUserId(Long var1);

  /**
   * 根据项目 ID 集合查询包含用户的项目简要信息。
   */
  List<ProjectBriefVOWithUser>
  listProjectBriefVOWithUserByProjectIds(List<Long> var1);
}
