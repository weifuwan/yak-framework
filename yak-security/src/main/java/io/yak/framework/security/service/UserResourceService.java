package io.yak.framework.security.service;

import io.yak.framework.common.PagingData;
import io.yak.framework.security.common.dto.resource.AssignToManyUserDTO;
import io.yak.framework.security.common.dto.resource.AssignToOneUserDTO;
import io.yak.framework.security.common.dto.resource.BatchAssignDTO;
import io.yak.framework.security.common.dto.resource.ControlLevelQueryDTO;
import io.yak.framework.security.common.dto.resource.MByRDataQueryDTO;
import io.yak.framework.security.common.dto.resource.MByRQueryDTO;
import io.yak.framework.security.common.dto.resource.MByUDataQueryDTO;
import io.yak.framework.security.common.dto.resource.MByUQueryDTO;
import io.yak.framework.security.common.dto.resource.UserResourceQueryDTO;
import io.yak.framework.security.common.enums.resource.ControlLevelCode;
import io.yak.framework.security.common.vo.resource.MByRDataVO;
import io.yak.framework.security.common.vo.resource.MByRVO;
import io.yak.framework.security.common.vo.resource.MByUDataVO;
import io.yak.framework.security.common.vo.resource.MByUVO;
import io.yak.framework.security.exception.YakSecurityException;

import java.util.List;

/**
 * 用户资源权限服务接口。
 *
 * @author weifuwan
 */
public interface UserResourceService {

  /**
   * 根据用户 ID 统计符合条件的资源数量。
   *
   * @param userId 用户 ID
   * @param queryDTO 资源查询条件
   * @return 资源数量
   */
  int getResourceCntByUserId(
          Long userId,
          UserResourceQueryDTO queryDTO);

  /**
   * 分页查询按资源管理的权限信息。
   *
   * @param queryDTO 查询条件
   * @return 按资源管理的权限分页数据
   * @throws YakSecurityException 查询参数异常
   */
  PagingData<MByRVO> getManageByResourcePage(
          MByRQueryDTO queryDTO)
          throws YakSecurityException;

  /**
   * 分页查询按用户管理的权限信息。
   *
   * @param queryDTO 查询条件
   * @return 按用户管理的权限分页数据
   */
  PagingData<MByUVO> getManageByUserPage(
          MByUQueryDTO queryDTO);

  /**
   * 为单个用户分配资源权限。
   *
   * @param assignDTO 分配参数
   * @throws YakSecurityException 分配参数异常
   */
  void assignResourcePermission(
          AssignToOneUserDTO assignDTO)
          throws YakSecurityException;

  /**
   * 为多个用户分配资源权限。
   *
   * @param assignDTO 分配参数
   * @throws YakSecurityException 分配参数异常
   */
  void assignResourcePermission(
          AssignToManyUserDTO assignDTO)
          throws YakSecurityException;

  /**
   * 批量分配资源权限。
   *
   * @param assignDTO 批量分配参数
   * @throws YakSecurityException 分配参数异常
   */
  void batchAssignResourcePermission(
          BatchAssignDTO assignDTO)
          throws YakSecurityException;

  /**
   * 查询按用户管理的资源权限数据。
   *
   * @param queryDTO 查询条件
   * @return 资源权限数据列表
   * @throws YakSecurityException 查询参数异常
   */
  List<MByUDataVO> getManagerByUserDataList(
          MByUDataQueryDTO queryDTO)
          throws YakSecurityException;

  /**
   * 查询按资源管理的用户权限数据。
   *
   * @param queryDTO 查询条件
   * @return 用户权限数据列表
   * @throws YakSecurityException 查询参数异常
   */
  List<MByRDataVO> getManagerByResourceDataList(
          MByRDataQueryDTO queryDTO)
          throws YakSecurityException;

  /**
   * 获取资源查看权限控制状态。
   *
   * @return 是否开启查看权限控制
   */
  boolean getViewPermissionControlStatus();

  /**
   * 切换资源查看权限控制状态。
   */
  void changeResourceViewControlStatus();

  /**
   * 查询指定用户对资源的权限控制级别。
   *
   * @param queryDTO 查询条件
   * @return 权限控制级别
   * @throws YakSecurityException 查询参数异常
   */
  ControlLevelCode getControlLevel(
          ControlLevelQueryDTO queryDTO)
          throws YakSecurityException;
}