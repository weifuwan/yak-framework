/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.po;

import java.io.Serializable;
import java.sql.Timestamp;

public class YakTaskPO
extends BasePO
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String taskCode;
    private String taskName;
    private String taskDesc;
    private String cron;
    private String className;
    private String params;
    private Integer retryTimes;
    private Timestamp lastFireTime;
    private Long timeout;
    private Integer status;
    private String subTaskCodes;
    private String consensual;
    private String taskWorkerStr;
    private String appName;
    private String owner;

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

    public String getTaskWorkerStr() {
        return this.taskWorkerStr;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getOwner() {
        return this.owner;
    }

    public YakTaskPO setId(Long id) {
        this.id = id;
        return this;
    }

    public YakTaskPO setTaskCode(String taskCode) {
        this.taskCode = taskCode;
        return this;
    }

    public YakTaskPO setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public YakTaskPO setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
        return this;
    }

    public YakTaskPO setCron(String cron) {
        this.cron = cron;
        return this;
    }

    public YakTaskPO setClassName(String className) {
        this.className = className;
        return this;
    }

    public YakTaskPO setParams(String params) {
        this.params = params;
        return this;
    }

    public YakTaskPO setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public YakTaskPO setLastFireTime(Timestamp lastFireTime) {
        this.lastFireTime = lastFireTime;
        return this;
    }

    public YakTaskPO setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public YakTaskPO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public YakTaskPO setSubTaskCodes(String subTaskCodes) {
        this.subTaskCodes = subTaskCodes;
        return this;
    }

    public YakTaskPO setConsensual(String consensual) {
        this.consensual = consensual;
        return this;
    }

    public YakTaskPO setTaskWorkerStr(String taskWorkerStr) {
        this.taskWorkerStr = taskWorkerStr;
        return this;
    }

    public YakTaskPO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public YakTaskPO setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "YakTaskPO(id=" + this.getId() + ", taskCode=" + this.getTaskCode() + ", taskName=" + this.getTaskName() + ", taskDesc=" + this.getTaskDesc() + ", cron=" + this.getCron() + ", className=" + this.getClassName() + ", params=" + this.getParams() + ", retryTimes=" + this.getRetryTimes() + ", lastFireTime=" + this.getLastFireTime() + ", timeout=" + this.getTimeout() + ", status=" + this.getStatus() + ", subTaskCodes=" + this.getSubTaskCodes() + ", consensual=" + this.getConsensual() + ", taskWorkerStr=" + this.getTaskWorkerStr() + ", appName=" + this.getAppName() + ", owner=" + this.getOwner() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakTaskPO)) {
            return false;
        }
        YakTaskPO other = (YakTaskPO)o;
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
        String this$taskWorkerStr = this.getTaskWorkerStr();
        String other$taskWorkerStr = other.getTaskWorkerStr();
        if (this$taskWorkerStr == null ? other$taskWorkerStr != null : !this$taskWorkerStr.equals(other$taskWorkerStr)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        if (this$appName == null ? other$appName != null : !this$appName.equals(other$appName)) {
            return false;
        }
        String this$owner = this.getOwner();
        String other$owner = other.getOwner();
        return !(this$owner == null ? other$owner != null : !this$owner.equals(other$owner));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof YakTaskPO;
    }

    @Override
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
        String $subTaskCodes = this.getSubTaskCodes();
        result = result * 59 + ($subTaskCodes == null ? 43 : $subTaskCodes.hashCode());
        String $consensual = this.getConsensual();
        result = result * 59 + ($consensual == null ? 43 : $consensual.hashCode());
        String $taskWorkerStr = this.getTaskWorkerStr();
        result = result * 59 + ($taskWorkerStr == null ? 43 : $taskWorkerStr.hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        String $owner = this.getOwner();
        result = result * 59 + ($owner == null ? 43 : $owner.hashCode());
        return result;
    }
}

