/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.job.common.vo;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;

public class LogITaskLockVO {
    @ApiModelProperty(value="\u4efb\u52a1\u9501id")
    private Long id;
    @ApiModelProperty(value="\u4efb\u52a1code")
    private String taskCode;
    @ApiModelProperty(value="\u8c03\u5ea6\u5668")
    private String workerCode;
    @ApiModelProperty(value="\u4efb\u52a1\u9501\u521b\u5efa\u65f6\u95f4")
    private Timestamp createTime;
    @ApiModelProperty(value="\u4efb\u52a1\u9501\u66f4\u65b0\u65f6\u95f4")
    private Timestamp updateTime;

    public Long getId() {
        return this.id;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public String getWorkerCode() {
        return this.workerCode;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogITaskLockVO)) {
            return false;
        }
        LogITaskLockVO other = (LogITaskLockVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        Timestamp this$createTime = this.getCreateTime();
        Timestamp other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Timestamp this$updateTime = this.getUpdateTime();
        Timestamp other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LogITaskLockVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $taskCode = this.getTaskCode();
        result = result * 59 + ($taskCode == null ? 43 : $taskCode.hashCode());
        String $workerCode = this.getWorkerCode();
        result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
        Timestamp $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Timestamp $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        return result;
    }

    public String toString() {
        return "LogITaskLockVO(id=" + this.getId() + ", taskCode=" + this.getTaskCode() + ", workerCode=" + this.getWorkerCode() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

