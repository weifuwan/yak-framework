/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.common.entity;

public class RolePermission {
    private Integer roleId;
    private Integer permissionId;

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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RolePermission)) {
            return false;
        }
        RolePermission other = (RolePermission)o;
        if (!other.canEqual(this)) {
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

    protected boolean canEqual(Object other) {
        return other instanceof RolePermission;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        Integer $permissionId = this.getPermissionId();
        result = result * 59 + ($permissionId == null ? 43 : ((Object)$permissionId).hashCode());
        return result;
    }

    public String toString() {
        return "RolePermission(roleId=" + this.getRoleId() + ", permissionId=" + this.getPermissionId() + ")";
    }
}

