/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.yak.job.core.monitor;

import com.yak.job.common.domain.YakTask;
import com.yak.job.core.task.TaskManager;
import com.yak.job.utils.ThreadUtil;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleTaskMonitor
implements TaskMonitor {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTaskMonitor.class);
    public static final long SCAN_INTERVAL_SLEEP_SECONDS = 10L;
    public static final long INTERVAL_SECONDS = 1L;
    private TaskManager taskManager;
    private Thread monitorThread;

    @Autowired
    public SimpleTaskMonitor(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void maintain() {
        this.monitorThread = new Thread((Runnable)new TaskMonitorExecutor(), "TaskMonitorExecutor_Thread");
        this.monitorThread.setDaemon(true);
        this.monitorThread.start();
    }

    @Override
    public void stop() {
        logger.info("class=SimpleTaskMonitor||method=stop||msg=task monitor stopByJobCode!");
        try {
            this.taskManager.stopAll();
            if (this.monitorThread != null && this.monitorThread.isAlive()) {
                this.monitorThread.interrupt();
            }
        } catch (Exception e) {
            logger.error("class=SimpleTaskMonitor||method=stop||msg=exception!", (Throwable)e);
        }
    }

    class TaskMonitorExecutor
    implements Runnable {
        TaskMonitorExecutor() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    List<YakTask> yakTaskList;
                    while (true) {
                        logger.info("class=TaskMonitorExecutor||method=run||msg=fetch tasks at regular {}", (Object)10L);
                        yakTaskList = SimpleTaskMonitor.this.taskManager.nextTriggers(1L);
                        if (yakTaskList != null && yakTaskList.size() != 0) break;
                        logger.info("class=TaskMonitorExecutor||method=run||msg=no tasks need run!");
                        ThreadUtil.sleep(1L, TimeUnit.SECONDS);
                    }
                    logger.info("class=TaskMonitorExecutor||method=run||msg=fetch tasks {}", yakTaskList.stream().map(YakTask::getTaskName).collect(Collectors.toList()));
                    Long firstFireTime = ((YakTask)yakTaskList.stream().findFirst().get()).getNextFireTime().getTime();
                    Long nowTime = System.currentTimeMillis();
                    if (nowTime < firstFireTime) {
                        Long between = firstFireTime - nowTime;
                        ThreadUtil.sleep(between + 1L, TimeUnit.MILLISECONDS);
                    }
                    logger.info("class=TaskMonitorExecutor||method=run||msg=start tasks={}, firstFireTime={}, nowTime={}", new Object[]{yakTaskList.stream().map(YakTask::getTaskName).collect(Collectors.toList()), firstFireTime, nowTime});
                    SimpleTaskMonitor.this.taskManager.submit(yakTaskList);
                } catch (Exception e) {
                    logger.error("class=TaskMonitorExecutor||method=run||msg=exception!", (Throwable)e);
                }
                ThreadUtil.sleep(10L, TimeUnit.SECONDS);
            }
        }
    }
}

