/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.beat;

/**
 * 工作节点心跳管理接口。
 */
public interface BeatManager {
    boolean beat();

    boolean stop();
}

