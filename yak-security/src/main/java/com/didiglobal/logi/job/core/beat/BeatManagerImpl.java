/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 */
package com.didiglobal.logi.job.core.beat;

import com.didiglobal.logi.job.LogIJobProperties;
import com.didiglobal.logi.job.common.domain.LogITask;
import com.didiglobal.logi.job.common.domain.LogIWorker;
import com.didiglobal.logi.job.common.po.LogITaskPO;
import com.didiglobal.logi.job.common.po.LogIWorkerPO;
import com.didiglobal.logi.job.core.WorkerSingleton;
import com.didiglobal.logi.job.core.beat.BeatManager;
import com.didiglobal.logi.job.core.job.JobManager;
import com.didiglobal.logi.job.mapper.LogITaskLockMapper;
import com.didiglobal.logi.job.mapper.LogITaskMapper;
import com.didiglobal.logi.job.mapper.LogIWorkerMapper;
import com.didiglobal.logi.job.utils.BeanUtil;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class BeatManagerImpl
implements BeatManager {
    private static final Logger logger = LoggerFactory.getLogger(BeatManagerImpl.class);
    private JobManager jobManager;
    private LogIWorkerMapper logIWorkerMapper;
    private LogITaskLockMapper logITaskLockMapper;
    private LogITaskMapper logITaskMapper;
    private LogIJobProperties logIJobProperties;

    @Autowired
    public BeatManagerImpl(JobManager jobManager, LogIWorkerMapper logIWorkerMapper, LogITaskLockMapper logITaskLockMapper, LogITaskMapper logITaskMapper, LogIJobProperties logIJobProperties) {
        this.jobManager = jobManager;
        this.logIWorkerMapper = logIWorkerMapper;
        this.logITaskLockMapper = logITaskLockMapper;
        this.logITaskMapper = logITaskMapper;
        this.logIJobProperties = logIJobProperties;
    }

    @Override
    public boolean beat() {
        logger.info("class=BeatManagerImpl||method=beat||msg=beat beat!!!");
        this.cleanWorker();
        WorkerSingleton workerSingleton = WorkerSingleton.getInstance();
        workerSingleton.updateInstanceMetrics();
        LogIWorker logIWorker = workerSingleton.getLogIWorker();
        logIWorker.setJobNum(this.jobManager.runningJobSize());
        logIWorker.setAppName(this.logIJobProperties.getAppName());
        int ret = null == this.logIWorkerMapper.selectByCode(logIWorker.getWorkerCode(), logIWorker.getAppName()) ? this.logIWorkerMapper.insert(logIWorker.getWorker()) : this.logIWorkerMapper.updateByCode(logIWorker.getWorker());
        return ret > 0;
    }

    @Override
    public boolean stop() {
        WorkerSingleton workerSingleton = WorkerSingleton.getInstance();
        LogIWorker logIWorker = workerSingleton.getLogIWorker();
        this.logIWorkerMapper.deleteByCode(logIWorker.getWorkerCode());
        this.logITaskLockMapper.deleteByWorkerCodeAndAppName(logIWorker.getWorkerCode(), this.logIJobProperties.getAppName());
        return true;
    }

    private void cleanTask(String appName, String workCode) {
        List<LogITaskPO> logITaskPOS = this.logITaskMapper.selectByAppName(appName);
        if (!CollectionUtils.isEmpty(logITaskPOS)) {
            for (LogITaskPO logITaskPO : logITaskPOS) {
                try {
                    List<LogITask.TaskWorker> taskWorkers = BeanUtil.convertToList(logITaskPO.getTaskWorkerStr(), LogITask.TaskWorker.class);
                    if (CollectionUtils.isEmpty(taskWorkers)) continue;
                    boolean needUpdate = false;
                    Iterator<LogITask.TaskWorker> iter = taskWorkers.iterator();
                    while (iter.hasNext()) {
                        LogITask.TaskWorker taskWorker = iter.next();
                        if (!workCode.equals(taskWorker.getWorkerCode())) continue;
                        iter.remove();
                        needUpdate = true;
                    }
                    if (!needUpdate) continue;
                    logITaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
                    this.logITaskMapper.updateTaskWorkStrByCode(logITaskPO);
                } catch (Exception e) {
                    logger.info("class=BeatManagerImpl||method=cleanTask||msg=clean task worker error!", (Throwable)e);
                }
            }
        }
    }

    private void cleanWorker() {
        long currentTime = System.currentTimeMillis();
        String appName = this.logIJobProperties.getAppName();
        List<LogIWorkerPO> logIWorkerPOS = this.logIWorkerMapper.selectByAppName(appName);
        if (CollectionUtils.isEmpty(logIWorkerPOS)) {
            return;
        }
        for (LogIWorkerPO logIWorkerPO : logIWorkerPOS) {
            if (logIWorkerPO.getHeartbeat().getTime() + 30000L >= currentTime) continue;
            this.logIWorkerMapper.deleteByCode(logIWorkerPO.getWorkerCode());
            this.logITaskLockMapper.deleteByWorkerCodeAndAppName(logIWorkerPO.getWorkerCode(), appName);
            this.cleanTask(appName, logIWorkerPO.getWorkerCode());
        }
    }
}

