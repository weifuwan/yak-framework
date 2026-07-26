package io.yak.framework.security.dao;

import io.yak.framework.security.common.dto.user.UserProjectDTO;
import io.yak.framework.security.common.entity.UserProject;
import io.yak.framework.security.common.po.UserProjectPO;
import java.util.List;

public interface UserProjectDao {
  public List<Long> selectUserIdListByProjectId(Long projectId, int userType);

  public List<Long> selectProjectIdListByUserIdList(List<Long> var1);

  public List<UserProjectPO> selectProjectListByUserIdList(List<Long> var1);

  public void insertBatch(List<UserProject> var1);

  public int deleteUserProject(List<UserProject> var1);

  public void deleteByProjectId(Long projectId);

  public void deleteByProjectIdAndUserType(Long projectId, int userType);

  public List<UserProject> selectByProjectIds(List<Long> var1);

  public List<UserProject> select(UserProjectDTO var1);
}
