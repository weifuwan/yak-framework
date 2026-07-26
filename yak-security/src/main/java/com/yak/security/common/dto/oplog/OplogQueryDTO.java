/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.dto.oplog;

import com.yak.security.common.dto.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u64cd\u4f5c\u65e5\u5fd7\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class OplogQueryDTO
extends PageParamDTO {
    @ApiModelProperty(value="\u64cd\u4f5c\u7c7b\u578b\uff08\u7cbe\u786e\uff09", dataType="String", required=false)
    private String operateType;
    @ApiModelProperty(value="\u64cd\u4f5c\u8005\u8be6\u60c5\uff08\u6a21\u7cca\uff09", dataType="String", required=false)
    private String detail;
    @ApiModelProperty(value="\u64cd\u4f5c\u8005\u7528\u6237\u8d26\u53f7\uff08\u6a21\u7cca\uff09", dataType="String", required=false)
    private String operator;
    @ApiModelProperty(value="\u64cd\u4f5c\u5bf9\u8c61\uff08\u6a21\u7cca\uff09", dataType="String", required=false)
    private String target;
    @ApiModelProperty(value="\u64cd\u4f5c\u6a21\u5757\uff08\u7cbe\u786e\uff09", dataType="String", required=false)
    private String targetType;
    @ApiModelProperty(value="\u64cd\u4f5c\u65b9\u5f0f\uff08\u7cbe\u786e\uff09", dataType="String", required=false)
    private String operationMethods;
    @ApiModelProperty(value="\u64cd\u4f5c\u8d77\u59cb\u65f6\u95f4\uff08\u65f6\u95f4\u6233ms\uff09", dataType="Long", required=false)
    private Long startTime;
    @ApiModelProperty(value="\u64cd\u4f5c\u7ed3\u675f\u65f6\u95f4\uff08\u65f6\u95f4\u6233ms\uff09", dataType="Long", required=false)
    private Long endTime;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OplogQueryDTO)) {
            return false;
        }
        OplogQueryDTO other = (OplogQueryDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Long this$startTime = this.getStartTime();
        Long other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !((Object)this$startTime).equals(other$startTime)) {
            return false;
        }
        Long this$endTime = this.getEndTime();
        Long other$endTime = other.getEndTime();
        if (this$endTime == null ? other$endTime != null : !((Object)this$endTime).equals(other$endTime)) {
            return false;
        }
        String this$operateType = this.getOperateType();
        String other$operateType = other.getOperateType();
        if (this$operateType == null ? other$operateType != null : !this$operateType.equals(other$operateType)) {
            return false;
        }
        String this$detail = this.getDetail();
        String other$detail = other.getDetail();
        if (this$detail == null ? other$detail != null : !this$detail.equals(other$detail)) {
            return false;
        }
        String this$operator = this.getOperator();
        String other$operator = other.getOperator();
        if (this$operator == null ? other$operator != null : !this$operator.equals(other$operator)) {
            return false;
        }
        String this$target = this.getTarget();
        String other$target = other.getTarget();
        if (this$target == null ? other$target != null : !this$target.equals(other$target)) {
            return false;
        }
        String this$targetType = this.getTargetType();
        String other$targetType = other.getTargetType();
        if (this$targetType == null ? other$targetType != null : !this$targetType.equals(other$targetType)) {
            return false;
        }
        String this$operationMethods = this.getOperationMethods();
        String other$operationMethods = other.getOperationMethods();
        return !(this$operationMethods == null ? other$operationMethods != null : !this$operationMethods.equals(other$operationMethods));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof OplogQueryDTO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        Long $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        String $operateType = this.getOperateType();
        result = result * 59 + ($operateType == null ? 43 : $operateType.hashCode());
        String $detail = this.getDetail();
        result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
        String $operator = this.getOperator();
        result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
        String $target = this.getTarget();
        result = result * 59 + ($target == null ? 43 : $target.hashCode());
        String $targetType = this.getTargetType();
        result = result * 59 + ($targetType == null ? 43 : $targetType.hashCode());
        String $operationMethods = this.getOperationMethods();
        result = result * 59 + ($operationMethods == null ? 43 : $operationMethods.hashCode());
        return result;
    }

    public String getOperateType() {
        return this.operateType;
    }

    public String getDetail() {
        return this.detail;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getTarget() {
        return this.target;
    }

    public String getTargetType() {
        return this.targetType;
    }

    public String getOperationMethods() {
        return this.operationMethods;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setOperationMethods(String operationMethods) {
        this.operationMethods = operationMethods;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "OplogQueryDTO(operateType=" + this.getOperateType() + ", detail=" + this.getDetail() + ", operator=" + this.getOperator() + ", target=" + this.getTarget() + ", targetType=" + this.getTargetType() + ", operationMethods=" + this.getOperationMethods() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ")";
    }
}

