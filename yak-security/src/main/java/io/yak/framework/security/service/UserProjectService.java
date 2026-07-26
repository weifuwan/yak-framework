package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.user.UserProjectDTO;
import io.yak.framework.security.common.entity.UserProject;
import io.yak.framework.security.common.enums.project.ProjectUserCode;
import java.util.List;

/**
 * 用户项目关系服务接口。
 */
public interface UserProjectService {
  /**
   * 根据项目 ID 查询用户 ID 集合。
   */
  List<Long> getUserIdListByProjectId(Long var1,
                                                ProjectUserCode var2);

  /**
   * 根据用户 ID 集合查询项目 ID 集合。
   */
  List<Long> getProjectIdListByUserIdList(List<Long> var1);

  /**
   * 保存用户项目关系。
   */
  void saveUserProject(Long var1, List<Long> var2);

  /**
   * 删除用户项目关系。
   */
  void delUserProject(Long var1, List<Long> var2);

  /**
   * 保存负责人项目关系。
   */
  void saveOwnerProject(Long var1, List<Long> var2);

  /**
   * 删除负责人项目关系。
   */
  void delOwnerProject(Long var1, List<Long> var2);

  /**
   * 更新用户项目关系。
   */
  void updateUserProject(Long var1, List<Long> var2);

  /**
   * 更新项目关联的用户信息。
   */
  void updateUserInformationAssociatedWithProject(Long var1,
                                                         List<Long> var2);

  /**
   * 根据项目 ID 删除用户项目关系。
   */
  void deleteUserProjectByProjectId(Long var1);

  /**
   * 根据项目 ID 删除负责人项目关系。
   */
  void deleteOwnerProjectByProjectId(Long var1);

  /**
   * 更新负责人项目关系。
   */
  void updateOwnerProject(Long var1, List<Long> var2);

  /**
   * 更新项目关联的负责人信息。
   */
  void updateOwnerInformationAssociatedWithProject(Long var1,
                                                          List<Long> var2);

  /**
   * 根据项目 ID 集合查询用户项目关系。
   */
  List<UserProject> lisUserProjectByProjectIds(List<Long> var1);

  /**
   * 根据查询条件查询用户项目关系。
   */
  List<UserProject> lisUserProjectByUserProjectDTO(UserProjectDTO var1);
}
