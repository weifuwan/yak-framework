/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.monitor;

/**
 * 监控器生命周期接口。
 */
public interface Monitor {
    void maintain();

    void stop();
}

