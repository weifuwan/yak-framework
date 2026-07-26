package io.yak.framework.security.dao;

import io.yak.framework.security.common.dto.resource.ControlLevelQueryDTO;
import io.yak.framework.security.common.dto.resource.UserResourceQueryDTO;
import io.yak.framework.security.common.entity.UserResource;
import io.yak.framework.security.common.enums.resource.ControlLevelCode;
import java.util.List;

public interface UserResourceDao {
  public int selectCountByUserId(Long userId, UserResourceQueryDTO queryDTO);

  public void deleteByUserId(Long userId, UserResourceQueryDTO queryDTO);

  public void deleteByControlLevel(ControlLevelCode var1);

  public void insert(UserResource var1);

  public void insertBatch(List<UserResource> var1);

  public void deleteByUserIdList(List<Long> var1, UserResourceQueryDTO var2);

  public void deleteByProjectIdList(List<Long> var1,
                                    UserResourceQueryDTO var2);

  public void deleteByResourceTypeIdList(List<Long> var1,
                                         UserResourceQueryDTO var2);

  public void deleteByResourceIdList(List<Long> var1,
                                     UserResourceQueryDTO var2);

  public int selectCountByUserIdAndControlLevel(Long userId,
                                                ControlLevelCode var2);

  public int selectCount(UserResourceQueryDTO var1);

  public List<Long> selectResourceIdListByUserId(Long userId,
                                                    UserResourceQueryDTO var2);

  public void deleteWithoutUserIdList(UserResourceQueryDTO var1,
                                      List<Long> var2);

  public void deleteByUserIdWithoutProjectIdList(Long userId,
                                                 UserResourceQueryDTO var2,
                                                 List<Long> var3);

  public void deleteByUserIdWithoutResourceTypeIdList(Long userId,
                                                      UserResourceQueryDTO var2,
                                                      List<Long> var3);

  public int selectCountGroupByUserId(UserResourceQueryDTO var1);

  public List<Long> selectUserIdListGroupByUserId(UserResourceQueryDTO var1);

  public Integer selectControlLevel(ControlLevelQueryDTO var1);
}
