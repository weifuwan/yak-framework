/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.role;

import com.didiglobal.logi.security.common.dto.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u89d2\u8272\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class RoleQueryDTO
extends PageParamDTO {
    @ApiModelProperty(value="\u89d2\u8272\u7f16\u53f7\uff08\u7cbe\u786e\uff09", dataType="String", required=false)
    private String roleCode;
    @ApiModelProperty(value="id\uff08\u7cbe\u786e\uff09", dataType="String", required=false)
    private Integer id;
    @ApiModelProperty(value="\u89d2\u8272\u540d\uff08\u6a21\u7cca\uff09", dataType="String", required=false)
    private String roleName;
    @ApiModelProperty(value="\u63cf\u8ff0\uff08\u6a21\u7cca\uff09", dataType="String", required=false)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoleQueryDTO)) {
            return false;
        }
        RoleQueryDTO other = (RoleQueryDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$roleCode = this.getRoleCode();
        String other$roleCode = other.getRoleCode();
        if (this$roleCode == null ? other$roleCode != null : !this$roleCode.equals(other$roleCode)) {
            return false;
        }
        String this$roleName = this.getRoleName();
        String other$roleName = other.getRoleName();
        if (this$roleName == null ? other$roleName != null : !this$roleName.equals(other$roleName)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof RoleQueryDTO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $roleCode = this.getRoleCode();
        result = result * 59 + ($roleCode == null ? 43 : $roleCode.hashCode());
        String $roleName = this.getRoleName();
        result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public Integer getId() {
        return this.id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RoleQueryDTO(roleCode=" + this.getRoleCode() + ", id=" + this.getId() + ", roleName=" + this.getRoleName() + ", description=" + this.getDescription() + ")";
    }
}

