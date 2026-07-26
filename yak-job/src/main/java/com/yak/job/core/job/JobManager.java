/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.domain.LogIJob;
import com.yak.job.common.domain.LogITask;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 作业管理接口。
 */
public interface JobManager {
    Future<Object> start(LogITask var1);

    Integer runningJobSize();

    boolean stopByJobCode(String var1);

    boolean stopByTaskCode(String var1);

    int stopAll();

    List<LogIJob> getJobs();
}

