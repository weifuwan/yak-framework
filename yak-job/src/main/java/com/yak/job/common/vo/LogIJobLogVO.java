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
import java.util.List;

@ApiModel(description="LogIJobLogVO \u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1\u8be6\u60c5")
public class LogIJobLogVO {
    @ApiModelProperty(value="\u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1id")
    private Long id;
    @ApiModelProperty(value="\u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1")
    private String jobCode;
    @ApiModelProperty(value="\u914d\u7f6e\u7684\u4efb\u52a1")
    private String taskCode;
    @ApiModelProperty(value="\u914d\u7f6e\u7684\u4efb\u52a1Id")
    private Long taskId;
    @ApiModelProperty(value="\u914d\u7f6e\u7684\u4efb\u52a1\u540d\u79f0")
    private String taskName;
    @ApiModelProperty(value="\u914d\u7f6e\u7684\u4efb\u52a1\u63cf\u8ff0")
    private String taskDesc;
    @ApiModelProperty(value="\u5b9a\u65f6\u4efb\u52a1\u8c03\u5ea6\u6267\u884c\u4ee3\u7801")
    private String className;
    @ApiModelProperty(value="\u8c03\u5ea6\u6267\u884c\u7684\u673a\u5668")
    private String workerCode;
    @ApiModelProperty(value="\u4efb\u52a1\u5f00\u59cb\u6267\u884c\u65f6\u95f4")
    private Timestamp startTime;
    @ApiModelProperty(value="\u4efb\u52a1\u7ed3\u675f\u6267\u884c\u65f6\u95f4")
    private Timestamp endTime;
    @ApiModelProperty(value="\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp createTime;
    @ApiModelProperty(value="\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp updateTime;
    @ApiModelProperty(value="\u4efb\u52a1\u8c03\u5ea6\u7ed3\u679c\uff0c0:\u8c03\u5ea6\u542f\u52a8\u4e2d\u30011:\u8fd0\u884c\u4e2d\u3001 2\uff1a\u6210\u529f\u30013\uff1a\u5931\u8d25\u30014\uff1a\u53d6\u6d88")
    private Integer status;
    @ApiModelProperty(value="\u4efb\u52a1\u6267\u884c\u9519\u8bef")
    private String error;
    @ApiModelProperty(value="\u4efb\u52a1\u6267\u884c\u7ed3\u679c")
    private String result;
    @ApiModelProperty(value="\u6240\u6709\u53ef\u88ab\u8c03\u5ea6\u7684\u673a\u5668\u5217\u8868")
    private List<String> allWorkerIps;
    @ApiModelProperty(value="\u8c03\u5ea6\u5230\u7684\u673a\u5668\u5217\u8868")
    private String workerIp;

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

    public String getWorkerCode() {
        return this.workerCode;
    }

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public Timestamp getEndTime() {
        return this.endTime;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
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

    public List<String> getAllWorkerIps() {
        return this.allWorkerIps;
    }

    public String getWorkerIp() {
        return this.workerIp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setAllWorkerIps(List<String> allWorkerIps) {
        this.allWorkerIps = allWorkerIps;
    }

    public void setWorkerIp(String workerIp) {
        this.workerIp = workerIp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogIJobLogVO)) {
            return false;
        }
        LogIJobLogVO other = (LogIJobLogVO)o;
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
        List<String> this$allWorkerIps = this.getAllWorkerIps();
        List<String> other$allWorkerIps = other.getAllWorkerIps();
        if (this$allWorkerIps == null ? other$allWorkerIps != null : !((Object)this$allWorkerIps).equals(other$allWorkerIps)) {
            return false;
        }
        String this$workerIp = this.getWorkerIp();
        String other$workerIp = other.getWorkerIp();
        return !(this$workerIp == null ? other$workerIp != null : !this$workerIp.equals(other$workerIp));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LogIJobLogVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $taskId = this.getTaskId();
        result = result * 59 + ($taskId == null ? 43 : ((Object)$taskId).hashCode());
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
        Timestamp $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        Timestamp $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        Timestamp $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Timestamp $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        String $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        List<String> $allWorkerIps = this.getAllWorkerIps();
        result = result * 59 + ($allWorkerIps == null ? 43 : ((Object)$allWorkerIps).hashCode());
        String $workerIp = this.getWorkerIp();
        result = result * 59 + ($workerIp == null ? 43 : $workerIp.hashCode());
        return result;
    }

    public String toString() {
        return "LogIJobLogVO(id=" + this.getId() + ", jobCode=" + this.getJobCode() + ", taskCode=" + this.getTaskCode() + ", taskId=" + this.getTaskId() + ", taskName=" + this.getTaskName() + ", taskDesc=" + this.getTaskDesc() + ", className=" + this.getClassName() + ", workerCode=" + this.getWorkerCode() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", status=" + this.getStatus() + ", error=" + this.getError() + ", result=" + this.getResult() + ", allWorkerIps=" + this.getAllWorkerIps() + ", workerIp=" + this.getWorkerIp() + ")";
    }
}

