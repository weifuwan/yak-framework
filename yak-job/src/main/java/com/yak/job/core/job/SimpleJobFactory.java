/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 */
package com.yak.job.core.job;

import com.yak.job.common.domain.LogIJob;
import com.yak.job.common.domain.LogITask;
import com.yak.job.common.enums.JobStatusEnum;
import com.yak.job.core.WorkerSingleton;
import com.yak.job.utils.IdWorker;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleJobFactory
implements JobFactory {
    private static final Logger logger = LoggerFactory.getLogger(SimpleJobFactory.class);
    private Map<String, Job> jobMap = new HashMap<String, Job>();

    @Override
    public void addJob(String className, Job job) {
        this.jobMap.put(className, job);
        logger.info("class=SimpleJobFactory||method=addJob||className={}||jobMap={}", (Object)className, (Object)this.jobMap.toString());
    }

    @Override
    public LogIJob newJob(LogITask logITask) {
        if (null == this.jobMap.get(logITask.getClassName())) {
            return null;
        }
        LogIJob logIJob = new LogIJob();
        logIJob.setJobCode(IdWorker.getIdStr());
        logIJob.setTaskCode(logITask.getTaskCode());
        logIJob.setTaskId(logITask.getId());
        logIJob.setTaskName(logITask.getTaskName());
        logIJob.setTaskDesc(logITask.getTaskDesc());
        logIJob.setClassName(logITask.getClassName());
        logIJob.setWorkerCode(WorkerSingleton.getInstance().getLogIWorker().getWorkerCode());
        logIJob.setWorkerIp(WorkerSingleton.getInstance().getLogIWorker().getIp());
        logIJob.setTryTimes(logITask.getRetryTimes() == null ? 1 : logITask.getRetryTimes());
        logIJob.setStatus(JobStatusEnum.STARTED.getValue());
        logIJob.setTimeout(logITask.getTimeout());
        logIJob.setJob(this.jobMap.get(logITask.getClassName()));
        logIJob.setTaskCallback(logITask.getTaskCallback());
        logIJob.setAppName(logITask.getAppName());
        return logIJob;
    }
}

