package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.user.UserProjectDTO;
import io.yak.framework.security.common.entity.UserProject;
import io.yak.framework.security.common.enums.project.ProjectUserCode;
import java.util.List;

public interface UserProjectService {
  public List<Integer> getUserIdListByProjectId(Integer var1,
                                                ProjectUserCode var2);

  public List<Integer> getProjectIdListByUserIdList(List<Integer> var1);

  public void saveUserProject(Integer var1, List<Integer> var2);

  public void delUserProject(Integer var1, List<Integer> var2);

  public void saveOwnerProject(Integer var1, List<Integer> var2);

  public void delOwnerProject(Integer var1, List<Integer> var2);

  public void updateUserProject(Integer var1, List<Integer> var2);

  public void updateUserInformationAssociatedWithProject(Integer var1,
                                                         List<Integer> var2);

  public void deleteUserProjectByProjectId(Integer var1);

  public void deleteOwnerProjectByProjectId(Integer var1);

  public void updateOwnerProject(Integer var1, List<Integer> var2);

  public void updateOwnerInformationAssociatedWithProject(Integer var1,
                                                          List<Integer> var2);

  public List<UserProject> lisUserProjectByProjectIds(List<Integer> var1);

  public List<UserProject> lisUserProjectByUserProjectDTO(UserProjectDTO var1);
}
