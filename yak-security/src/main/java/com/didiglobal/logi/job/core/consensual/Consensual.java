/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.core.consensual;

import com.didiglobal.logi.job.common.domain.LogITask;

public interface Consensual {
    public String getName();

    public boolean canClaim(LogITask var1);
}

