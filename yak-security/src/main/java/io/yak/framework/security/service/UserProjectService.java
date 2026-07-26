package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.user.UserProjectDTO;
import io.yak.framework.security.common.entity.UserProject;
import io.yak.framework.security.common.enums.project.ProjectUserCode;
import java.util.List;

public interface UserProjectService {
  public List<Long> getUserIdListByProjectId(Long var1,
                                                ProjectUserCode var2);

  public List<Long> getProjectIdListByUserIdList(List<Long> var1);

  public void saveUserProject(Long var1, List<Long> var2);

  public void delUserProject(Long var1, List<Long> var2);

  public void saveOwnerProject(Long var1, List<Long> var2);

  public void delOwnerProject(Long var1, List<Long> var2);

  public void updateUserProject(Long var1, List<Long> var2);

  public void updateUserInformationAssociatedWithProject(Long var1,
                                                         List<Long> var2);

  public void deleteUserProjectByProjectId(Long var1);

  public void deleteOwnerProjectByProjectId(Long var1);

  public void updateOwnerProject(Long var1, List<Long> var2);

  public void updateOwnerInformationAssociatedWithProject(Long var1,
                                                          List<Long> var2);

  public List<UserProject> lisUserProjectByProjectIds(List<Long> var1);

  public List<UserProject> lisUserProjectByUserProjectDTO(UserProjectDTO var1);
}
