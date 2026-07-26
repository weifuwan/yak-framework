/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.core.job;

import com.didiglobal.logi.job.common.domain.LogIJob;
import com.didiglobal.logi.job.common.domain.LogITask;
import com.didiglobal.logi.job.core.job.Job;

public interface JobFactory {
    public void addJob(String var1, Job var2);

    public LogIJob newJob(LogITask var1);
}

