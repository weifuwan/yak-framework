/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.dao;

import com.yak.security.common.entity.Permission;

import java.util.List;

public interface PermissionDao {
    public List<Permission> selectAllAndAscOrderByLevel();

    public void insertBatch(List<Permission> var1);
}

