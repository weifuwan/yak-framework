/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.domain.YakJob;
import com.yak.job.common.domain.YakTask;

public interface JobFactory {
    public void addJob(String var1, Job var2);

    public YakJob newJob(YakTask var1);
}

