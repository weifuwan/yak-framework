/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.vo.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u9879\u76ee\u5220\u9664\u524d\u7684\u68c0\u67e5\u4fe1\u606f")
public class ProjectDeleteCheckVO {
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=false)
    private Integer projectId;
    @ApiModelProperty(value="\u670d\u52a1\u540dlist\uff0c\u5b58\u653e\u5f15\u7528\u8be5\u9879\u76ee\u7684\u5177\u4f53\u8d44\u6e90\u540d", dataType="List<String>", required=false)
    private List<String> resourceNameList;

    public ProjectDeleteCheckVO(Integer projectId, List<String> resourceNameList) {
        this.projectId = projectId;
        this.resourceNameList = resourceNameList;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public List<String> getResourceNameList() {
        return this.resourceNameList;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setResourceNameList(List<String> resourceNameList) {
        this.resourceNameList = resourceNameList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectDeleteCheckVO)) {
            return false;
        }
        ProjectDeleteCheckVO other = (ProjectDeleteCheckVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$projectId = this.getProjectId();
        Integer other$projectId = other.getProjectId();
        if (this$projectId == null ? other$projectId != null : !((Object)this$projectId).equals(other$projectId)) {
            return false;
        }
        List<String> this$resourceNameList = this.getResourceNameList();
        List<String> other$resourceNameList = other.getResourceNameList();
        return !(this$resourceNameList == null ? other$resourceNameList != null : !((Object)this$resourceNameList).equals(other$resourceNameList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ProjectDeleteCheckVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $projectId = this.getProjectId();
        result = result * 59 + ($projectId == null ? 43 : ((Object)$projectId).hashCode());
        List<String> $resourceNameList = this.getResourceNameList();
        result = result * 59 + ($resourceNameList == null ? 43 : ((Object)$resourceNameList).hashCode());
        return result;
    }

    public String toString() {
        return "ProjectDeleteCheckVO(projectId=" + this.getProjectId() + ", resourceNameList=" + this.getResourceNameList() + ")";
    }
}

