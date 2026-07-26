/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.common.entity.dept;

public class DeptBrief {
    private Integer id;
    private String deptName;
    private Boolean leaf;
    private Integer level;
    private Integer parentId;

    public Integer getId() {
        return this.id;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Boolean getLeaf() {
        return this.leaf;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeptBrief)) {
            return false;
        }
        DeptBrief other = (DeptBrief)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        Integer this$parentId = this.getParentId();
        Integer other$parentId = other.getParentId();
        if (this$parentId == null ? other$parentId != null : !((Object)this$parentId).equals(other$parentId)) {
            return false;
        }
        String this$deptName = this.getDeptName();
        String other$deptName = other.getDeptName();
        return !(this$deptName == null ? other$deptName != null : !this$deptName.equals(other$deptName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeptBrief;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Boolean $leaf = this.getLeaf();
        result = result * 59 + ($leaf == null ? 43 : ((Object)$leaf).hashCode());
        Integer $level = this.getLevel();
        result = result * 59 + ($level == null ? 43 : ((Object)$level).hashCode());
        Integer $parentId = this.getParentId();
        result = result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
        String $deptName = this.getDeptName();
        result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
        return result;
    }

    public String toString() {
        return "DeptBrief(id=" + this.getId() + ", deptName=" + this.getDeptName() + ", leaf=" + this.getLeaf() + ", level=" + this.getLevel() + ", parentId=" + this.getParentId() + ")";
    }
}

