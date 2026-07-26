/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Service
 */
package com.didiglobal.logi.job.core.consensual;

import com.didiglobal.logi.job.common.domain.LogITask;
import com.didiglobal.logi.job.core.consensual.AbstractConsensual;
import com.didiglobal.logi.job.core.consensual.ConsensualEnum;
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

