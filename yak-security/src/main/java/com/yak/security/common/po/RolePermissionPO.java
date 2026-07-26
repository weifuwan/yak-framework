/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package com.yak.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value="logi_security_role_permission")
public class RolePermissionPO
extends BasePO {
    private Integer roleId;
    private Integer permissionId;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RolePermissionPO)) {
            return false;
        }
        RolePermissionPO other = (RolePermissionPO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$roleId = this.getRoleId();
        Integer other$roleId = other.getRoleId();
        if (this$roleId == null ? other$roleId != null : !((Object)this$roleId).equals(other$roleId)) {
            return false;
        }
        Integer this$permissionId = this.getPermissionId();
        Integer other$permissionId = other.getPermissionId();
        return !(this$permissionId == null ? other$permissionId != null : !((Object)this$permissionId).equals(other$permissionId));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof RolePermissionPO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        Integer $permissionId = this.getPermissionId();
        result = result * 59 + ($permissionId == null ? 43 : ((Object)$permissionId).hashCode());
        return result;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public Integer getPermissionId() {
        return this.permissionId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "RolePermissionPO(roleId=" + this.getRoleId() + ", permissionId=" + this.getPermissionId() + ")";
    }
}

