/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.util.CollectionUtils
 */
package com.didiglobal.logi.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.didiglobal.logi.security.common.entity.RolePermission;
import com.didiglobal.logi.security.common.po.RolePermissionPO;
import com.didiglobal.logi.security.dao.RolePermissionDao;
import com.didiglobal.logi.security.dao.impl.BaseDaoImpl;
import com.didiglobal.logi.security.dao.mapper.RolePermissionMapper;
import com.didiglobal.logi.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RolePermissionDaoImpl
extends BaseDaoImpl<RolePermissionPO>
implements RolePermissionDao {
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public void insertBatch(List<RolePermission> rolePermissionList) {
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            return;
        }
        List<RolePermissionPO> rolePermissionPOList = CopyBeanUtil.copyList(rolePermissionList, RolePermissionPO.class);
        for (RolePermissionPO rolePermissionPO : rolePermissionPOList) {
            rolePermissionPO.setAppName(this.logiSecurityProper.getAppName());
            this.rolePermissionMapper.insert(rolePermissionPO);
        }
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        if (roleId == null) {
            return;
        }
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.eq((Object)"role_id", (Object)roleId);
        this.rolePermissionMapper.delete((Wrapper)queryWrapper);
    }

    @Override
    public List<Integer> selectPermissionIdListByRoleId(Integer roleId) {
        if (roleId == null) {
            return new ArrayList<Integer>();
        }
        ArrayList<Integer> roleIdList = new ArrayList<Integer>();
        roleIdList.add(roleId);
        return this.selectPermissionIdListByRoleIdList(roleIdList);
    }

    @Override
    public List<Integer> selectPermissionIdListByRoleIdList(List<Integer> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return new ArrayList<Integer>();
        }
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.select(new String[]{"permission_id"}).in((Object)"role_id", roleIdList);
        List permissionIdList = this.rolePermissionMapper.selectObjs((Wrapper)queryWrapper);
        return permissionIdList.stream().map(Integer.class::cast).collect(Collectors.toList());
    }
}

