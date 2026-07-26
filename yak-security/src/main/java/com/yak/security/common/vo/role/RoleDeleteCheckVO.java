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
import java.util.List;

@ApiModel(description="\u89d2\u8272\u5220\u9664\u524d\u7684\u68c0\u67e5\u4fe1\u606f")
public class RoleDeleteCheckVO {
    @ApiModelProperty(value="\u89d2\u8272id", dataType="Integer", required=false)
    private Integer roleId;
    @ApiModelProperty(value="\u7528\u6237\u540dlist\uff0c\u5b58\u653e\u5f15\u7528\u8be5\u89d2\u8272\u7684\u7528\u6237\u540d", dataType="List<String>", required=false)
    private List<String> userNameList;

    public Integer getRoleId() {
        return this.roleId;
    }

    public List<String> getUserNameList() {
        return this.userNameList;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoleDeleteCheckVO)) {
            return false;
        }
        RoleDeleteCheckVO other = (RoleDeleteCheckVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$roleId = this.getRoleId();
        Integer other$roleId = other.getRoleId();
        if (this$roleId == null ? other$roleId != null : !((Object)this$roleId).equals(other$roleId)) {
            return false;
        }
        List<String> this$userNameList = this.getUserNameList();
        List<String> other$userNameList = other.getUserNameList();
        return !(this$userNameList == null ? other$userNameList != null : !((Object)this$userNameList).equals(other$userNameList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RoleDeleteCheckVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        List<String> $userNameList = this.getUserNameList();
        result = result * 59 + ($userNameList == null ? 43 : ((Object)$userNameList).hashCode());
        return result;
    }

    public String toString() {
        return "RoleDeleteCheckVO(roleId=" + this.getRoleId() + ", userNameList=" + this.getUserNameList() + ")";
    }
}

