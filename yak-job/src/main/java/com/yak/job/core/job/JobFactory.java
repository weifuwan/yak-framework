/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.domain.YakJob;
import com.yak.job.common.domain.YakTask;

/**
 * 作业实例工厂接口。
 */
public interface JobFactory {
    void addJob(String var1, Job var2);

    YakJob newJob(YakTask var1);
}

