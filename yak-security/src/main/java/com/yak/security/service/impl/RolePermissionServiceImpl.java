/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 */
package com.yak.security.service.impl;

import com.yak.security.common.entity.RolePermission;
import com.yak.security.dao.RolePermissionDao;
import com.yak.security.service.RolePermissionService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value="logiSecurityRolePermissionServiceImpl")
public class RolePermissionServiceImpl
implements RolePermissionService {
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public void saveRolePermission(Integer roleId, List<Integer> permissionIdList) {
        if (roleId == null || CollectionUtils.isEmpty(permissionIdList)) {
            return;
        }
        this.rolePermissionDao.insertBatch(this.getRolePermissionList(roleId, permissionIdList));
    }

    @Override
    public void updateRolePermission(Integer roleId, List<Integer> permissionIdList) {
        this.deleteRolePermissionByRoleId(roleId);
        this.saveRolePermission(roleId, permissionIdList);
    }

    @Override
    public void deleteRolePermissionByRoleId(Integer roleId) {
        if (roleId == null) {
            return;
        }
        this.rolePermissionDao.deleteByRoleId(roleId);
    }

    @Override
    public List<Integer> getPermissionIdListByRoleId(Integer roleId) {
        if (roleId == null) {
            return new ArrayList<Integer>();
        }
        return this.rolePermissionDao.selectPermissionIdListByRoleId(roleId);
    }

    @Override
    public List<Integer> getPermissionIdListByRoleIdList(List<Integer> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return new ArrayList<Integer>();
        }
        return this.rolePermissionDao.selectPermissionIdListByRoleIdList(roleIdList);
    }

    private List<RolePermission> getRolePermissionList(Integer roleId, List<Integer> permissionIdList) {
        ArrayList<RolePermission> rolePermissionList = new ArrayList<RolePermission>();
        for (Integer permissionId : permissionIdList) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        return rolePermissionList;
    }
}

