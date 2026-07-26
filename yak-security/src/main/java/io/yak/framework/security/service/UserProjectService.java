package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.user.UserProjectDTO;
import io.yak.framework.security.common.entity.UserProject;
import io.yak.framework.security.common.enums.project.ProjectUserCode;
import java.util.List;

/**
 * 用户项目关系服务接口。
 */
public interface UserProjectService {
  List<Long> getUserIdListByProjectId(Long var1,
                                                ProjectUserCode var2);

  List<Long> getProjectIdListByUserIdList(List<Long> var1);

  void saveUserProject(Long var1, List<Long> var2);

  void delUserProject(Long var1, List<Long> var2);

  void saveOwnerProject(Long var1, List<Long> var2);

  void delOwnerProject(Long var1, List<Long> var2);

  void updateUserProject(Long var1, List<Long> var2);

  void updateUserInformationAssociatedWithProject(Long var1,
                                                         List<Long> var2);

  void deleteUserProjectByProjectId(Long var1);

  void deleteOwnerProjectByProjectId(Long var1);

  void updateOwnerProject(Long var1, List<Long> var2);

  void updateOwnerInformationAssociatedWithProject(Long var1,
                                                          List<Long> var2);

  List<UserProject> lisUserProjectByProjectIds(List<Long> var1);

  List<UserProject> lisUserProjectByUserProjectDTO(UserProjectDTO var1);
}
