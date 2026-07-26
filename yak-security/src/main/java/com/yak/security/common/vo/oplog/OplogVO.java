/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.vo.oplog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel(description="\u64cd\u4f5c\u65e5\u5fd7\u4fe1\u606f")
public class OplogVO {
    @ApiModelProperty(value="\u64cd\u4f5c\u65e5\u5fd7id", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u64cd\u4f5c\u8005ip", dataType="String", required=false)
    private String operatorIp;
    @ApiModelProperty(value="\u64cd\u4f5c\u8005\u7528\u6237\u8d26\u53f7", dataType="String", required=false)
    private String operator;
    @ApiModelProperty(value="\u64cd\u4f5c\u7c7b\u578b", dataType="String", required=false)
    private String operateType;
    @ApiModelProperty(value="\u64cd\u4f5c\u5bf9\u8c61", dataType="String", required=false)
    private String target;
    @ApiModelProperty(value="\u5bf9\u8c61\u5206\u7c7b", dataType="String", required=false)
    private String targetType;
    @ApiModelProperty(value="\u64cd\u4f5c\u65e5\u5fd7\u8be6\u60c5", dataType="String", required=false)
    private String detail;
    @ApiModelProperty(value="\u8bb0\u5f55\u65f6\u95f4\uff08\u65f6\u95f4\u6233ms\uff09", dataType="Long", required=false)
    private Date createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4\uff08\u65f6\u95f4\u6233ms\uff09", dataType="Long", required=false)
    private Date updateTime;

    public Integer getId() {
        return this.id;
    }

    public String getOperatorIp() {
        return this.operatorIp;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getOperateType() {
        return this.operateType;
    }

    public String getTarget() {
        return this.target;
    }

    public String getTargetType() {
        return this.targetType;
    }

    public String getDetail() {
        return this.detail;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOperatorIp(String operatorIp) {
        this.operatorIp = operatorIp;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OplogVO)) {
            return false;
        }
        OplogVO other = (OplogVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$operatorIp = this.getOperatorIp();
        String other$operatorIp = other.getOperatorIp();
        if (this$operatorIp == null ? other$operatorIp != null : !this$operatorIp.equals(other$operatorIp)) {
            return false;
        }
        String this$operator = this.getOperator();
        String other$operator = other.getOperator();
        if (this$operator == null ? other$operator != null : !this$operator.equals(other$operator)) {
            return false;
        }
        String this$operateType = this.getOperateType();
        String other$operateType = other.getOperateType();
        if (this$operateType == null ? other$operateType != null : !this$operateType.equals(other$operateType)) {
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
        String this$detail = this.getDetail();
        String other$detail = other.getDetail();
        if (this$detail == null ? other$detail != null : !this$detail.equals(other$detail)) {
            return false;
        }
        Date this$createTime = this.getCreateTime();
        Date other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Date this$updateTime = this.getUpdateTime();
        Date other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OplogVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $operatorIp = this.getOperatorIp();
        result = result * 59 + ($operatorIp == null ? 43 : $operatorIp.hashCode());
        String $operator = this.getOperator();
        result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
        String $operateType = this.getOperateType();
        result = result * 59 + ($operateType == null ? 43 : $operateType.hashCode());
        String $target = this.getTarget();
        result = result * 59 + ($target == null ? 43 : $target.hashCode());
        String $targetType = this.getTargetType();
        result = result * 59 + ($targetType == null ? 43 : $targetType.hashCode());
        String $detail = this.getDetail();
        result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
        Date $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Date $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        return result;
    }

    public String toString() {
        return "OplogVO(id=" + this.getId() + ", operatorIp=" + this.getOperatorIp() + ", operator=" + this.getOperator() + ", operateType=" + this.getOperateType() + ", target=" + this.getTarget() + ", targetType=" + this.getTargetType() + ", detail=" + this.getDetail() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

