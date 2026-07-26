/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.dao;

import com.didiglobal.logi.security.common.entity.RolePermission;
import java.util.List;

public interface RolePermissionDao {
    public void insertBatch(List<RolePermission> var1);

    public void deleteByRoleId(Integer var1);

    public List<Integer> selectPermissionIdListByRoleId(Integer var1);

    public List<Integer> selectPermissionIdListByRoleIdList(List<Integer> var1);
}

