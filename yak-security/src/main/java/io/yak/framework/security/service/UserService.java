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
 *
 * @author weifuwan
 */
public interface UserService {

  /**
   * 校验指定用户字段是否可用。
   */
  Result<Void> check(Integer checkType, String checkValue);

  /**
   * 分页查询用户详细信息。
   */
  PagingData<UserVO> getUserPage(UserQueryDTO queryDTO);

  /**
   * 分页查询用户简要信息。
   */
  PagingData<UserBriefVO> getUserBriefPage(UserBriefQueryDTO queryDTO);

  /**
   * 根据用户 ID 查询用户详情。
   */
  UserVO getUserDetailByUserId(Long userId);

  /**
   * 根据用户 ID 删除用户。
   */
  Result<Void> deleteByUserId(Long userId);

  /**
   * 根据用户名查询用户简要信息。
   */
  UserBriefVO getUserBriefByUsername(String username);

  /**
   * 根据用户名查询用户实体。
   */
  User getUserByUsername(String username);

  /**
   * 根据用户 ID 集合查询用户简要信息。
   */
  List<UserBriefVO> getUserBriefListByUserIds(List<Long> userIds);

  /**
   * 根据部门 ID 查询用户简要信息。
   */
  List<UserBriefVO> getUserBriefListByDeptId(Long deptId);

  /**
   * 根据用户 ID 查询角色分配信息。
   */
  List<AssignInfoVO> getAssignInfoListByUserId(Long userId)
          throws YakSecurityException;

  /**
   * 根据角色 ID 查询用户简要信息。
   */
  List<UserBriefVO> getUserBriefListByRoleId(Long roleId);

  /**
   * 根据用户名或真实姓名模糊查询用户。
   */
  List<UserBriefVO> searchUserBriefList(String keyword);

  /**
   * 查询全部用户简要信息并按创建时间排序。
   */
  List<UserBriefVO> getAllUserBriefListOrderByCreateTime(
          boolean ascending);

  /**
   * 根据用户名或真实姓名查询用户 ID 集合。
   */
  List<Long> searchUserIds(String keyword);

  /**
   * 查询全部用户简要信息。
   */
  List<UserBriefVO> getAllUserBriefList();

  /**
   * 新增用户。
   */
  Result<Void> addUser(UserDTO userDTO, String operator);

  /**
   * 编辑用户。
   */
  Result<Void> editUser(UserDTO userDTO, String operator);

  /**
   * 根据用户 ID 集合批量查询用户详情。
   */
  Result<List<UserVO>> getUserDetailsByUserIds(List<Long> userIds);

  /**
   * 根据用户 ID 集合查询用户基础信息。
   */
  List<UserBasicVO> getUserBasicListByUserIds(List<Long> userIds);
}