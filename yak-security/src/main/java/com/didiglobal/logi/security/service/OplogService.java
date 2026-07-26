/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.service;

import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.dto.oplog.OplogDTO;
import com.didiglobal.logi.security.common.dto.oplog.OplogQueryDTO;
import com.didiglobal.logi.security.common.vo.oplog.OplogVO;
import java.util.List;

public interface OplogService {
    public Integer saveOplog(OplogDTO var1);

    public PagingData<OplogVO> getOplogPage(OplogQueryDTO var1);

    public OplogVO getOplogDetailByOplogId(Integer var1);

    public List<String> listTargetType();
}

