/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.TaskResult;

/**
 * 可执行作业接口。
 */
public interface Job {
    TaskResult execute(JobContext var1) throws Exception;
}

