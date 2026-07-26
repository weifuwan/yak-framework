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

@ApiModel(description="\u89d2\u8272\u7b80\u8981\u4fe1\u606f")
public class RoleBriefVO {
    @ApiModelProperty(value="\u89d2\u8272id", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u89d2\u8272\u540d", dataType="String", required=false)
    private String roleName;

    public Integer getId() {
        return this.id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoleBriefVO)) {
            return false;
        }
        RoleBriefVO other = (RoleBriefVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$roleName = this.getRoleName();
        String other$roleName = other.getRoleName();
        return !(this$roleName == null ? other$roleName != null : !this$roleName.equals(other$roleName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RoleBriefVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $roleName = this.getRoleName();
        result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
        return result;
    }

    public String toString() {
        return "RoleBriefVO(id=" + this.getId() + ", roleName=" + this.getRoleName() + ")";
    }
}

