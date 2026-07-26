/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.didiglobal.logi.job.core;

import com.didiglobal.logi.job.core.Scheduler;
import com.didiglobal.logi.job.core.monitor.BeatMonitor;
import com.didiglobal.logi.job.core.monitor.MisfireMonitor;
import com.didiglobal.logi.job.core.monitor.TaskMonitor;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleScheduler
implements Scheduler {
    private BeatMonitor beatMonitor;
    private TaskMonitor taskMonitor;
    private MisfireMonitor misfireMonitor;

    @Autowired
    public SimpleScheduler(BeatMonitor beatMonitor, TaskMonitor taskMonitor, MisfireMonitor misfireMonitor) {
        this.beatMonitor = beatMonitor;
        this.taskMonitor = taskMonitor;
        this.misfireMonitor = misfireMonitor;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void startup() {
        this.beatMonitor.maintain();
        this.taskMonitor.maintain();
        this.misfireMonitor.maintain();
    }

    @Override
    public void shutdown() {
        this.beatMonitor.stop();
        this.taskMonitor.stop();
        this.misfireMonitor.stop();
    }
}

