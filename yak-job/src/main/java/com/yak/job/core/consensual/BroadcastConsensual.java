/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Service
 */
package com.yak.job.core.consensual;

import com.yak.job.common.domain.YakTask;
import org.springframework.stereotype.Service;

@Service
public class BroadcastConsensual
extends AbstractConsensual {
    @Override
    public String getName() {
        return ConsensualEnum.BROADCAST.name();
    }

    @Override
    public boolean tryClaim(YakTask yakTask) {
        return true;
    }
}

