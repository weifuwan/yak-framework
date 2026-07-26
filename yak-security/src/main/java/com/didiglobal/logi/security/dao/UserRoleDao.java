/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.dao;

import com.didiglobal.logi.security.common.entity.UserRole;
import com.didiglobal.logi.security.common.po.UserRolePO;
import java.util.List;

public interface UserRoleDao {
    public List<Integer> selectUserIdListByRoleId(Integer var1);

    public List<Integer> selectRoleIdListByUserId(Integer var1);

    public void insertBatch(List<UserRole> var1);

    public int deleteByUserIdOrRoleId(Integer var1, Integer var2);

    public int selectCountByRoleId(Integer var1);

    public List<UserRolePO> selectByRoleIds(List<Integer> var1);

    public List<UserRolePO> getRoleIdListByUserIds(List<Integer> var1);
}

