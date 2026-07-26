package io.yak.framework.security.dao;

import io.yak.framework.security.common.dto.user.UserProjectDTO;
import io.yak.framework.security.common.entity.UserProject;
import io.yak.framework.security.common.po.UserProjectPO;
import java.util.List;

public interface UserProjectDao {
  public List<Long> selectUserIdListByProjectId(Integer var1, int var2);

  public List<Long> selectProjectIdListByUserIdList(List<Long> var1);

  public List<UserProjectPO> selectProjectListByUserIdList(List<Long> var1);

  public void insertBatch(List<UserProject> var1);

  public int deleteUserProject(List<UserProject> var1);

  public void deleteByProjectId(Integer var1);

  public void deleteByProjectIdAndUserType(Integer var1, int var2);

  public List<UserProject> selectByProjectIds(List<Long> var1);

  public List<UserProject> select(UserProjectDTO var1);
}
