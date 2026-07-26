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

public interface ProjectService {
  public ProjectVO createProject(ProjectSaveDTO var1, String var2)
      throws YakSecurityException;

  public ProjectVO getProjectDetailByProjectId(Long var1)
      throws YakSecurityException;

  public ProjectBriefVO getProjectBriefByProjectId(Long var1);

  public PagingData<ProjectVO> getProjectPage(ProjectQueryDTO var1);

  public PagingData<ProjectVO> getProjectPage(ProjectQueryDTO var1,
                                              List<Long> var2);

  public void deleteProjectByProjectId(Long var1, String var2);

  public void updateProject(ProjectSaveDTO var1, String var2)
      throws YakSecurityException;

  public void changeProjectStatus(Long var1, String var2);

  public void addProjectUser(Long var1, Long var2, String var3);

  public void delProjectUser(Long var1, Long var2, String var3);

  public void addProjectOwner(Long var1, Long var2, String var3);

  public void delProjectOwner(Long var1, Long var2, String var3);

  public List<ProjectBriefVO> getProjectBriefList();

  public ProjectDeleteCheckVO checkBeforeDelete(Long var1);

  public PagingData<ProjectBriefVO>
  getProjectBriefPage(ProjectBriefQueryDTO var1);

  public boolean checkProjectExist(Long var1);

  public Result<List<UserBriefVO>> unassignedByProjectId(Long var1)
      throws YakSecurityException;

  public Result<List<ProjectBriefVO>> getProjectBriefByUserId(Long var1);

  public List<ProjectBriefVOWithUser>
  listProjectBriefVOWithUserByProjectIds(List<Long> var1);
}
