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

/**
 * 用户服务接口。
 */
public interface UserService {
  Result<Void> check(Integer var1, String var2);

  PagingData<UserVO> getUserPage(UserQueryDTO var1);

  PagingData<UserBriefVO> getUserBriefPage(UserBriefQueryDTO var1);

  UserVO getUserDetailByUserId(Long var1);

  Result<Void> deleteByUserId(Long var1);

  UserBriefVO getUserBriefByUserName(String var1);

  User getUserByUserName(String var1);

  List<UserBriefVO> getUserBriefListByUserIdList(List<Long> var1);

  List<UserBriefVO> getUserBriefListByDeptId(Long var1);

  List<AssignInfoVO> getAssignDataByUserId(Long var1)
      throws YakSecurityException;

  List<UserBriefVO> getUserBriefListByRoleId(Long var1);

  List<UserBriefVO> getUserBriefListByUsernameOrRealName(String var1);

  List<UserBriefVO> getAllUserBriefListOrderByCreateTime(boolean var1);

  List<Long> getUserIdListByUsernameOrRealName(String var1);

  List<UserBriefVO> getAllUserBriefList();

  Result<Void> addUser(UserDTO var1, String var2);

  Result<Void> editUser(UserDTO var1, String var2);

  Result<List<UserVO>> getUserDetailByUserIds(List<Long> var1);

  List<UserBasicVO> getUserBasicListByUserIdList(List<Long> var1);
}
