/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.service;

import com.yak.security.common.enums.oplog.OplogCode;

import java.util.List;

public interface OplogExtraService {
    public List<String> getOplogExtraNameListByType(Integer var1);

    public void saveOplogExtraList(List<String> var1, OplogCode var2);
}

