/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.domain.LogIJob;
import com.yak.job.common.domain.LogITask;

import java.util.List;
import java.util.concurrent.Future;

public interface JobManager {
    public Future<Object> start(LogITask var1);

    public Integer runningJobSize();

    public boolean stopByJobCode(String var1);

    public boolean stopByTaskCode(String var1);

    public int stopAll();

    public List<LogIJob> getJobs();
}

