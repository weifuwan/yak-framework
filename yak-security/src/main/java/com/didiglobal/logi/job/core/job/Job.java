/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.core.job;

import com.didiglobal.logi.job.common.TaskResult;
import com.didiglobal.logi.job.core.job.JobContext;

public interface Job {
    public TaskResult execute(JobContext var1) throws Exception;
}

