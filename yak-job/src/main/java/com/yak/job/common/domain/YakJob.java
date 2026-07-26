/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.domain;

import com.yak.job.common.TaskResult;
import com.yak.job.common.po.YakJobLogPO;
import com.yak.job.common.po.YakJobPO;
import com.yak.job.core.job.Job;
import com.yak.job.core.task.TaskCallback;
import com.yak.job.utils.BeanUtil;

import java.sql.Timestamp;
import java.util.Objects;

public class YakJob {
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

    public YakJobPO getAuvJob() {
        YakJobPO job = new YakJobPO();
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

    public YakJobLogPO getAuvJobLog() {
        YakJobLogPO yakJobLogPO = new YakJobLogPO();
        yakJobLogPO.setJobCode(this.getJobCode());
        yakJobLogPO.setTaskCode(this.getTaskCode());
        yakJobLogPO.setTaskId(this.getTaskId());
        yakJobLogPO.setTaskName(this.getTaskName());
        yakJobLogPO.setTaskDesc(this.getTaskDesc());
        yakJobLogPO.setClassName(this.getClassName());
        yakJobLogPO.setWorkerCode(this.getWorkerCode());
        yakJobLogPO.setWorkerIp(this.getWorkerIp());
        yakJobLogPO.setTryTimes(this.getTryTimes());
        yakJobLogPO.setStartTime(this.getStartTime());
        yakJobLogPO.setEndTime(this.getEndTime());
        yakJobLogPO.setStatus(this.getStatus());
        yakJobLogPO.setError(this.getError() == null ? "" : this.getError());
        yakJobLogPO.setResult(this.getResult() == null ? "" : BeanUtil.convertToJson(this.getResult()));
        yakJobLogPO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        yakJobLogPO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        yakJobLogPO.setAppName(this.getAppName());
        return yakJobLogPO;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        YakJob yakJob = (YakJob) o;
        return this.jobCode.equals(yakJob.jobCode);
    }

    public int hashCode() {
        return Objects.hash(this.jobCode);
    }

    public String getJobCode() {
        return this.jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getTryTimes() {
        return this.tryTimes;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }

    public String getWorkerCode() {
        return this.workerCode;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public String getWorkerIp() {
        return this.workerIp;
    }

    public void setWorkerIp(String workerIp) {
        this.workerIp = workerIp;
    }

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public TaskResult getResult() {
        return this.result;
    }

    public void setResult(TaskResult result) {
        this.result = result;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public TaskCallback getTaskCallback() {
        return this.taskCallback;
    }

    public void setTaskCallback(TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String toString() {
        return "YakJob(jobCode=" + this.getJobCode() + ", taskCode=" + this.getTaskCode() + ", taskId=" + this.getTaskId() + ", taskName=" + this.getTaskName() + ", taskDesc=" + this.getTaskDesc() + ", className=" + this.getClassName() + ", retryTimes=" + this.getRetryTimes() + ", tryTimes=" + this.getTryTimes() + ", workerCode=" + this.getWorkerCode() + ", workerIp=" + this.getWorkerIp() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", status=" + this.getStatus() + ", error=" + this.getError() + ", timeout=" + this.getTimeout() + ", result=" + this.getResult() + ", job=" + this.getJob() + ", taskCallback=" + this.getTaskCallback() + ", appName=" + this.getAppName() + ")";
    }
}

