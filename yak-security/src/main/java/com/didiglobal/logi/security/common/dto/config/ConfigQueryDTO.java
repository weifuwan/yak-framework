/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.config;

import com.didiglobal.logi.security.common.dto.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u914d\u7f6e\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class ConfigQueryDTO
extends PageParamDTO {
    @ApiModelProperty(value="\u914d\u7f6e\u7ec4")
    private String valueGroup;
    @ApiModelProperty(value="\u914d\u7f6e\u540d\u79f0")
    private String valueName;
    @ApiModelProperty(value="\u72b6\u6001(1 \u6b63\u5e38\uff1b2 \u7981\u7528)")
    private Integer status;
    @ApiModelProperty(value="\u5907\u6ce8/\u63cf\u8ff0")
    private String memo;
    @ApiModelProperty(value="\u64cd\u7eb5\u8005")
    private String operator;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConfigQueryDTO)) {
            return false;
        }
        ConfigQueryDTO other = (ConfigQueryDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$valueGroup = this.getValueGroup();
        String other$valueGroup = other.getValueGroup();
        if (this$valueGroup == null ? other$valueGroup != null : !this$valueGroup.equals(other$valueGroup)) {
            return false;
        }
        String this$valueName = this.getValueName();
        String other$valueName = other.getValueName();
        if (this$valueName == null ? other$valueName != null : !this$valueName.equals(other$valueName)) {
            return false;
        }
        String this$memo = this.getMemo();
        String other$memo = other.getMemo();
        if (this$memo == null ? other$memo != null : !this$memo.equals(other$memo)) {
            return false;
        }
        String this$operator = this.getOperator();
        String other$operator = other.getOperator();
        return !(this$operator == null ? other$operator != null : !this$operator.equals(other$operator));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof ConfigQueryDTO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $valueGroup = this.getValueGroup();
        result = result * 59 + ($valueGroup == null ? 43 : $valueGroup.hashCode());
        String $valueName = this.getValueName();
        result = result * 59 + ($valueName == null ? 43 : $valueName.hashCode());
        String $memo = this.getMemo();
        result = result * 59 + ($memo == null ? 43 : $memo.hashCode());
        String $operator = this.getOperator();
        result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
        return result;
    }

    public String getValueGroup() {
        return this.valueGroup;
    }

    public String getValueName() {
        return this.valueName;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getMemo() {
        return this.memo;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setValueGroup(String valueGroup) {
        this.valueGroup = valueGroup;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "ConfigQueryDTO(valueGroup=" + this.getValueGroup() + ", valueName=" + this.getValueName() + ", status=" + this.getStatus() + ", memo=" + this.getMemo() + ", operator=" + this.getOperator() + ")";
    }
}

