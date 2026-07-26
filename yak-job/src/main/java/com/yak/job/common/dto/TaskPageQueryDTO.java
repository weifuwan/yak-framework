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

@ApiModel(description = "\u5206\u9875\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class TaskPageQueryDTO {
    @ApiModelProperty(value = "\u5f53\u524d\u9875", dataType = "Integer", required = true)
    private Integer page = 1;
    @ApiModelProperty(value = "\u6bcf\u9875\u5927\u5c0f", dataType = "Integer", required = true)
    private Integer size = 10;
    @ApiModelProperty(value = "\u4efb\u52a1id", dataType = "Long", required = false)
    private Long taskId;
    @ApiModelProperty(value = "\u4efb\u52a1\u63cf\u8ff0", dataType = "String", required = false)
    private String taskDesc;
    @ApiModelProperty(value = "\u4efb\u52a1\u5904\u7406\u5668", dataType = "String", required = false)
    private String className;
    @ApiModelProperty(value = "\u4efb\u52a1\u72b6\u6001", dataType = "Integer", required = false)
    private Integer taskStatus;

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

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getTaskStatus() {
        return this.taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TaskPageQueryDTO)) {
            return false;
        }
        TaskPageQueryDTO other = (TaskPageQueryDTO) o;
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
        Long this$taskId = this.getTaskId();
        Long other$taskId = other.getTaskId();
        if (this$taskId == null ? other$taskId != null : !((Object) this$taskId).equals(other$taskId)) {
            return false;
        }
        Integer this$taskStatus = this.getTaskStatus();
        Integer other$taskStatus = other.getTaskStatus();
        if (this$taskStatus == null ? other$taskStatus != null : !((Object) this$taskStatus).equals(other$taskStatus)) {
            return false;
        }
        String this$taskDesc = this.getTaskDesc();
        String other$taskDesc = other.getTaskDesc();
        if (this$taskDesc == null ? other$taskDesc != null : !this$taskDesc.equals(other$taskDesc)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        return !(this$className == null ? other$className != null : !this$className.equals(other$className));
    }

    protected boolean canEqual(Object other) {
        return other instanceof TaskPageQueryDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $page = this.getPage();
        result = result * 59 + ($page == null ? 43 : ((Object) $page).hashCode());
        Integer $size = this.getSize();
        result = result * 59 + ($size == null ? 43 : ((Object) $size).hashCode());
        Long $taskId = this.getTaskId();
        result = result * 59 + ($taskId == null ? 43 : ((Object) $taskId).hashCode());
        Integer $taskStatus = this.getTaskStatus();
        result = result * 59 + ($taskStatus == null ? 43 : ((Object) $taskStatus).hashCode());
        String $taskDesc = this.getTaskDesc();
        result = result * 59 + ($taskDesc == null ? 43 : $taskDesc.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        return result;
    }

    public String toString() {
        return "TaskPageQueryDTO(page=" + this.getPage() + ", size=" + this.getSize() + ", taskId=" + this.getTaskId() + ", taskDesc=" + this.getTaskDesc() + ", className=" + this.getClassName() + ", taskStatus=" + this.getTaskStatus() + ")";
    }
}

