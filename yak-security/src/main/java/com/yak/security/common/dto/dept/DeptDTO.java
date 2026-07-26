/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.dto.dept;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

@ApiModel(description="\u90e8\u95e8\u5bfc\u5165\u4fe1\u606f")
public class DeptDTO {
    @ApiModelProperty(value="\u90e8\u95e8\u540d", dataType="String", required=true)
    private String deptName;
    @ApiModelProperty(value="\u90e8\u95e8\u63cf\u8ff0", dataType="String", required=false)
    private String description;
    @ApiModelProperty(value="\u5b50\u90e8\u95e8", dataType="List<DeptDTO>", required=false)
    private List<DeptDTO> childDeptDTOList;

    public List<DeptDTO> getChildDeptDTOList() {
        if (this.childDeptDTOList == null) {
            this.childDeptDTOList = new ArrayList<DeptDTO>();
        }
        return this.childDeptDTOList;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChildDeptDTOList(List<DeptDTO> childDeptDTOList) {
        this.childDeptDTOList = childDeptDTOList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeptDTO)) {
            return false;
        }
        DeptDTO other = (DeptDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$deptName = this.getDeptName();
        String other$deptName = other.getDeptName();
        if (this$deptName == null ? other$deptName != null : !this$deptName.equals(other$deptName)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        List<DeptDTO> this$childDeptDTOList = this.getChildDeptDTOList();
        List<DeptDTO> other$childDeptDTOList = other.getChildDeptDTOList();
        return !(this$childDeptDTOList == null ? other$childDeptDTOList != null : !((Object)this$childDeptDTOList).equals(other$childDeptDTOList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeptDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $deptName = this.getDeptName();
        result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        List<DeptDTO> $childDeptDTOList = this.getChildDeptDTOList();
        result = result * 59 + ($childDeptDTOList == null ? 43 : ((Object)$childDeptDTOList).hashCode());
        return result;
    }

    public String toString() {
        return "DeptDTO(deptName=" + this.getDeptName() + ", description=" + this.getDescription() + ", childDeptDTOList=" + this.getChildDeptDTOList() + ")";
    }
}

