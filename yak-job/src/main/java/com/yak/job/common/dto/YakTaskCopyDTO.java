/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common.dto;

import lombok.Data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 任务复制数据传输对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "YakTask \u4efb\u52a1\u590d\u5236DTO")
public class YakTaskCopyDTO {
    /** 任务描述。 */
    @ApiModelProperty(value = "\u4efb\u52a1\u63cf\u8ff0\uff08\u4e2d\u6587\uff09")
    private String taskDesc;
    /** 工作节点 IP 列表。 */
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u5668\u7684ip\u5217\u8868")
    private List<String> workerIps;
    /** 任务参数。 */
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u5668\u7684\u53c2\u6570")
    private String param;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakTaskCopyDTO)) {
            return false;
        }
        YakTaskCopyDTO other = (YakTaskCopyDTO) o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$taskDesc = this.getTaskDesc();
        String other$taskDesc = other.getTaskDesc();
        if (this$taskDesc == null ? other$taskDesc != null : !this$taskDesc.equals(other$taskDesc)) {
            return false;
        }
        List<String> this$workerIps = this.getWorkerIps();
        List<String> other$workerIps = other.getWorkerIps();
        if (this$workerIps == null ? other$workerIps != null : !((Object) this$workerIps).equals(other$workerIps)) {
            return false;
        }
        String this$param = this.getParam();
        String other$param = other.getParam();
        return !(this$param == null ? other$param != null : !this$param.equals(other$param));
    }

    protected boolean canEqual(Object other) {
        return other instanceof YakTaskCopyDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $taskDesc = this.getTaskDesc();
        result = result * 59 + ($taskDesc == null ? 43 : $taskDesc.hashCode());
        List<String> $workerIps = this.getWorkerIps();
        result = result * 59 + ($workerIps == null ? 43 : ((Object) $workerIps).hashCode());
        String $param = this.getParam();
        result = result * 59 + ($param == null ? 43 : $param.hashCode());
        return result;
    }

    public String toString() {
        return "YakTaskCopyDTO(taskDesc=" + this.getTaskDesc() + ", workerIps=" + this.getWorkerIps() + ", param=" + this.getParam() + ")";
    }
}

