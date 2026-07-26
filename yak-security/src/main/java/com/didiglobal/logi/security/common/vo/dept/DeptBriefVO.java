/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.vo.dept;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u90e8\u95e8\u7b80\u8981\u4fe1\u606f")
public class DeptBriefVO {
    @ApiModelProperty(value="\u90e8\u95e8id", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u90e8\u95e8\u540d", dataType="String", required=false)
    private String deptName;
    @ApiModelProperty(value="\u7236\u90e8\u95e8id\uff08\u6839\u90e8\u95e8parentId\u4e3a0\uff09", dataType="Integer", required=false)
    private Integer parentId;

    public Integer getId() {
        return this.id;
    }

    public String getDeptName() {
        return this.deptName;
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

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeptBriefVO)) {
            return false;
        }
        DeptBriefVO other = (DeptBriefVO)o;
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
        String this$deptName = this.getDeptName();
        String other$deptName = other.getDeptName();
        return !(this$deptName == null ? other$deptName != null : !this$deptName.equals(other$deptName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeptBriefVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $parentId = this.getParentId();
        result = result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
        String $deptName = this.getDeptName();
        result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
        return result;
    }

    public String toString() {
        return "DeptBriefVO(id=" + this.getId() + ", deptName=" + this.getDeptName() + ", parentId=" + this.getParentId() + ")";
    }
}

