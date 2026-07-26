/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.po;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogIJobLogPO
extends BasePO
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String jobCode;
    private String taskCode;
    private Long taskId;
    private String taskName;
    private String taskDesc;
    private String className;
    private Integer tryTimes;
    private String workerCode;
    private String workerIp;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer status;
    private String error;
    private String result;
    private String appName;

    public Long getId() {
        return this.id;
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

    public String getResult() {
        return this.result;
    }

    public String getAppName() {
        return this.appName;
    }

    public LogIJobLogPO setId(Long id) {
        this.id = id;
        return this;
    }

    public LogIJobLogPO setJobCode(String jobCode) {
        this.jobCode = jobCode;
        return this;
    }

    public LogIJobLogPO setTaskCode(String taskCode) {
        this.taskCode = taskCode;
        return this;
    }

    public LogIJobLogPO setTaskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public LogIJobLogPO setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public LogIJobLogPO setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
        return this;
    }

    public LogIJobLogPO setClassName(String className) {
        this.className = className;
        return this;
    }

    public LogIJobLogPO setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
        return this;
    }

    public LogIJobLogPO setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
        return this;
    }

    public LogIJobLogPO setWorkerIp(String workerIp) {
        this.workerIp = workerIp;
        return this;
    }

    public LogIJobLogPO setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        return this;
    }

    public LogIJobLogPO setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        return this;
    }

    public LogIJobLogPO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public LogIJobLogPO setError(String error) {
        this.error = error;
        return this;
    }

    public LogIJobLogPO setResult(String result) {
        this.result = result;
        return this;
    }

    public LogIJobLogPO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    @Override
    public String toString() {
        return "LogIJobLogPO(id=" + this.getId() + ", jobCode=" + this.getJobCode() + ", taskCode=" + this.getTaskCode() + ", taskId=" + this.getTaskId() + ", taskName=" + this.getTaskName() + ", taskDesc=" + this.getTaskDesc() + ", className=" + this.getClassName() + ", tryTimes=" + this.getTryTimes() + ", workerCode=" + this.getWorkerCode() + ", workerIp=" + this.getWorkerIp() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", status=" + this.getStatus() + ", error=" + this.getError() + ", result=" + this.getResult() + ", appName=" + this.getAppName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogIJobLogPO)) {
            return false;
        }
        LogIJobLogPO other = (LogIJobLogPO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$taskId = this.getTaskId();
        Long other$taskId = other.getTaskId();
        if (this$taskId == null ? other$taskId != null : !((Object)this$taskId).equals(other$taskId)) {
            return false;
        }
        Integer this$tryTimes = this.getTryTimes();
        Integer other$tryTimes = other.getTryTimes();
        if (this$tryTimes == null ? other$tryTimes != null : !((Object)this$tryTimes).equals(other$tryTimes)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$jobCode = this.getJobCode();
        String other$jobCode = other.getJobCode();
        if (this$jobCode == null ? other$jobCode != null : !this$jobCode.equals(other$jobCode)) {
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
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$workerCode = this.getWorkerCode();
        String other$workerCode = other.getWorkerCode();
        if (this$workerCode == null ? other$workerCode != null : !this$workerCode.equals(other$workerCode)) {
            return false;
        }
        String this$workerIp = this.getWorkerIp();
        String other$workerIp = other.getWorkerIp();
        if (this$workerIp == null ? other$workerIp != null : !this$workerIp.equals(other$workerIp)) {
            return false;
        }
        Timestamp this$startTime = this.getStartTime();
        Timestamp other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !((Object)this$startTime).equals(other$startTime)) {
            return false;
        }
        Timestamp this$endTime = this.getEndTime();
        Timestamp other$endTime = other.getEndTime();
        if (this$endTime == null ? other$endTime != null : !((Object)this$endTime).equals(other$endTime)) {
            return false;
        }
        String this$error = this.getError();
        String other$error = other.getError();
        if (this$error == null ? other$error != null : !this$error.equals(other$error)) {
            return false;
        }
        String this$result = this.getResult();
        String other$result = other.getResult();
        if (this$result == null ? other$result != null : !this$result.equals(other$result)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        return !(this$appName == null ? other$appName != null : !this$appName.equals(other$appName));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof LogIJobLogPO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $taskId = this.getTaskId();
        result = result * 59 + ($taskId == null ? 43 : ((Object)$taskId).hashCode());
        Integer $tryTimes = this.getTryTimes();
        result = result * 59 + ($tryTimes == null ? 43 : ((Object)$tryTimes).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $jobCode = this.getJobCode();
        result = result * 59 + ($jobCode == null ? 43 : $jobCode.hashCode());
        String $taskCode = this.getTaskCode();
        result = result * 59 + ($taskCode == null ? 43 : $taskCode.hashCode());
        String $taskName = this.getTaskName();
        result = result * 59 + ($taskName == null ? 43 : $taskName.hashCode());
        String $taskDesc = this.getTaskDesc();
        result = result * 59 + ($taskDesc == null ? 43 : $taskDesc.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $workerCode = this.getWorkerCode();
        result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
        String $workerIp = this.getWorkerIp();
        result = result * 59 + ($workerIp == null ? 43 : $workerIp.hashCode());
        Timestamp $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        Timestamp $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        String $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        String $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        return result;
    }
}

