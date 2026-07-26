/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.domain;

import com.yak.job.core.task.TaskCallback;

import java.sql.Timestamp;
import java.util.List;

public class YakTask {
    private Long id;
    private String taskCode;
    private String taskName;
    private String taskDesc;
    private String cron;
    private String className;
    private String params;
    private Integer retryTimes;
    private Timestamp lastFireTime;
    private Timestamp nextFireTime;
    private Long timeout;
    private Integer status;
    private String subTaskCodes;
    private String consensual;
    private List<TaskWorker> taskWorkers;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String appName;
    private String owner;
    private TaskCallback taskCallback;

    public Long getId() {
        return this.id;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public String getCron() {
        return this.cron;
    }

    public String getClassName() {
        return this.className;
    }

    public String getParams() {
        return this.params;
    }

    public Integer getRetryTimes() {
        return this.retryTimes;
    }

    public Timestamp getLastFireTime() {
        return this.lastFireTime;
    }

    public Timestamp getNextFireTime() {
        return this.nextFireTime;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getSubTaskCodes() {
        return this.subTaskCodes;
    }

    public String getConsensual() {
        return this.consensual;
    }

    public List<TaskWorker> getTaskWorkers() {
        return this.taskWorkers;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getOwner() {
        return this.owner;
    }

    public TaskCallback getTaskCallback() {
        return this.taskCallback;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public void setLastFireTime(Timestamp lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public void setNextFireTime(Timestamp nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setSubTaskCodes(String subTaskCodes) {
        this.subTaskCodes = subTaskCodes;
    }

    public void setConsensual(String consensual) {
        this.consensual = consensual;
    }

    public void setTaskWorkers(List<TaskWorker> taskWorkers) {
        this.taskWorkers = taskWorkers;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setTaskCallback(TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakTask)) {
            return false;
        }
        YakTask other = (YakTask)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$retryTimes = this.getRetryTimes();
        Integer other$retryTimes = other.getRetryTimes();
        if (this$retryTimes == null ? other$retryTimes != null : !((Object)this$retryTimes).equals(other$retryTimes)) {
            return false;
        }
        Long this$timeout = this.getTimeout();
        Long other$timeout = other.getTimeout();
        if (this$timeout == null ? other$timeout != null : !((Object)this$timeout).equals(other$timeout)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$taskCode = this.getTaskCode();
        String other$taskCode = other.getTaskCode();
        if (this$taskCode == null ? other$taskCode != null : !this$taskCode.equals(other$taskCode)) {
            return false;
        }
        String this$taskName = this.getTaskName();
        String other$taskName = other.getTaskName();
        if (this$taskName == null ? other$taskName != null : !this$taskName.equals(other$taskName)) {
            return false;
        }
        String this$taskDesc = this.getTaskDesc();
        String other$taskDesc = other.getTaskDesc();
        if (this$taskDesc == null ? other$taskDesc != null : !this$taskDesc.equals(other$taskDesc)) {
            return false;
        }
        String this$cron = this.getCron();
        String other$cron = other.getCron();
        if (this$cron == null ? other$cron != null : !this$cron.equals(other$cron)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$params = this.getParams();
        String other$params = other.getParams();
        if (this$params == null ? other$params != null : !this$params.equals(other$params)) {
            return false;
        }
        Timestamp this$lastFireTime = this.getLastFireTime();
        Timestamp other$lastFireTime = other.getLastFireTime();
        if (this$lastFireTime == null ? other$lastFireTime != null : !((Object)this$lastFireTime).equals(other$lastFireTime)) {
            return false;
        }
        Timestamp this$nextFireTime = this.getNextFireTime();
        Timestamp other$nextFireTime = other.getNextFireTime();
        if (this$nextFireTime == null ? other$nextFireTime != null : !((Object)this$nextFireTime).equals(other$nextFireTime)) {
            return false;
        }
        String this$subTaskCodes = this.getSubTaskCodes();
        String other$subTaskCodes = other.getSubTaskCodes();
        if (this$subTaskCodes == null ? other$subTaskCodes != null : !this$subTaskCodes.equals(other$subTaskCodes)) {
            return false;
        }
        String this$consensual = this.getConsensual();
        String other$consensual = other.getConsensual();
        if (this$consensual == null ? other$consensual != null : !this$consensual.equals(other$consensual)) {
            return false;
        }
        List<TaskWorker> this$taskWorkers = this.getTaskWorkers();
        List<TaskWorker> other$taskWorkers = other.getTaskWorkers();
        if (this$taskWorkers == null ? other$taskWorkers != null : !((Object)this$taskWorkers).equals(other$taskWorkers)) {
            return false;
        }
        Timestamp this$createTime = this.getCreateTime();
        Timestamp other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Timestamp this$updateTime = this.getUpdateTime();
        Timestamp other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        if (this$appName == null ? other$appName != null : !this$appName.equals(other$appName)) {
            return false;
        }
        String this$owner = this.getOwner();
        String other$owner = other.getOwner();
        if (this$owner == null ? other$owner != null : !this$owner.equals(other$owner)) {
            return false;
        }
        TaskCallback this$taskCallback = this.getTaskCallback();
        TaskCallback other$taskCallback = other.getTaskCallback();
        return !(this$taskCallback == null ? other$taskCallback != null : !this$taskCallback.equals(other$taskCallback));
    }

    protected boolean canEqual(Object other) {
        return other instanceof YakTask;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $retryTimes = this.getRetryTimes();
        result = result * 59 + ($retryTimes == null ? 43 : ((Object)$retryTimes).hashCode());
        Long $timeout = this.getTimeout();
        result = result * 59 + ($timeout == null ? 43 : ((Object)$timeout).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $taskCode = this.getTaskCode();
        result = result * 59 + ($taskCode == null ? 43 : $taskCode.hashCode());
        String $taskName = this.getTaskName();
        result = result * 59 + ($taskName == null ? 43 : $taskName.hashCode());
        String $taskDesc = this.getTaskDesc();
        result = result * 59 + ($taskDesc == null ? 43 : $taskDesc.hashCode());
        String $cron = this.getCron();
        result = result * 59 + ($cron == null ? 43 : $cron.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        Timestamp $lastFireTime = this.getLastFireTime();
        result = result * 59 + ($lastFireTime == null ? 43 : ((Object)$lastFireTime).hashCode());
        Timestamp $nextFireTime = this.getNextFireTime();
        result = result * 59 + ($nextFireTime == null ? 43 : ((Object)$nextFireTime).hashCode());
        String $subTaskCodes = this.getSubTaskCodes();
        result = result * 59 + ($subTaskCodes == null ? 43 : $subTaskCodes.hashCode());
        String $consensual = this.getConsensual();
        result = result * 59 + ($consensual == null ? 43 : $consensual.hashCode());
        List<TaskWorker> $taskWorkers = this.getTaskWorkers();
        result = result * 59 + ($taskWorkers == null ? 43 : ((Object)$taskWorkers).hashCode());
        Timestamp $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Timestamp $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        String $owner = this.getOwner();
        result = result * 59 + ($owner == null ? 43 : $owner.hashCode());
        TaskCallback $taskCallback = this.getTaskCallback();
        result = result * 59 + ($taskCallback == null ? 43 : $taskCallback.hashCode());
        return result;
    }

    public String toString() {
        return "YakTask(id=" + this.getId() + ", taskCode=" + this.getTaskCode() + ", taskName=" + this.getTaskName() + ", taskDesc=" + this.getTaskDesc() + ", cron=" + this.getCron() + ", className=" + this.getClassName() + ", params=" + this.getParams() + ", retryTimes=" + this.getRetryTimes() + ", lastFireTime=" + this.getLastFireTime() + ", nextFireTime=" + this.getNextFireTime() + ", timeout=" + this.getTimeout() + ", status=" + this.getStatus() + ", subTaskCodes=" + this.getSubTaskCodes() + ", consensual=" + this.getConsensual() + ", taskWorkers=" + this.getTaskWorkers() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", appName=" + this.getAppName() + ", owner=" + this.getOwner() + ", taskCallback=" + this.getTaskCallback() + ")";
    }

    public static class TaskWorker {
        private Integer status;
        private Timestamp lastFireTime;
        private String workerCode;
        private String ip;

        public Integer getStatus() {
            return this.status;
        }

        public Timestamp getLastFireTime() {
            return this.lastFireTime;
        }

        public String getWorkerCode() {
            return this.workerCode;
        }

        public String getIp() {
            return this.ip;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public void setLastFireTime(Timestamp lastFireTime) {
            this.lastFireTime = lastFireTime;
        }

        public void setWorkerCode(String workerCode) {
            this.workerCode = workerCode;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof TaskWorker)) {
                return false;
            }
            TaskWorker other = (TaskWorker)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Integer this$status = this.getStatus();
            Integer other$status = other.getStatus();
            if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
                return false;
            }
            Timestamp this$lastFireTime = this.getLastFireTime();
            Timestamp other$lastFireTime = other.getLastFireTime();
            if (this$lastFireTime == null ? other$lastFireTime != null : !((Object)this$lastFireTime).equals(other$lastFireTime)) {
                return false;
            }
            String this$workerCode = this.getWorkerCode();
            String other$workerCode = other.getWorkerCode();
            if (this$workerCode == null ? other$workerCode != null : !this$workerCode.equals(other$workerCode)) {
                return false;
            }
            String this$ip = this.getIp();
            String other$ip = other.getIp();
            return !(this$ip == null ? other$ip != null : !this$ip.equals(other$ip));
        }

        protected boolean canEqual(Object other) {
            return other instanceof TaskWorker;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Integer $status = this.getStatus();
            result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
            Timestamp $lastFireTime = this.getLastFireTime();
            result = result * 59 + ($lastFireTime == null ? 43 : ((Object)$lastFireTime).hashCode());
            String $workerCode = this.getWorkerCode();
            result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
            String $ip = this.getIp();
            result = result * 59 + ($ip == null ? 43 : $ip.hashCode());
            return result;
        }

        public String toString() {
            return "YakTask.TaskWorker(status=" + this.getStatus() + ", lastFireTime=" + this.getLastFireTime() + ", workerCode=" + this.getWorkerCode() + ", ip=" + this.getIp() + ")";
        }

        public TaskWorker(Integer status, Timestamp lastFireTime, String workerCode, String ip) {
            this.status = status;
            this.lastFireTime = lastFireTime;
            this.workerCode = workerCode;
            this.ip = ip;
        }

        public TaskWorker() {
        }
    }
}

