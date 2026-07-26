package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.user.UserBriefQueryDTO;
import io.yak.framework.security.common.dto.user.UserDTO;
import io.yak.framework.security.common.dto.user.UserQueryDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.vo.role.AssignInfoVO;
import io.yak.framework.security.common.vo.user.UserBasicVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.common.vo.user.UserVO;
import io.yak.framework.security.exception.YakSecurityException;
import java.util.List;

public interface UserService {
  public Result<Void> check(Integer var1, String var2);

  public PagingData<UserVO> getUserPage(UserQueryDTO var1);

  public PagingData<UserBriefVO> getUserBriefPage(UserBriefQueryDTO var1);

  public UserVO getUserDetailByUserId(Long var1);

  public Result<Void> deleteByUserId(Long var1);

  public UserBriefVO getUserBriefByUserName(String var1);

  public User getUserByUserName(String var1);

  public List<UserBriefVO> getUserBriefListByUserIdList(List<Long> var1);

  public List<UserBriefVO> getUserBriefListByDeptId(Long var1);

  public List<AssignInfoVO> getAssignDataByUserId(Long var1)
      throws YakSecurityException;

  public List<UserBriefVO> getUserBriefListByRoleId(Long var1);

  public List<UserBriefVO> getUserBriefListByUsernameOrRealName(String var1);

  public List<UserBriefVO> getAllUserBriefListOrderByCreateTime(boolean var1);

  public List<Long> getUserIdListByUsernameOrRealName(String var1);

  public List<UserBriefVO> getAllUserBriefList();

  public Result<Void> addUser(UserDTO var1, String var2);

  public Result<Void> editUser(UserDTO var1, String var2);

  public Result<List<UserVO>> getUserDetailByUserIds(List<Long> var1);

  public List<UserBasicVO> getUserBasicListByUserIdList(List<Long> var1);
}
