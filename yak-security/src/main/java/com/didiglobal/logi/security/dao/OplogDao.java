/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 */
package com.didiglobal.logi.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.didiglobal.logi.security.common.dto.oplog.OplogQueryDTO;
import com.didiglobal.logi.security.common.entity.Oplog;
import java.util.List;

public interface OplogDao {
    public IPage<Oplog> selectPageWithoutDetail(OplogQueryDTO var1);

    public Oplog selectByOplogId(Integer var1);

    public void insert(Oplog var1);

    public List<String> listTargetType();
}

