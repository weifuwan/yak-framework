/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel(description = "YakJobVO \u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1\u8be6\u60c5")
public class YakJobVO {
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1")
    private String jobCode;
    @ApiModelProperty(value = "\u914d\u7f6e\u7684\u4efb\u52a1")
    private String taskCode;
    @ApiModelProperty(value = "\u5b9a\u65f6\u4efb\u52a1\u8c03\u5ea6\u6267\u884c\u4ee3\u7801")
    private String className;
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u7684\u673a\u5668")
    private String workerCode;
    @ApiModelProperty(value = "\u4efb\u52a1\u6267\u884c\u9519\u8bef")
    private String error;
    @ApiModelProperty(value = "\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp createTime;
    @ApiModelProperty(value = "\u4efb\u52a1\u6267\u884c\u7ed3\u679c")
    private String result;

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

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getWorkerCode() {
        return this.workerCode;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakJobVO)) {
            return false;
        }
        YakJobVO other = (YakJobVO) o;
        if (!other.canEqual(this)) {
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
        String this$error = this.getError();
        String other$error = other.getError();
        if (this$error == null ? other$error != null : !this$error.equals(other$error)) {
            return false;
        }
        Timestamp this$createTime = this.getCreateTime();
        Timestamp other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object) this$createTime).equals(other$createTime)) {
            return false;
        }
        String this$result = this.getResult();
        String other$result = other.getResult();
        return !(this$result == null ? other$result != null : !this$result.equals(other$result));
    }

    protected boolean canEqual(Object other) {
        return other instanceof YakJobVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $jobCode = this.getJobCode();
        result = result * 59 + ($jobCode == null ? 43 : $jobCode.hashCode());
        String $taskCode = this.getTaskCode();
        result = result * 59 + ($taskCode == null ? 43 : $taskCode.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $workerCode = this.getWorkerCode();
        result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
        String $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        Timestamp $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object) $createTime).hashCode());
        String $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        return result;
    }

    public String toString() {
        return "YakJobVO(jobCode=" + this.getJobCode() + ", taskCode=" + this.getTaskCode() + ", className=" + this.getClassName() + ", workerCode=" + this.getWorkerCode() + ", error=" + this.getError() + ", createTime=" + this.getCreateTime() + ", result=" + this.getResult() + ")";
    }
}

