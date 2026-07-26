/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Service
 */
package com.yak.job.core.monitor;

import org.springframework.stereotype.Service;

@Service
public class SimpleMisfireMonitor
implements MisfireMonitor {
    @Override
    public void maintain() {
    }

    @Override
    public void stop() {
    }

    class MisfireMonitorThread
    implements Runnable {
        MisfireMonitorThread() {
        }

        @Override
        public void run() {
        }
    }
}

