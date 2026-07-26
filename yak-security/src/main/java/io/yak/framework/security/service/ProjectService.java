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
  ProjectVO createProject(ProjectSaveDTO var1, String var2)
      throws YakSecurityException;

  ProjectVO getProjectDetailByProjectId(Long var1)
      throws YakSecurityException;

  ProjectBriefVO getProjectBriefByProjectId(Long var1);

  PagingData<ProjectVO> getProjectPage(ProjectQueryDTO var1);

  PagingData<ProjectVO> getProjectPage(ProjectQueryDTO var1,
                                              List<Long> var2);

  void deleteProjectByProjectId(Long var1, String var2);

  void updateProject(ProjectSaveDTO var1, String var2)
      throws YakSecurityException;

  void changeProjectStatus(Long var1, String var2);

  void addProjectUser(Long var1, Long var2, String var3);

  void delProjectUser(Long var1, Long var2, String var3);

  void addProjectOwner(Long var1, Long var2, String var3);

  void delProjectOwner(Long var1, Long var2, String var3);

  List<ProjectBriefVO> getProjectBriefList();

  ProjectDeleteCheckVO checkBeforeDelete(Long var1);

  PagingData<ProjectBriefVO>
  getProjectBriefPage(ProjectBriefQueryDTO var1);

  boolean checkProjectExist(Long var1);

  Result<List<UserBriefVO>> unassignedByProjectId(Long var1)
      throws YakSecurityException;

  Result<List<ProjectBriefVO>> getProjectBriefByUserId(Long var1);

  List<ProjectBriefVOWithUser>
  listProjectBriefVOWithUserByProjectIds(List<Long> var1);
}
