/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.common.entity.role;

import com.didiglobal.logi.security.common.entity.BaseEntity;

public class Role
extends BaseEntity {
    private String roleCode;
    private String roleName;
    private String description;
    private String lastReviser;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role other = (Role)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
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
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$lastReviser = this.getLastReviser();
        String other$lastReviser = other.getLastReviser();
        return !(this$lastReviser == null ? other$lastReviser != null : !this$lastReviser.equals(other$lastReviser));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof Role;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        String $roleCode = this.getRoleCode();
        result = result * 59 + ($roleCode == null ? 43 : $roleCode.hashCode());
        String $roleName = this.getRoleName();
        result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $lastReviser = this.getLastReviser();
        result = result * 59 + ($lastReviser == null ? 43 : $lastReviser.hashCode());
        return result;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLastReviser() {
        return this.lastReviser;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastReviser(String lastReviser) {
        this.lastReviser = lastReviser;
    }

    @Override
    public String toString() {
        return "Role(roleCode=" + this.getRoleCode() + ", roleName=" + this.getRoleName() + ", description=" + this.getDescription() + ", lastReviser=" + this.getLastReviser() + ")";
    }
}

