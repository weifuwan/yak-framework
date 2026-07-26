/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.common.po;

import com.didiglobal.logi.job.common.po.BasePO;
import java.io.Serializable;

public class LogITaskLockPO
extends BasePO
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String taskCode;
    private String workerCode;
    private Long expireTime;
    private String appName;

    public Long getId() {
        return this.id;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public String getWorkerCode() {
        return this.workerCode;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public String getAppName() {
        return this.appName;
    }

    public LogITaskLockPO setId(Long id) {
        this.id = id;
        return this;
    }

    public LogITaskLockPO setTaskCode(String taskCode) {
        this.taskCode = taskCode;
        return this;
    }

    public LogITaskLockPO setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
        return this;
    }

    public LogITaskLockPO setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public LogITaskLockPO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    @Override
    public String toString() {
        return "LogITaskLockPO(id=" + this.getId() + ", taskCode=" + this.getTaskCode() + ", workerCode=" + this.getWorkerCode() + ", expireTime=" + this.getExpireTime() + ", appName=" + this.getAppName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogITaskLockPO)) {
            return false;
        }
        LogITaskLockPO other = (LogITaskLockPO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$expireTime = this.getExpireTime();
        Long other$expireTime = other.getExpireTime();
        if (this$expireTime == null ? other$expireTime != null : !((Object)this$expireTime).equals(other$expireTime)) {
            return false;
        }
        String this$taskCode = this.getTaskCode();
        String other$taskCode = other.getTaskCode();
        if (this$taskCode == null ? other$taskCode != null : !this$taskCode.equals(other$taskCode)) {
            return false;
        }
        String this$workerCode = this.getWorkerCode();
        String other$workerCode = other.getWorkerCode();
        if (this$workerCode == null ? other$workerCode != null : !this$workerCode.equals(other$workerCode)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        return !(this$appName == null ? other$appName != null : !this$appName.equals(other$appName));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof LogITaskLockPO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $expireTime = this.getExpireTime();
        result = result * 59 + ($expireTime == null ? 43 : ((Object)$expireTime).hashCode());
        String $taskCode = this.getTaskCode();
        result = result * 59 + ($taskCode == null ? 43 : $taskCode.hashCode());
        String $workerCode = this.getWorkerCode();
        result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        return result;
    }
}

