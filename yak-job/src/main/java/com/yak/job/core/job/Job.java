/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.TaskResult;

public interface Job {
    public TaskResult execute(JobContext var1) throws Exception;
}

