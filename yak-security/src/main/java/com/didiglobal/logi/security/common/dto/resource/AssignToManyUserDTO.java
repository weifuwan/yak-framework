/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u8d44\u6e90\u6743\u9650\u5206\u914d\u4fe1\u606f\uff0c\u5206\u914d\u7528\u6237\uff08\u67d0\u9879\u76ee\uff0c\u67d0\u9879\u76ee\u4e0b\u67d0\u8d44\u6e90\u7c7b\u522b\uff0c\u67d0\u9879\u76ee\u4e0b\u67d0\u8d44\u6e90\u7c7b\u522b\u4e0b\u67d0\u5177\u4f53\u8d44\u6e90\u6743\u9650->\u5206\u914dN\u4e2a\u7528\u6237\uff09")
public class AssignToManyUserDTO {
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=true)
    private Integer projectId;
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u522bid\uff08\u5982\u679c\u4e3anull\uff0c\u5219\u8868\u793a\u8be5\u9879\u76ee\u4e0b\u7684\u6240\u6709\u5177\u4f53\u8d44\u6e90\u6743\u9650\u90fd\u5206\u914d\u7ed9\u7528\u6237list\uff09", dataType="Integer", required=false)
    private Integer resourceTypeId;
    @ApiModelProperty(value="\u5177\u4f53\u8d44\u6e90id\uff08\u5982\u679c\u4e3anull\uff0c\u5219\u8868\u793a\u8be5\u8d44\u6e90\u7c7b\u522b\u4e0b\u7684\u6240\u6709\u5177\u4f53\u8d44\u6e90\u6743\u9650\u90fd\u5206\u914d\u7ed9\u7528\u6237list\uff09", dataType="Integer", required=false)
    private Integer resourceId;
    @ApiModelProperty(value="\u7528\u6237idList\uff0c\u6570\u7ec4\u957f\u5ea6\u53ef\u4ee5\u4e3a0\uff0c\u4f46\u662f\u4e0d\u53ef\u4e3anull\uff0cidList\u4e3a\u7a7a\u8868\u793a\u79fb\u9664\u6240\u6709old\u8be5\u8d44\u6e90\u6743\u9650\u4e0e\u7528\u6237\u7684\u5173\u8054\u4fe1\u606f", dataType="List<Integer>", required=true)
    private List<Integer> userIdList;
    @ApiModelProperty(value="\u6392\u9664\u7684\u7528\u6237idList\uff08\u4e0d\u5220\u9664\u8be5\u7528\u6237\u5bf9\u8d44\u6e90\u7684\u6743\u9650\uff0c\u7528\u4e8e\u534a\u9009\u72b6\u6001\u7684\u7528\u6237\uff09", dataType="List<Integer>", required=false)
    private List<Integer> excludeUserIdList;
    @ApiModelProperty(value="\u8d44\u6e90\u7ba1\u7406\u7ea7\u522b\uff1a1\uff08\u67e5\u770b\u6743\u9650\uff09\u30012\uff08\u7ba1\u7406\u6743\u9650\uff09", dataType="Integer", required=true)
    private Integer controlLevel;

    public Integer getProjectId() {
        return this.projectId;
    }

    public Integer getResourceTypeId() {
        return this.resourceTypeId;
    }

    public Integer getResourceId() {
        return this.resourceId;
    }

    public List<Integer> getUserIdList() {
        return this.userIdList;
    }

    public List<Integer> getExcludeUserIdList() {
        return this.excludeUserIdList;
    }

    public Integer getControlLevel() {
        return this.controlLevel;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }

    public void setExcludeUserIdList(List<Integer> excludeUserIdList) {
        this.excludeUserIdList = excludeUserIdList;
    }

    public void setControlLevel(Integer controlLevel) {
        this.controlLevel = controlLevel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AssignToManyUserDTO)) {
            return false;
        }
        AssignToManyUserDTO other = (AssignToManyUserDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$projectId = this.getProjectId();
        Integer other$projectId = other.getProjectId();
        if (this$projectId == null ? other$projectId != null : !((Object)this$projectId).equals(other$projectId)) {
            return false;
        }
        Integer this$resourceTypeId = this.getResourceTypeId();
        Integer other$resourceTypeId = other.getResourceTypeId();
        if (this$resourceTypeId == null ? other$resourceTypeId != null : !((Object)this$resourceTypeId).equals(other$resourceTypeId)) {
            return false;
        }
        Integer this$resourceId = this.getResourceId();
        Integer other$resourceId = other.getResourceId();
        if (this$resourceId == null ? other$resourceId != null : !((Object)this$resourceId).equals(other$resourceId)) {
            return false;
        }
        Integer this$controlLevel = this.getControlLevel();
        Integer other$controlLevel = other.getControlLevel();
        if (this$controlLevel == null ? other$controlLevel != null : !((Object)this$controlLevel).equals(other$controlLevel)) {
            return false;
        }
        List<Integer> this$userIdList = this.getUserIdList();
        List<Integer> other$userIdList = other.getUserIdList();
        if (this$userIdList == null ? other$userIdList != null : !((Object)this$userIdList).equals(other$userIdList)) {
            return false;
        }
        List<Integer> this$excludeUserIdList = this.getExcludeUserIdList();
        List<Integer> other$excludeUserIdList = other.getExcludeUserIdList();
        return !(this$excludeUserIdList == null ? other$excludeUserIdList != null : !((Object)this$excludeUserIdList).equals(other$excludeUserIdList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AssignToManyUserDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $projectId = this.getProjectId();
        result = result * 59 + ($projectId == null ? 43 : ((Object)$projectId).hashCode());
        Integer $resourceTypeId = this.getResourceTypeId();
        result = result * 59 + ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
        Integer $resourceId = this.getResourceId();
        result = result * 59 + ($resourceId == null ? 43 : ((Object)$resourceId).hashCode());
        Integer $controlLevel = this.getControlLevel();
        result = result * 59 + ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
        List<Integer> $userIdList = this.getUserIdList();
        result = result * 59 + ($userIdList == null ? 43 : ((Object)$userIdList).hashCode());
        List<Integer> $excludeUserIdList = this.getExcludeUserIdList();
        result = result * 59 + ($excludeUserIdList == null ? 43 : ((Object)$excludeUserIdList).hashCode());
        return result;
    }

    public String toString() {
        return "AssignToManyUserDTO(projectId=" + this.getProjectId() + ", resourceTypeId=" + this.getResourceTypeId() + ", resourceId=" + this.getResourceId() + ", userIdList=" + this.getUserIdList() + ", excludeUserIdList=" + this.getExcludeUserIdList() + ", controlLevel=" + this.getControlLevel() + ")";
    }
}

