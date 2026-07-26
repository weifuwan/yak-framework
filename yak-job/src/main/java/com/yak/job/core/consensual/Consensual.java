/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.consensual;

import com.yak.job.common.domain.YakTask;

/**
 * 任务执行共识策略接口。
 */
public interface Consensual {
    String getName();

    boolean canClaim(YakTask var1);
}

