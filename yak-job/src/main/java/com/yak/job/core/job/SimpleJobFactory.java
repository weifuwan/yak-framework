/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Component
 */
package com.yak.job.core.job;

import com.yak.job.common.domain.YakJob;
import com.yak.job.common.domain.YakTask;
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
    public YakJob newJob(YakTask yakTask) {
        if (null == this.jobMap.get(yakTask.getClassName())) {
            return null;
        }
        YakJob yakJob = new YakJob();
        yakJob.setJobCode(IdWorker.getIdStr());
        yakJob.setTaskCode(yakTask.getTaskCode());
        yakJob.setTaskId(yakTask.getId());
        yakJob.setTaskName(yakTask.getTaskName());
        yakJob.setTaskDesc(yakTask.getTaskDesc());
        yakJob.setClassName(yakTask.getClassName());
        yakJob.setWorkerCode(WorkerSingleton.getInstance().getYakWorker().getWorkerCode());
        yakJob.setWorkerIp(WorkerSingleton.getInstance().getYakWorker().getIp());
        yakJob.setTryTimes(yakTask.getRetryTimes() == null ? 1 : yakTask.getRetryTimes());
        yakJob.setStatus(JobStatusEnum.STARTED.getValue());
        yakJob.setTimeout(yakTask.getTimeout());
        yakJob.setJob(this.jobMap.get(yakTask.getClassName()));
        yakJob.setTaskCallback(yakTask.getTaskCallback());
        yakJob.setAppName(yakTask.getAppName());
        return yakJob;
    }
}

