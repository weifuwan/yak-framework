/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.po;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogIJobPO
extends BasePO
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String jobCode;
    private String taskCode;
    private String className;
    private Integer tryTimes;
    private String workerCode;
    private Timestamp startTime;
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

    public String getClassName() {
        return this.className;
    }

    public Integer getTryTimes() {
        return this.tryTimes;
    }

    public String getWorkerCode() {
        return this.workerCode;
    }

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public String getAppName() {
        return this.appName;
    }

    public LogIJobPO setId(Long id) {
        this.id = id;
        return this;
    }

    public LogIJobPO setJobCode(String jobCode) {
        this.jobCode = jobCode;
        return this;
    }

    public LogIJobPO setTaskCode(String taskCode) {
        this.taskCode = taskCode;
        return this;
    }

    public LogIJobPO setClassName(String className) {
        this.className = className;
        return this;
    }

    public LogIJobPO setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
        return this;
    }

    public LogIJobPO setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
        return this;
    }

    public LogIJobPO setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        return this;
    }

    public LogIJobPO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    @Override
    public String toString() {
        return "LogIJobPO(id=" + this.getId() + ", jobCode=" + this.getJobCode() + ", taskCode=" + this.getTaskCode() + ", className=" + this.getClassName() + ", tryTimes=" + this.getTryTimes() + ", workerCode=" + this.getWorkerCode() + ", startTime=" + this.getStartTime() + ", appName=" + this.getAppName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogIJobPO)) {
            return false;
        }
        LogIJobPO other = (LogIJobPO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$tryTimes = this.getTryTimes();
        Integer other$tryTimes = other.getTryTimes();
        if (this$tryTimes == null ? other$tryTimes != null : !((Object)this$tryTimes).equals(other$tryTimes)) {
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
        Timestamp this$startTime = this.getStartTime();
        Timestamp other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !((Object)this$startTime).equals(other$startTime)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        return !(this$appName == null ? other$appName != null : !this$appName.equals(other$appName));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof LogIJobPO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $tryTimes = this.getTryTimes();
        result = result * 59 + ($tryTimes == null ? 43 : ((Object)$tryTimes).hashCode());
        String $jobCode = this.getJobCode();
        result = result * 59 + ($jobCode == null ? 43 : $jobCode.hashCode());
        String $taskCode = this.getTaskCode();
        result = result * 59 + ($taskCode == null ? 43 : $taskCode.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $workerCode = this.getWorkerCode();
        result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
        Timestamp $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        return result;
    }
}

