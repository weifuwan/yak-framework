/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.vo.dept;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u90e8\u95e8\u6811\u4fe1\u606f")
public class DeptTreeVO {
    @ApiModelProperty(value="\u90e8\u95e8id", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u90e8\u95e8\u540d", dataType="String", required=false)
    private String deptName;
    @ApiModelProperty(value="\u63cf\u8ff0", dataType="String", required=false)
    private String description;
    @ApiModelProperty(value="\u7236\u90e8\u95e8id\uff08\u6839\u90e8\u95e8parentId\u4e3a0\uff09", dataType="Integer", required=false)
    private Integer parentId;
    @ApiModelProperty(value="\u662f\u5426\u662f\u53f6\u5b50\u90e8\u95e8", dataType="Boolean", required=false)
    private Boolean leaf;
    @ApiModelProperty(value="\u5b69\u5b50\u90e8\u95e8", dataType="List<DeptTreeVO>", required=false)
    private List<DeptTreeVO> childList;

    public DeptTreeVO() {
    }

    DeptTreeVO(Integer id, String deptName, String description, Integer parentId, Boolean leaf, List<DeptTreeVO> childList) {
        this.id = id;
        this.deptName = deptName;
        this.description = description;
        this.parentId = parentId;
        this.leaf = leaf;
        this.childList = childList;
    }

    public static DeptTreeVOBuilder builder() {
        return new DeptTreeVOBuilder();
    }

    public Integer getId() {
        return this.id;
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

    public List<DeptTreeVO> getChildList() {
        return this.childList;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setChildList(List<DeptTreeVO> childList) {
        this.childList = childList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeptTreeVO)) {
            return false;
        }
        DeptTreeVO other = (DeptTreeVO)o;
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
        List<DeptTreeVO> this$childList = this.getChildList();
        List<DeptTreeVO> other$childList = other.getChildList();
        return !(this$childList == null ? other$childList != null : !((Object)this$childList).equals(other$childList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeptTreeVO;
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
        String $deptName = this.getDeptName();
        result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        List<DeptTreeVO> $childList = this.getChildList();
        result = result * 59 + ($childList == null ? 43 : ((Object)$childList).hashCode());
        return result;
    }

    public String toString() {
        return "DeptTreeVO(id=" + this.getId() + ", deptName=" + this.getDeptName() + ", description=" + this.getDescription() + ", parentId=" + this.getParentId() + ", leaf=" + this.getLeaf() + ", childList=" + this.getChildList() + ")";
    }

    public static class DeptTreeVOBuilder {
        private Integer id;
        private String deptName;
        private String description;
        private Integer parentId;
        private Boolean leaf;
        private List<DeptTreeVO> childList;

        DeptTreeVOBuilder() {
        }

        public DeptTreeVOBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public DeptTreeVOBuilder deptName(String deptName) {
            this.deptName = deptName;
            return this;
        }

        public DeptTreeVOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DeptTreeVOBuilder parentId(Integer parentId) {
            this.parentId = parentId;
            return this;
        }

        public DeptTreeVOBuilder leaf(Boolean leaf) {
            this.leaf = leaf;
            return this;
        }

        public DeptTreeVOBuilder childList(List<DeptTreeVO> childList) {
            this.childList = childList;
            return this;
        }

        public DeptTreeVO build() {
            return new DeptTreeVO(this.id, this.deptName, this.description, this.parentId, this.leaf, this.childList);
        }

        public String toString() {
            return "DeptTreeVO.DeptTreeVOBuilder(id=" + this.id + ", deptName=" + this.deptName + ", description=" + this.description + ", parentId=" + this.parentId + ", leaf=" + this.leaf + ", childList=" + this.childList + ")";
        }
    }
}

