package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.role.RoleAssignDTO;
import io.yak.framework.security.common.dto.role.RoleQueryDTO;
import io.yak.framework.security.common.dto.role.RoleSaveDTO;
import io.yak.framework.security.common.vo.role.AssignInfoVO;
import io.yak.framework.security.common.vo.role.RoleBriefVO;
import io.yak.framework.security.common.vo.role.RoleDeleteCheckVO;
import io.yak.framework.security.common.vo.role.RoleVO;
import io.yak.framework.security.exception.YakSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 角色服务接口。
 */
public interface RoleService {
  /**
   * 根据角色 ID 查询角色详情。
   */
  RoleVO getRoleDetailByRoleId(Long var1);

  /**
   * 根据角色 ID 查询角色简要信息。
   */
  RoleBriefVO getRoleBriefByRoleId(Long var1);

  /**
   * 分页查询角色。
   */
  PagingData<RoleVO> getRolePage(RoleQueryDTO var1);

  /**
   * 创建角色。
   */
  void createRole(RoleSaveDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  /**
   * 根据角色 ID 删除角色。
   */
  void deleteRoleByRoleId(Long var1, HttpServletRequest var2)
      throws YakSecurityException;

  /**
   * 从角色中删除用户。
   */
  void deleteUserFromRole(Long var1, Long var2,
                                 HttpServletRequest var3)
      throws YakSecurityException;

  /**
   * 更新角色。
   */
  void updateRole(RoleSaveDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  /**
   * 分配角色。
   */
  void assignRoles(RoleAssignDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  /**
   * 根据角色 ID 查询分配信息。
   */
  List<AssignInfoVO> getAssignInfoByRoleId(Long var1);

  /**
   * 根据角色名称查询角色简要信息。
   */
  List<RoleBriefVO> getRoleBriefListByRoleName(String var1);

  /**
   * 执行删除前校验。
   */
  RoleDeleteCheckVO checkBeforeDelete(Long var1);

  /**
   * 查询全部角色简要信息。
   */
  List<RoleBriefVO> getAllRoleBriefList();

  /**
   * 根据用户 ID 查询角色简要信息。
   */
  List<RoleBriefVO> getRoleBriefListByUserId(Long var1);

  /**
   * 根据用户 ID 集合查询角色简要信息。
   */
  Map<Long, List<RoleBriefVO>>
  getRoleBriefListByUserIds(List<Long> var1);
}
