/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core;

/**
 * 任务调度接口。
 */
public interface Scheduler {
    void initialize();

    void startup();

    void shutdown();
}

