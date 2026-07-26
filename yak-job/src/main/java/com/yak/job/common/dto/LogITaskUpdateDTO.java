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
import java.util.List;

@ApiModel(description="LogITask \u4efb\u52a1\u7f16\u8f91DTO")
public class LogITaskUpdateDTO {
    @ApiModelProperty(value="\u8c03\u5ea6\u6267\u884c\u5668\u7684ip\u5217\u8868")
    private List<String> workerIps;
    @ApiModelProperty(value="\u8c03\u5ea6\u6267\u884c\u5668\u7684\u53c2\u6570")
    private String param;

    public List<String> getWorkerIps() {
        return this.workerIps;
    }

    public String getParam() {
        return this.param;
    }

    public void setWorkerIps(List<String> workerIps) {
        this.workerIps = workerIps;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogITaskUpdateDTO)) {
            return false;
        }
        LogITaskUpdateDTO other = (LogITaskUpdateDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<String> this$workerIps = this.getWorkerIps();
        List<String> other$workerIps = other.getWorkerIps();
        if (this$workerIps == null ? other$workerIps != null : !((Object)this$workerIps).equals(other$workerIps)) {
            return false;
        }
        String this$param = this.getParam();
        String other$param = other.getParam();
        return !(this$param == null ? other$param != null : !this$param.equals(other$param));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LogITaskUpdateDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<String> $workerIps = this.getWorkerIps();
        result = result * 59 + ($workerIps == null ? 43 : ((Object)$workerIps).hashCode());
        String $param = this.getParam();
        result = result * 59 + ($param == null ? 43 : $param.hashCode());
        return result;
    }

    public String toString() {
        return "LogITaskUpdateDTO(workerIps=" + this.getWorkerIps() + ", param=" + this.getParam() + ")";
    }
}

