package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.dto.user.UserProjectDTO;
import io.yak.framework.security.common.entity.UserProject;
import io.yak.framework.security.common.enums.project.ProjectUserCode;
import io.yak.framework.security.dao.UserProjectDao;
import io.yak.framework.security.service.UserProjectService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value = "yakSecurityUserProjectServiceImpl")
public class UserProjectServiceImpl implements UserProjectService {
  private static final int NORMAL = 0;
  private static final int OWNER = 1;
  @Autowired private UserProjectDao userProjectDao;

  @Override
  public List<Long> getUserIdListByProjectId(Long projectId,
                                                ProjectUserCode code) {
    if (projectId == null) {
      return new ArrayList<Long>();
    }
    return this.userProjectDao.selectUserIdListByProjectId(projectId,
                                                           code.getType());
  }

  @Override
  public List<Long> getProjectIdListByUserIdList(List<Long> userIdList) {
    if (CollectionUtils.isEmpty(userIdList)) {
      return new ArrayList<Long>();
    }
    return this.userProjectDao.selectProjectIdListByUserIdList(userIdList);
  }

  @Override
  public void saveUserProject(Long projectId, List<Long> userIdList) {
    if (projectId == null || CollectionUtils.isEmpty(userIdList)) {
      return;
    }
    this.userProjectDao.insertBatch(
        this.getUserProjectList(projectId, userIdList, 0));
  }

  @Override
  public void delUserProject(Long projectId, List<Long> userIdList) {
    if (projectId == null || CollectionUtils.isEmpty(userIdList)) {
      return;
    }
    this.userProjectDao.deleteUserProject(
        this.getUserProjectList(projectId, userIdList, 0));
  }

  @Override
  public void saveOwnerProject(Long projectId, List<Long> ownerIdList) {
    if (projectId == null || CollectionUtils.isEmpty(ownerIdList)) {
      return;
    }
    this.userProjectDao.insertBatch(
        this.getUserProjectList(projectId, ownerIdList, 1));
  }

  @Override
  public void delOwnerProject(Long projectId, List<Long> ownerIdList) {
    if (projectId == null || CollectionUtils.isEmpty(ownerIdList)) {
      return;
    }
    this.userProjectDao.deleteUserProject(
        this.getUserProjectList(projectId, ownerIdList, 1));
  }

  @Override
  public void updateUserProject(Long projectId, List<Long> userIdList) {
    this.deleteUserProjectByProjectId(projectId);
    this.saveUserProject(projectId, userIdList);
  }

  @Override
  public void
  updateUserInformationAssociatedWithProject(Long projectId,
                                             List<Long> userIdList) {
    if (CollectionUtils.isEmpty(userIdList)) {
      return;
    }
    List<Long> userIds =
        this.userProjectDao.selectUserIdListByProjectId(projectId, 0);
    List<Long> filterUserIdList = userIdList.stream()
                                         .filter(id -> !userIds.contains(id))
                                         .distinct()
                                         .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(filterUserIdList)) {
      return;
    }
    this.saveUserProject(projectId, filterUserIdList);
  }

  @Override
  public void updateOwnerProject(Long projectId, List<Long> ownerIdList) {
    this.deleteOwnerProjectByProjectId(projectId);
    this.saveOwnerProject(projectId, ownerIdList);
  }

  @Override
  public void
  updateOwnerInformationAssociatedWithProject(Long projectId,
                                              List<Long> ownerIdList) {
    if (CollectionUtils.isEmpty(ownerIdList)) {
      return;
    }
    List<Long> userIds =
        this.userProjectDao.selectUserIdListByProjectId(projectId, 0);
    List<Long> filterOwnerIdList = ownerIdList.stream()
                                          .filter(id -> !userIds.contains(id))
                                          .distinct()
                                          .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(filterOwnerIdList)) {
      return;
    }
    this.saveUserProject(projectId, filterOwnerIdList);
  }

  @Override
  public void deleteUserProjectByProjectId(Long projectId) {
    if (projectId == null) {
      return;
    }
    this.userProjectDao.deleteByProjectIdAndUserType(projectId, 0);
  }

  @Override
  public void deleteOwnerProjectByProjectId(Long projectId) {
    if (projectId == null) {
      return;
    }
    this.userProjectDao.deleteByProjectIdAndUserType(projectId, 1);
  }

  private List<UserProject> getUserProjectList(Long projectId,
                                               List<Long> userIdList,
                                               int userType) {
    ArrayList<UserProject> userProjectList = new ArrayList<UserProject>();
    for (Long userId : userIdList) {
      UserProject userProject = new UserProject();
      userProject.setProjectId(projectId);
      userProject.setUserId(userId);
      userProject.setUserType(userType);
      userProjectList.add(userProject);
    }
    return userProjectList;
  }

  @Override
  public List<UserProject>
  lisUserProjectByProjectIds(List<Long> projectIds) {
    return this.userProjectDao.selectByProjectIds(projectIds);
  }

  @Override
  public List<UserProject>
  lisUserProjectByUserProjectDTO(UserProjectDTO userProjectDTO) {
    return this.userProjectDao.select(userProjectDTO);
  }
}
