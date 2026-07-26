package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.common.dto.user.UserProjectDTO;
import io.yak.framework.security.common.entity.UserProject;
import io.yak.framework.security.common.po.UserProjectPO;
import io.yak.framework.security.dao.UserProjectDao;
import io.yak.framework.security.dao.impl.BaseDaoImpl;
import io.yak.framework.security.dao.mapper.UserProjectMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserProjectDaoImpl
    extends BaseDaoImpl<UserProjectPO> implements UserProjectDao {
  @Autowired private UserProjectMapper userProjectMapper;

  @Override
  public List<Long> selectUserIdListByProjectId(Long projectId,
                                                   int type) {
    if (projectId == null) {
      return new ArrayList<Long>();
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"user_id"});
    queryWrapper.eq((Object) "project_id", (Object)projectId);
    queryWrapper.eq((Object) "user_type", (Object)type);
    List userIdList = this.userProjectMapper.selectObjs((Wrapper)queryWrapper);
    return userIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserProject> selectByProjectIds(List<Long> projectIds) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"user_id", "project_id", "user_type"});
    return CopyBeanUtil.copyList(
        this.userProjectMapper.selectList((Wrapper)queryWrapper),
        UserProject.class);
  }

  @Override
  public List<Long>
  selectProjectIdListByUserIdList(List<Long> userIdList) {
    if (CollectionUtils.isEmpty(userIdList)) {
      return new ArrayList<Long>();
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(new String[] {"project_id"})
        .in((Object) "user_id", userIdList);
    List projectIdList =
        this.userProjectMapper.selectObjs((Wrapper)queryWrapper);
    return projectIdList.stream()
        .map(Integer.class ::cast)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserProjectPO>
  selectProjectListByUserIdList(List<Long> userIdList) {
    if (CollectionUtils.isEmpty(userIdList)) {
      return Collections.emptyList();
    }
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.in((Object) "user_id", userIdList);
    return this.userProjectMapper.selectList((Wrapper)queryWrapper);
  }

  @Override
  public void insertBatch(List<UserProject> userProjectList) {
    if (!CollectionUtils.isEmpty(userProjectList)) {
      for (UserProject project : userProjectList) {
        UserProjectPO userProjectPO = this.getByProjectAndUserId(project);
        if (null == userProjectPO) {
          this.addUserProject(project);
          continue;
        }
        this.updateUserProject(userProjectPO.getId(), project);
      }
    }
  }

  @Override
  public int deleteUserProject(List<UserProject> userProjectList) {
    int delNu = 0;
    if (!CollectionUtils.isEmpty(userProjectList)) {
      for (UserProject userProject : userProjectList) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.eq((Object) "project_id",
                        (Object)userProject.getProjectId());
        queryWrapper.eq((Object) "user_id", (Object)userProject.getUserId());
        queryWrapper.eq(Objects.nonNull(userProject.getUserType()),
                        (Object) "user_type",
                        (Object)userProject.getUserType());
        delNu += this.userProjectMapper.delete((Wrapper)queryWrapper);
      }
    }
    return delNu;
  }

  @Override
  public void deleteByProjectId(Long projectId) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "project_id", (Object)projectId);
    this.userProjectMapper.delete((Wrapper)queryWrapper);
  }

  @Override
  public void deleteByProjectIdAndUserType(Long projectId, int userType) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "project_id", (Object)projectId);
    queryWrapper.eq((Object) "user_type", (Object)userType);
    this.userProjectMapper.delete((Wrapper)queryWrapper);
  }

  private int addUserProject(UserProject userProject) {
    UserProjectPO userProjectPO =
        CopyBeanUtil.copy(userProject, UserProjectPO.class);
    userProjectPO.setAppName(this.yakSecurityProperties.getApplicationName());
    return this.userProjectMapper.insert(userProjectPO);
  }

  private UserProjectPO getByProjectAndUserId(UserProject userProject) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.eq((Object) "project_id", (Object)userProject.getProjectId());
    queryWrapper.eq((Object) "user_id", (Object)userProject.getUserId());
    queryWrapper.eq(Objects.nonNull(userProject.getUserType()),
                    (Object) "user_type", (Object)userProject.getUserType());
    return (UserProjectPO)this.userProjectMapper.selectOne(
        (Wrapper)queryWrapper);
  }

  private int updateUserProject(int id, UserProject userProject) {
    UserProjectPO userProjectPO =
        CopyBeanUtil.copy(userProject, UserProjectPO.class);
    userProjectPO.setId(id);
    return this.userProjectMapper.updateById(userProjectPO);
  }

  @Override
  public List<UserProject> select(UserProjectDTO userProjectDTO) {
    QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
    queryWrapper.select(
        new String[] {"id", "project_id", "user_id", "user_type"});
    if (Objects.nonNull(userProjectDTO)) {
      ((QueryWrapper)((QueryWrapper)((QueryWrapper)((QueryWrapper)queryWrapper
                                                        .eq(Objects.nonNull(
                                                                userProjectDTO
                                                                    .getId()),
                                                            (Object) "id",
                                                            (Object)
                                                                userProjectDTO
                                                                    .getId()))
                                         .eq(Objects.nonNull(
                                                 userProjectDTO.getProjectId()),
                                             (Object) "project_id",
                                             (Object)
                                                 userProjectDTO.getProjectId()))
                          .eq(Objects.nonNull(userProjectDTO.getUserId()),
                              (Object) "user_id",
                              (Object)userProjectDTO.getUserId()))
           .eq(Objects.nonNull(userProjectDTO.getUserType()),
               (Object) "user_type", (Object)userProjectDTO.getUserType()))
          .eq(Objects.nonNull(userProjectDTO.getIsDelete()),
              (Object) "is_delete", (Object)userProjectDTO.getIsDelete());
    }
    return CopyBeanUtil.copyList(
        this.userProjectMapper.selectList((Wrapper)queryWrapper),
        UserProject.class);
  }
}
