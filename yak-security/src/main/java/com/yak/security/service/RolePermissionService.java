/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.service;

import java.util.List;

public interface RolePermissionService {
    public void saveRolePermission(Integer var1, List<Integer> var2);

    public void updateRolePermission(Integer var1, List<Integer> var2);

    public void deleteRolePermissionByRoleId(Integer var1);

    public List<Integer> getPermissionIdListByRoleId(Integer var1);

    public List<Integer> getPermissionIdListByRoleIdList(List<Integer> var1);
}

