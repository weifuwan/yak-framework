/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.common.entity;

public class Permission {
    private Integer id;
    private String permissionName;
    private Integer parentId;
    private Boolean leaf;
    private Integer level;
    private String description;

    public Integer getId() {
        return this.id;
    }

    public String getPermissionName() {
        return this.permissionName;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public Boolean getLeaf() {
        return this.leaf;
    }

    public Integer getLevel() {
        return this.level;
    }

    public String getDescription() {
        return this.description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Permission)) {
            return false;
        }
        Permission other = (Permission)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$parentId = this.getParentId();
        Integer other$parentId = other.getParentId();
        if (this$parentId == null ? other$parentId != null : !((Object)this$parentId).equals(other$parentId)) {
            return false;
        }
        Boolean this$leaf = this.getLeaf();
        Boolean other$leaf = other.getLeaf();
        if (this$leaf == null ? other$leaf != null : !((Object)this$leaf).equals(other$leaf)) {
            return false;
        }
        Integer this$level = this.getLevel();
        Integer other$level = other.getLevel();
        if (this$level == null ? other$level != null : !((Object)this$level).equals(other$level)) {
            return false;
        }
        String this$permissionName = this.getPermissionName();
        String other$permissionName = other.getPermissionName();
        if (this$permissionName == null ? other$permissionName != null : !this$permissionName.equals(other$permissionName)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Permission;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $parentId = this.getParentId();
        result = result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
        Boolean $leaf = this.getLeaf();
        result = result * 59 + ($leaf == null ? 43 : ((Object)$leaf).hashCode());
        Integer $level = this.getLevel();
        result = result * 59 + ($level == null ? 43 : ((Object)$level).hashCode());
        String $permissionName = this.getPermissionName();
        result = result * 59 + ($permissionName == null ? 43 : $permissionName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String toString() {
        return "Permission(id=" + this.getId() + ", permissionName=" + this.getPermissionName() + ", parentId=" + this.getParentId() + ", leaf=" + this.getLeaf() + ", level=" + this.getLevel() + ", description=" + this.getDescription() + ")";
    }
}

