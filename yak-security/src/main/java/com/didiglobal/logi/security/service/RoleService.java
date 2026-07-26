/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  javax.servlet.http.HttpServletRequest
 */
package com.didiglobal.logi.security.service;

import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.dto.role.RoleAssignDTO;
import com.didiglobal.logi.security.common.dto.role.RoleQueryDTO;
import com.didiglobal.logi.security.common.dto.role.RoleSaveDTO;
import com.didiglobal.logi.security.common.vo.role.AssignInfoVO;
import com.didiglobal.logi.security.common.vo.role.RoleBriefVO;
import com.didiglobal.logi.security.common.vo.role.RoleDeleteCheckVO;
import com.didiglobal.logi.security.common.vo.role.RoleVO;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface RoleService {
    public RoleVO getRoleDetailByRoleId(Integer var1);

    public RoleBriefVO getRoleBriefByRoleId(Integer var1);

    public PagingData<RoleVO> getRolePage(RoleQueryDTO var1);

    public void createRole(RoleSaveDTO var1, HttpServletRequest var2) throws LogiSecurityException;

    public void deleteRoleByRoleId(Integer var1, HttpServletRequest var2) throws LogiSecurityException;

    public void deleteUserFromRole(Integer var1, Integer var2, HttpServletRequest var3) throws LogiSecurityException;

    public void updateRole(RoleSaveDTO var1, HttpServletRequest var2) throws LogiSecurityException;

    public void assignRoles(RoleAssignDTO var1, HttpServletRequest var2) throws LogiSecurityException;

    public List<AssignInfoVO> getAssignInfoByRoleId(Integer var1);

    public List<RoleBriefVO> getRoleBriefListByRoleName(String var1);

    public RoleDeleteCheckVO checkBeforeDelete(Integer var1);

    public List<RoleBriefVO> getAllRoleBriefList();

    public List<RoleBriefVO> getRoleBriefListByUserId(Integer var1);

    public Map<Integer, List<RoleBriefVO>> getRoleBriefListByUserIds(List<Integer> var1);
}

