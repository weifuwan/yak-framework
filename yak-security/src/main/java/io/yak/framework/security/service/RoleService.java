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

public interface RoleService {
  public RoleVO getRoleDetailByRoleId(Long var1);

  public RoleBriefVO getRoleBriefByRoleId(Long var1);

  public PagingData<RoleVO> getRolePage(RoleQueryDTO var1);

  public void createRole(RoleSaveDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  public void deleteRoleByRoleId(Long var1, HttpServletRequest var2)
      throws YakSecurityException;

  public void deleteUserFromRole(Long var1, Long var2,
                                 HttpServletRequest var3)
      throws YakSecurityException;

  public void updateRole(RoleSaveDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  public void assignRoles(RoleAssignDTO var1, HttpServletRequest var2)
      throws YakSecurityException;

  public List<AssignInfoVO> getAssignInfoByRoleId(Long var1);

  public List<RoleBriefVO> getRoleBriefListByRoleName(String var1);

  public RoleDeleteCheckVO checkBeforeDelete(Long var1);

  public List<RoleBriefVO> getAllRoleBriefList();

  public List<RoleBriefVO> getRoleBriefListByUserId(Long var1);

  public Map<Long, List<RoleBriefVO>>
  getRoleBriefListByUserIds(List<Long> var1);
}
