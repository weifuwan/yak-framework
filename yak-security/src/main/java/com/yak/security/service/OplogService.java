/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.service;

import com.yak.security.common.PagingData;
import com.yak.security.common.dto.oplog.OplogDTO;
import com.yak.security.common.dto.oplog.OplogQueryDTO;
import com.yak.security.common.vo.oplog.OplogVO;

import java.util.List;

public interface OplogService {
    public Integer saveOplog(OplogDTO var1);

    public PagingData<OplogVO> getOplogPage(OplogQueryDTO var1);

    public OplogVO getOplogDetailByOplogId(Integer var1);

    public List<String> listTargetType();
}

