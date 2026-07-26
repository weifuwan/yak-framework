/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.user;

import com.didiglobal.logi.security.common.dto.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u7528\u6237\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class UserQueryDTO
extends PageParamDTO {
    @ApiModelProperty(value="\u6839\u636e\u7528\u6237id\u67e5\u8be2", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u6839\u636e\u89d2\u8272id\u67e5\u8be2", dataType="Integer", required=false)
    private Integer roleId;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7", dataType="String", required=false)
    private String userName;
    @ApiModelProperty(value="\u771f\u5b9e\u59d3\u540d", dataType="String", required=false)
    private String realName;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserQueryDTO)) {
            return false;
        }
        UserQueryDTO other = (UserQueryDTO)o;
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
        Integer this$roleId = this.getRoleId();
        Integer other$roleId = other.getRoleId();
        if (this$roleId == null ? other$roleId != null : !((Object)this$roleId).equals(other$roleId)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$realName = this.getRealName();
        String other$realName = other.getRealName();
        return !(this$realName == null ? other$realName != null : !this$realName.equals(other$realName));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof UserQueryDTO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        return result;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "UserQueryDTO(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", userName=" + this.getUserName() + ", realName=" + this.getRealName() + ")";
    }
}

