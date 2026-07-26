/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.core.worker;

import com.didiglobal.logi.job.common.Result;
import com.didiglobal.logi.job.common.po.LogIWorkerPO;
import java.util.List;
import java.util.Map;

public interface WorkerManager {
    public Result<List<String>> listAllWorkerIps();

    public Map<String, LogIWorkerPO> mapAllWorkers();
}

