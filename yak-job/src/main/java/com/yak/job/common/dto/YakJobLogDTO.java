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

@ApiModel(description="YakTask \u4f5c\u4e1a\u65e5\u5fd7\u4fe1\u606f")
public class YakJobLogDTO {
    @ApiModelProperty(value="\u4f5c\u4e1a\u540d\u79f0")
    private String jobCode;
    @ApiModelProperty(value="\u4f5c\u4e1a\u6240\u5c5e\u4efb\u52a1\u540d\u79f0")
    private String taskCode;
    @ApiModelProperty(value="\u4f5c\u4e1a\u6240\u5c5e\u4efb\u52a1\u7684\u7c7b\u540d")
    private String className;
    @ApiModelProperty(value="\u4f5c\u4e1a\u5931\u8d25\u91cd\u8bd5\u6b21\u6570")
    private Integer tryTimes;
    @ApiModelProperty(value="\u4f5c\u4e1a\u8c03\u5ea6\u5668\u5730\u5740")
    private String workerCode;
    @ApiModelProperty(value="\u4f5c\u4e1a\u5f00\u59cb\u6267\u884c\u65f6\u95f4")
    private Timestamp startTime;
    @ApiModelProperty(value="\u4f5c\u4e1a\u6267\u884c\u7ed3\u675f\u65f6\u95f4")
    private Timestamp endTime;
    @ApiModelProperty(value="\u4f5c\u4e1a\u72b6\u6001")
    private Integer status;
    @ApiModelProperty(value="\u4f5c\u4e1a\u6267\u884c\u5931\u8d25\u4fe1\u606f")
    private String error;
    @ApiModelProperty(value="\u4f5c\u4e1a\u521b\u5efa\u65f6\u95f4")
    private Timestamp createTime;
    @ApiModelProperty(value="\u4f5c\u4e1a\u66f4\u65b0\u65f6\u95f4")
    private Timestamp updateTime;
    @ApiModelProperty(value="\u4f5c\u4e1a\u6267\u884c\u7ed3\u679c")
    private String result;
    @ApiModelProperty(value="\u4f5c\u4e1a\u6267\u884c\u4eba")
    private String operator;

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

    public Timestamp getEndTime() {
        return this.endTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getError() {
        return this.error;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public String getResult() {
        return this.result;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
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

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakJobLogDTO)) {
            return false;
        }
        YakJobLogDTO other = (YakJobLogDTO)o;
        if (!other.canEqual(this)) {
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
        String this$result = this.getResult();
        String other$result = other.getResult();
        if (this$result == null ? other$result != null : !this$result.equals(other$result)) {
            return false;
        }
        String this$operator = this.getOperator();
        String other$operator = other.getOperator();
        return !(this$operator == null ? other$operator != null : !this$operator.equals(other$operator));
    }

    protected boolean canEqual(Object other) {
        return other instanceof YakJobLogDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $tryTimes = this.getTryTimes();
        result = result * 59 + ($tryTimes == null ? 43 : ((Object)$tryTimes).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
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
        Timestamp $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        String $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        Timestamp $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Timestamp $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        String $operator = this.getOperator();
        result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
        return result;
    }

    public String toString() {
        return "YakJobLogDTO(jobCode=" + this.getJobCode() + ", taskCode=" + this.getTaskCode() + ", className=" + this.getClassName() + ", tryTimes=" + this.getTryTimes() + ", workerCode=" + this.getWorkerCode() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", status=" + this.getStatus() + ", error=" + this.getError() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", result=" + this.getResult() + ", operator=" + this.getOperator() + ")";
    }
}

