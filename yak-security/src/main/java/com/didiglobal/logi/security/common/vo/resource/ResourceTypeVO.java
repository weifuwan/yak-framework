/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.vo.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u8d44\u6e90\u7c7b\u578b\u4fe1\u606f")
public class ResourceTypeVO {
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u578b\u6807\u8bc6", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u578b\u540d", dataType="String", required=false)
    private String typeName;

    public Integer getId() {
        return this.id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResourceTypeVO)) {
            return false;
        }
        ResourceTypeVO other = (ResourceTypeVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$typeName = this.getTypeName();
        String other$typeName = other.getTypeName();
        return !(this$typeName == null ? other$typeName != null : !this$typeName.equals(other$typeName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourceTypeVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $typeName = this.getTypeName();
        result = result * 59 + ($typeName == null ? 43 : $typeName.hashCode());
        return result;
    }

    public String toString() {
        return "ResourceTypeVO(id=" + this.getId() + ", typeName=" + this.getTypeName() + ")";
    }
}

