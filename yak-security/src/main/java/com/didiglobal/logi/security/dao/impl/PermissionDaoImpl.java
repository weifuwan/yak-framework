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
import com.didiglobal.logi.security.common.entity.Permission;
import com.didiglobal.logi.security.common.po.PermissionPO;
import com.didiglobal.logi.security.dao.PermissionDao;
import com.didiglobal.logi.security.dao.impl.BaseDaoImpl;
import com.didiglobal.logi.security.dao.mapper.PermissionMapper;
import com.didiglobal.logi.security.util.CopyBeanUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PermissionDaoImpl
extends BaseDaoImpl<PermissionPO>
implements PermissionDao {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> selectAllAndAscOrderByLevel() {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.orderByAsc((Object)"level");
        return CopyBeanUtil.copyList(this.permissionMapper.selectList((Wrapper)queryWrapper), Permission.class);
    }

    @Override
    public void insertBatch(List<Permission> permissionList) {
        if (CollectionUtils.isEmpty(permissionList)) {
            return;
        }
        List<PermissionPO> permissionPOList = CopyBeanUtil.copyList(permissionList, PermissionPO.class);
        for (PermissionPO permissionPO : permissionPOList) {
            permissionPO.setAppName(this.logiSecurityProper.getAppName());
            this.permissionMapper.insert(permissionPO);
        }
    }
}

