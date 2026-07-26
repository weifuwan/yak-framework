/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.vo.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u5206\u914d\u89d2\u8272\u6216\u8005\u5206\u914d\u7528\u6237/\u5217\u8868\u4fe1\u606f")
public class AssignInfoVO {
    @ApiModelProperty(value="\u5206\u914d\u7528\u6237\uff1a\u7528\u6237id\uff0c\u5206\u914d\u89d2\u8272\uff1a\u89d2\u8272id", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u5206\u914d\u7528\u6237\uff1a\u7528\u6237\u540d\uff0c\u5206\u914d\u89d2\u8272\uff1a\u89d2\u8272\u540d", dataType="String", required=false)
    private String name;
    @ApiModelProperty(value="\u7528\u6237\u662f\u5426\u62e5\u6709\u8be5\u89d2\u8272 \u6216 \u8be5\u89d2\u8272\u662f\u5426\u5206\u914d\u8be5\u7528\u6237", dataType="Boolean", required=false)
    private Boolean has;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getHas() {
        return this.has;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHas(Boolean has) {
        this.has = has;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AssignInfoVO)) {
            return false;
        }
        AssignInfoVO other = (AssignInfoVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Boolean this$has = this.getHas();
        Boolean other$has = other.getHas();
        if (this$has == null ? other$has != null : !((Object)this$has).equals(other$has)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AssignInfoVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Boolean $has = this.getHas();
        result = result * 59 + ($has == null ? 43 : ((Object)$has).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "AssignInfoVO(id=" + this.getId() + ", name=" + this.getName() + ", has=" + this.getHas() + ")";
    }
}

