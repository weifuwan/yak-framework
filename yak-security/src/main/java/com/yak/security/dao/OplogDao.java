/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 */
package com.yak.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yak.security.common.dto.oplog.OplogQueryDTO;
import com.yak.security.common.entity.Oplog;

import java.util.List;

public interface OplogDao {
    public IPage<Oplog> selectPageWithoutDetail(OplogQueryDTO var1);

    public Oplog selectByOplogId(Integer var1);

    public void insert(Oplog var1);

    public List<String> listTargetType();
}

