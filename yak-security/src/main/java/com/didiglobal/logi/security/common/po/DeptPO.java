/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package com.didiglobal.logi.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.didiglobal.logi.security.common.po.BasePO;

@TableName(value="logi_security_dept")
public class DeptPO
extends BasePO {
    private String deptName;
    private String description;
    private Integer parentId;
    private Boolean leaf;
    private Integer level;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeptPO)) {
            return false;
        }
        DeptPO other = (DeptPO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
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
        String this$deptName = this.getDeptName();
        String other$deptName = other.getDeptName();
        if (this$deptName == null ? other$deptName != null : !this$deptName.equals(other$deptName)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof DeptPO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $parentId = this.getParentId();
        result = result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
        Boolean $leaf = this.getLeaf();
        result = result * 59 + ($leaf == null ? 43 : ((Object)$leaf).hashCode());
        Integer $level = this.getLevel();
        result = result * 59 + ($level == null ? 43 : ((Object)$level).hashCode());
        String $deptName = this.getDeptName();
        result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getDescription() {
        return this.description;
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

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "DeptPO(deptName=" + this.getDeptName() + ", description=" + this.getDescription() + ", parentId=" + this.getParentId() + ", leaf=" + this.getLeaf() + ", level=" + this.getLevel() + ")";
    }
}

