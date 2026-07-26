/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.didiglobal.logi.job.core.consensual;

import com.didiglobal.logi.job.common.domain.LogITask;
import com.didiglobal.logi.job.core.consensual.AbstractConsensual;
import com.didiglobal.logi.job.core.consensual.ConsensualEnum;
import com.didiglobal.logi.job.core.task.TaskLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RandomConsensual
extends AbstractConsensual {
    private static final Logger logger = LoggerFactory.getLogger(RandomConsensual.class);
    @Autowired
    private TaskLockService taskLockService;

    @Override
    public String getName() {
        return ConsensualEnum.RANDOM.name();
    }

    @Override
    public boolean tryClaim(LogITask logITask) {
        if (this.taskLockService.tryAcquire(logITask.getTaskCode()).booleanValue()) {
            logITask.setTaskCallback(taskCode -> {
                logger.info("class=RandomConsensual||method=tryClaim||msg=release task lock taskCode {}", (Object)taskCode);
                this.taskLockService.tryRelease(taskCode);
            });
            return true;
        }
        return false;
    }
}

