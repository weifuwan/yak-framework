/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Service
 */
package com.yak.job.core.consensual;

import com.yak.job.common.domain.LogITask;
import org.springframework.stereotype.Service;

@Service
public class BroadcastConsensual
extends AbstractConsensual {
    @Override
    public String getName() {
        return ConsensualEnum.BROADCAST.name();
    }

    @Override
    public boolean tryClaim(LogITask logITask) {
        return true;
    }
}

