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

@ApiModel(description = "\u4efb\u52a1\u65e5\u5fd7\u5206\u9875\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class TaskLogPageQueryDTO {
    @ApiModelProperty(value = "\u5f53\u524d\u9875", dataType = "Integer", required = true)
    private Integer page = 1;
    @ApiModelProperty(value = "\u6bcf\u9875\u5927\u5c0f", dataType = "Integer", required = true)
    private Integer size = 10;
    @ApiModelProperty(value = "\u5f00\u59cb\u65f6\u95f4", dataType = "Long", required = false)
    private Long beginTime;
    @ApiModelProperty(value = "\u7ed3\u675f\u65f6\u95f4", dataType = "Long", required = false)
    private Long endTime;
    @ApiModelProperty(value = "\u4efb\u52a1\u72b6\u6001", dataType = "Integer", required = false)
    private Integer taskStatus;
    @ApiModelProperty(value = "\u4efb\u52a1id", dataType = "Long", required = false)
    private Long taskId;
    @ApiModelProperty(value = "\u4efb\u52a1\u63cf\u8ff0", dataType = "String", required = false)
    private String taskDesc;
    @ApiModelProperty(value = "\u6392\u5e8f\u65b9\u5f0f\uff0casc\uff1a\u6b63\u5e8f(\u4ece\u5c0f\u5230\u5927)\uff0cdesc\uff1a\u9006\u5e8f(\u4ece\u5927\u5230\u5c0f)", dataType = "String", required = false)
    private String sortAsc;
    @ApiModelProperty(value = "\u6392\u5e8f\u5b57\u6bb5\uff0c\u5fc5\u987b\u662f\uff1astatus\u3001result\u3001create_time\u3001start_time\u3001end_time", dataType = "String", required = false)
    private String sortName;

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getTaskStatus() {
        return this.taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getSortAsc() {
        return this.sortAsc;
    }

    public void setSortAsc(String sortAsc) {
        this.sortAsc = sortAsc;
    }

    public String getSortName() {
        return this.sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TaskLogPageQueryDTO)) {
            return false;
        }
        TaskLogPageQueryDTO other = (TaskLogPageQueryDTO) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$page = this.getPage();
        Integer other$page = other.getPage();
        if (this$page == null ? other$page != null : !((Object) this$page).equals(other$page)) {
            return false;
        }
        Integer this$size = this.getSize();
        Integer other$size = other.getSize();
        if (this$size == null ? other$size != null : !((Object) this$size).equals(other$size)) {
            return false;
        }
        Long this$beginTime = this.getBeginTime();
        Long other$beginTime = other.getBeginTime();
        if (this$beginTime == null ? other$beginTime != null : !((Object) this$beginTime).equals(other$beginTime)) {
            return false;
        }
        Long this$endTime = this.getEndTime();
        Long other$endTime = other.getEndTime();
        if (this$endTime == null ? other$endTime != null : !((Object) this$endTime).equals(other$endTime)) {
            return false;
        }
        Integer this$taskStatus = this.getTaskStatus();
        Integer other$taskStatus = other.getTaskStatus();
        if (this$taskStatus == null ? other$taskStatus != null : !((Object) this$taskStatus).equals(other$taskStatus)) {
            return false;
        }
        Long this$taskId = this.getTaskId();
        Long other$taskId = other.getTaskId();
        if (this$taskId == null ? other$taskId != null : !((Object) this$taskId).equals(other$taskId)) {
            return false;
        }
        String this$taskDesc = this.getTaskDesc();
        String other$taskDesc = other.getTaskDesc();
        if (this$taskDesc == null ? other$taskDesc != null : !this$taskDesc.equals(other$taskDesc)) {
            return false;
        }
        String this$sortAsc = this.getSortAsc();
        String other$sortAsc = other.getSortAsc();
        if (this$sortAsc == null ? other$sortAsc != null : !this$sortAsc.equals(other$sortAsc)) {
            return false;
        }
        String this$sortName = this.getSortName();
        String other$sortName = other.getSortName();
        return !(this$sortName == null ? other$sortName != null : !this$sortName.equals(other$sortName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof TaskLogPageQueryDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $page = this.getPage();
        result = result * 59 + ($page == null ? 43 : ((Object) $page).hashCode());
        Integer $size = this.getSize();
        result = result * 59 + ($size == null ? 43 : ((Object) $size).hashCode());
        Long $beginTime = this.getBeginTime();
        result = result * 59 + ($beginTime == null ? 43 : ((Object) $beginTime).hashCode());
        Long $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object) $endTime).hashCode());
        Integer $taskStatus = this.getTaskStatus();
        result = result * 59 + ($taskStatus == null ? 43 : ((Object) $taskStatus).hashCode());
        Long $taskId = this.getTaskId();
        result = result * 59 + ($taskId == null ? 43 : ((Object) $taskId).hashCode());
        String $taskDesc = this.getTaskDesc();
        result = result * 59 + ($taskDesc == null ? 43 : $taskDesc.hashCode());
        String $sortAsc = this.getSortAsc();
        result = result * 59 + ($sortAsc == null ? 43 : $sortAsc.hashCode());
        String $sortName = this.getSortName();
        result = result * 59 + ($sortName == null ? 43 : $sortName.hashCode());
        return result;
    }

    public String toString() {
        return "TaskLogPageQueryDTO(page=" + this.getPage() + ", size=" + this.getSize() + ", beginTime=" + this.getBeginTime() + ", endTime=" + this.getEndTime() + ", taskStatus=" + this.getTaskStatus() + ", taskId=" + this.getTaskId() + ", taskDesc=" + this.getTaskDesc() + ", sortAsc=" + this.getSortAsc() + ", sortName=" + this.getSortName() + ")";
    }
}

