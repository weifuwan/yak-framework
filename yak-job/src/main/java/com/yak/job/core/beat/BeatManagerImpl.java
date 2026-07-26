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
package com.yak.job.core.beat;

import com.yak.job.YakJobProperties;
import com.yak.job.common.domain.YakTask;
import com.yak.job.common.domain.YakWorker;
import com.yak.job.common.po.YakTaskPO;
import com.yak.job.common.po.YakWorkerPO;
import com.yak.job.core.WorkerSingleton;
import com.yak.job.core.job.JobManager;
import com.yak.job.mapper.YakTaskLockMapper;
import com.yak.job.mapper.YakTaskMapper;
import com.yak.job.mapper.YakWorkerMapper;
import com.yak.job.utils.BeanUtil;
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
    private YakWorkerMapper yakWorkerMapper;
    private YakTaskLockMapper yakTaskLockMapper;
    private YakTaskMapper yakTaskMapper;
    private YakJobProperties yakJobProperties;

    @Autowired
    public BeatManagerImpl(JobManager jobManager, YakWorkerMapper yakWorkerMapper, YakTaskLockMapper yakTaskLockMapper, YakTaskMapper yakTaskMapper, YakJobProperties yakJobProperties) {
        this.jobManager = jobManager;
        this.yakWorkerMapper = yakWorkerMapper;
        this.yakTaskLockMapper = yakTaskLockMapper;
        this.yakTaskMapper = yakTaskMapper;
        this.yakJobProperties = yakJobProperties;
    }

    @Override
    public boolean beat() {
        logger.info("class=BeatManagerImpl||method=beat||msg=beat beat!!!");
        this.cleanWorker();
        WorkerSingleton workerSingleton = WorkerSingleton.getInstance();
        workerSingleton.updateInstanceMetrics();
        YakWorker yakWorker = workerSingleton.getYakWorker();
        yakWorker.setJobNum(this.jobManager.runningJobSize());
        yakWorker.setAppName(this.yakJobProperties.getAppName());
        int ret = null == this.yakWorkerMapper.selectByCode(yakWorker.getWorkerCode(), yakWorker.getAppName()) ? this.yakWorkerMapper.insert(yakWorker.getWorker()) : this.yakWorkerMapper.updateByCode(yakWorker.getWorker());
        return ret > 0;
    }

    @Override
    public boolean stop() {
        WorkerSingleton workerSingleton = WorkerSingleton.getInstance();
        YakWorker yakWorker = workerSingleton.getYakWorker();
        this.yakWorkerMapper.deleteByCode(yakWorker.getWorkerCode());
        this.yakTaskLockMapper.deleteByWorkerCodeAndAppName(yakWorker.getWorkerCode(), this.yakJobProperties.getAppName());
        return true;
    }

    private void cleanTask(String appName, String workCode) {
        List<YakTaskPO> yakTaskPOS = this.yakTaskMapper.selectByAppName(appName);
        if (!CollectionUtils.isEmpty(yakTaskPOS)) {
            for (YakTaskPO yakTaskPO : yakTaskPOS) {
                try {
                    List<YakTask.TaskWorker> taskWorkers = BeanUtil.convertToList(yakTaskPO.getTaskWorkerStr(), YakTask.TaskWorker.class);
                    if (CollectionUtils.isEmpty(taskWorkers)) continue;
                    boolean needUpdate = false;
                    Iterator<YakTask.TaskWorker> iter = taskWorkers.iterator();
                    while (iter.hasNext()) {
                        YakTask.TaskWorker taskWorker = iter.next();
                        if (!workCode.equals(taskWorker.getWorkerCode())) continue;
                        iter.remove();
                        needUpdate = true;
                    }
                    if (!needUpdate) continue;
                    yakTaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
                    this.yakTaskMapper.updateTaskWorkStrByCode(yakTaskPO);
                } catch (Exception e) {
                    logger.info("class=BeatManagerImpl||method=cleanTask||msg=clean task worker error!", (Throwable)e);
                }
            }
        }
    }

    private void cleanWorker() {
        long currentTime = System.currentTimeMillis();
        String appName = this.yakJobProperties.getAppName();
        List<YakWorkerPO> yakWorkerPOS = this.yakWorkerMapper.selectByAppName(appName);
        if (CollectionUtils.isEmpty(yakWorkerPOS)) {
            return;
        }
        for (YakWorkerPO yakWorkerPO : yakWorkerPOS) {
            if (yakWorkerPO.getHeartbeat().getTime() + 30000L >= currentTime) continue;
            this.yakWorkerMapper.deleteByCode(yakWorkerPO.getWorkerCode());
            this.yakTaskLockMapper.deleteByWorkerCodeAndAppName(yakWorkerPO.getWorkerCode(), appName);
            this.cleanTask(appName, yakWorkerPO.getWorkerCode());
        }
    }
}

