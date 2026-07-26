/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.common.domain;

import com.didiglobal.logi.job.common.TaskResult;
import com.didiglobal.logi.job.common.po.LogIJobLogPO;
import com.didiglobal.logi.job.common.po.LogIJobPO;
import com.didiglobal.logi.job.core.job.Job;
import com.didiglobal.logi.job.core.task.TaskCallback;
import com.didiglobal.logi.job.utils.BeanUtil;
import java.sql.Timestamp;
import java.util.Objects;

public class LogIJob {
    private String jobCode;
    private String taskCode;
    private Long taskId;
    private String taskName;
    private String taskDesc;
    private String className;
    private Integer retryTimes;
    private Integer tryTimes;
    private String workerCode;
    private String workerIp;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer status;
    private String error;
    private Long timeout;
    private TaskResult result;
    private Job job;
    private TaskCallback taskCallback;
    private String appName;

    public LogIJobPO getAuvJob() {
        LogIJobPO job = new LogIJobPO();
        job.setJobCode(this.getJobCode());
        job.setTaskCode(this.getTaskCode());
        job.setClassName(this.getClassName());
        job.setTryTimes(this.getTryTimes());
        job.setWorkerCode(this.getWorkerCode());
        job.setAppName(this.getAppName());
        job.setStartTime(new Timestamp(System.currentTimeMillis()));
        job.setCreateTime(new Timestamp(System.currentTimeMillis()));
        job.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return job;
    }

    public LogIJobLogPO getAuvJobLog() {
        LogIJobLogPO logIJobLogPO = new LogIJobLogPO();
        logIJobLogPO.setJobCode(this.getJobCode());
        logIJobLogPO.setTaskCode(this.getTaskCode());
        logIJobLogPO.setTaskId(this.getTaskId());
        logIJobLogPO.setTaskName(this.getTaskName());
        logIJobLogPO.setTaskDesc(this.getTaskDesc());
        logIJobLogPO.setClassName(this.getClassName());
        logIJobLogPO.setWorkerCode(this.getWorkerCode());
        logIJobLogPO.setWorkerIp(this.getWorkerIp());
        logIJobLogPO.setTryTimes(this.getTryTimes());
        logIJobLogPO.setStartTime(this.getStartTime());
        logIJobLogPO.setEndTime(this.getEndTime());
        logIJobLogPO.setStatus(this.getStatus());
        logIJobLogPO.setError(this.getError() == null ? "" : this.getError());
        logIJobLogPO.setResult(this.getResult() == null ? "" : BeanUtil.convertToJson(this.getResult()));
        logIJobLogPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        logIJobLogPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        logIJobLogPO.setAppName(this.getAppName());
        return logIJobLogPO;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        LogIJob logIJob = (LogIJob)o;
        return this.jobCode.equals(logIJob.jobCode);
    }

    public int hashCode() {
        return Objects.hash(this.jobCode);
    }

    public String getJobCode() {
        return this.jobCode;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public String getClassName() {
        return this.className;
    }

    public Integer getRetryTimes() {
        return this.retryTimes;
    }

    public Integer getTryTimes() {
        return this.tryTimes;
    }

    public String getWorkerCode() {
        return this.workerCode;
    }

    public String getWorkerIp() {
        return this.workerIp;
    }

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public Timestamp getEndTime() {
        return this.endTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getError() {
        return this.error;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public TaskResult getResult() {
        return this.result;
    }

    public Job getJob() {
        return this.job;
    }

    public TaskCallback getTaskCallback() {
        return this.taskCallback;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public void setWorkerIp(String workerIp) {
        this.workerIp = workerIp;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public void setResult(TaskResult result) {
        this.result = result;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setTaskCallback(TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String toString() {
        return "LogIJob(jobCode=" + this.getJobCode() + ", taskCode=" + this.getTaskCode() + ", taskId=" + this.getTaskId() + ", taskName=" + this.getTaskName() + ", taskDesc=" + this.getTaskDesc() + ", className=" + this.getClassName() + ", retryTimes=" + this.getRetryTimes() + ", tryTimes=" + this.getTryTimes() + ", workerCode=" + this.getWorkerCode() + ", workerIp=" + this.getWorkerIp() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", status=" + this.getStatus() + ", error=" + this.getError() + ", timeout=" + this.getTimeout() + ", result=" + this.getResult() + ", job=" + this.getJob() + ", taskCallback=" + this.getTaskCallback() + ", appName=" + this.getAppName() + ")";
    }
}

