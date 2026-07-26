/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.worker;

import com.yak.job.common.Result;
import com.yak.job.common.po.YakWorkerPO;

import java.util.List;
import java.util.Map;

/**
 * 工作节点管理接口。
 */
public interface WorkerManager {
    Result<List<String>> listAllWorkerIps();

    Map<String, YakWorkerPO> mapAllWorkers();
}

