/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.dao;

import com.yak.security.common.entity.OplogExtra;

import java.util.List;

public interface OplogExtraDao {
    public List<OplogExtra> selectListByType(Integer var1);

    public void insertBatch(List<OplogExtra> var1);
}

