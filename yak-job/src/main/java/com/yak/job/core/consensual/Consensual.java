/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.consensual;

import com.yak.job.common.domain.YakTask;

public interface Consensual {
    public String getName();

    public boolean canClaim(YakTask var1);
}

