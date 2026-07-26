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
  RoleVO getRoleDetailByRoleId(Long var1);

  RoleBriefVO getRoleBriefByRoleId(Long var1);

  PagingData<RoleVO> getRolePage(RoleQueryDTO var1);

  void createRole(RoleSaveDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  void deleteRoleByRoleId(Long var1, HttpServletRequest var2)
      throws YakSecurityException;

  void deleteUserFromRole(Long var1, Long var2,
                                 HttpServletRequest var3)
      throws YakSecurityException;

  void updateRole(RoleSaveDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  void assignRoles(RoleAssignDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  List<AssignInfoVO> getAssignInfoByRoleId(Long var1);

  List<RoleBriefVO> getRoleBriefListByRoleName(String var1);

  RoleDeleteCheckVO checkBeforeDelete(Long var1);

  List<RoleBriefVO> getAllRoleBriefList();

  List<RoleBriefVO> getRoleBriefListByUserId(Long var1);

  Map<Long, List<RoleBriefVO>>
  getRoleBriefListByUserIds(List<Long> var1);
}
