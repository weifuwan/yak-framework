/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel(description = "YakTask \u4f5c\u4e1a\u4fe1\u606f")
public class YakJobDTO {
    @ApiModelProperty(value = "\u4f5c\u4e1acode")
    private String code;
    @ApiModelProperty(value = "\u4f5c\u4e1a\u6240\u5c5e\u4efb\u52a1code")
    private String taskCode;
    @ApiModelProperty(value = "\u4f5c\u4e1a\u6240\u5c5e\u4efb\u52a1\u7684\u7c7b\u4fe1\u606f")
    private String className;
    @ApiModelProperty(value = "\u4f5c\u4e1a\u91cd\u8bd5\u6b21\u6570")
    private Integer tryTimes;
    @ApiModelProperty(value = "\u8c03\u5ea6\u5668\u5730\u5740")
    private String workerCode;
    @ApiModelProperty(value = "\u4f5c\u4e1a\u5f00\u59cb\u6267\u884c\u65f6\u95f4")
    private Timestamp startTime;
    @ApiModelProperty(value = "\u4f5c\u4e1a\u521b\u5efa\u65f6\u95f4")
    private Timestamp createTime;
    @ApiModelProperty(value = "\u4f5c\u4e1a\u66f4\u65b0\u65f6\u95f4")
    private Timestamp updateTime;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakJobDTO)) {
            return false;
        }
        YakJobDTO other = (YakJobDTO) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$tryTimes = this.getTryTimes();
        Integer other$tryTimes = other.getTryTimes();
        if (this$tryTimes == null ? other$tryTimes != null : !((Object) this$tryTimes).equals(other$tryTimes)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
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
        if (this$startTime == null ? other$startTime != null : !((Object) this$startTime).equals(other$startTime)) {
            return false;
        }
        Timestamp this$createTime = this.getCreateTime();
        Timestamp other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object) this$createTime).equals(other$createTime)) {
            return false;
        }
        Timestamp this$updateTime = this.getUpdateTime();
        Timestamp other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !((Object) this$updateTime).equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof YakJobDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $tryTimes = this.getTryTimes();
        result = result * 59 + ($tryTimes == null ? 43 : ((Object) $tryTimes).hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $taskCode = this.getTaskCode();
        result = result * 59 + ($taskCode == null ? 43 : $taskCode.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $workerCode = this.getWorkerCode();
        result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
        Timestamp $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object) $startTime).hashCode());
        Timestamp $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object) $createTime).hashCode());
        Timestamp $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object) $updateTime).hashCode());
        return result;
    }

    public String toString() {
        return "YakJobDTO(code=" + this.getCode() + ", taskCode=" + this.getTaskCode() + ", className=" + this.getClassName() + ", tryTimes=" + this.getTryTimes() + ", workerCode=" + this.getWorkerCode() + ", startTime=" + this.getStartTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

