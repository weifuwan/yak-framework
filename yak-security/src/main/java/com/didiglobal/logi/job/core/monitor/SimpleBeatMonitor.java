/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.didiglobal.logi.job.core.monitor;

import com.didiglobal.logi.job.core.beat.BeatManager;
import com.didiglobal.logi.job.core.monitor.BeatMonitor;
import com.didiglobal.logi.job.core.monitor.SimpleTaskMonitor;
import com.didiglobal.logi.job.utils.ThreadUtil;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleBeatMonitor
implements BeatMonitor {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTaskMonitor.class);
    private BeatManager beatManager;
    private Thread monitorThread;
    public static final long INTERVAL = 10L;

    @Autowired
    public SimpleBeatMonitor(BeatManager beatManager) {
        this.beatManager = beatManager;
    }

    @Override
    public void maintain() {
        this.beatManager.beat();
        this.monitorThread = new Thread((Runnable)new BeatMonitorThread(), "BeatMonitorThread");
        this.monitorThread.start();
    }

    @Override
    public void stop() {
        logger.info("class=SimpleBeatMonitor||method=stop||msg=beat monitor stopByJobCode!!!");
        try {
            this.beatManager.stop();
            if (this.monitorThread != null && this.monitorThread.isAlive()) {
                this.monitorThread.interrupt();
            }
        } catch (Exception e) {
            logger.error("class=SimpleBeatMonitor||method=stop||msg=exception!", (Throwable)e);
        }
    }

    class BeatMonitorThread
    implements Runnable {
        BeatMonitorThread() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        ThreadUtil.sleep(10L, TimeUnit.SECONDS);
                        SimpleBeatMonitor.this.beatManager.beat();
                    }
                } catch (Exception e) {
                    logger.info("class=SimpleBeatMonitor||method=run||msg=exception!", (Throwable)e);
                    continue;
                }
                break;
            }
        }
    }
}

