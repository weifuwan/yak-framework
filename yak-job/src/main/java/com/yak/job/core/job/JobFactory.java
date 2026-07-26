/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.domain.LogIJob;
import com.yak.job.common.domain.LogITask;

public interface JobFactory {
    public void addJob(String var1, Job var2);

    public LogIJob newJob(LogITask var1);
}

