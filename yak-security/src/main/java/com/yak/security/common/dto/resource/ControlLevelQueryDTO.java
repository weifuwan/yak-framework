/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.dto.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u83b7\u53d6\u7528\u6237\u62e5\u6709\u8d44\u6e90\u7ba1\u7406\u6743\u9650\u7c7b\u522b\u7684\u67e5\u8be2\u6761\u4ef6")
public class ControlLevelQueryDTO {
    @ApiModelProperty(value="\u7528\u6237id", dataType="Integer", required=true)
    private Integer userId;
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=true)
    private Integer projectId;
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u522bid", dataType="Integer", required=true)
    private Integer resourceTypeId;
    @ApiModelProperty(value="\u5177\u4f53\u8d44\u6e90id", dataType="Integer", required=true)
    private Integer resourceId;

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public Integer getResourceTypeId() {
        return this.resourceTypeId;
    }

    public Integer getResourceId() {
        return this.resourceId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ControlLevelQueryDTO)) {
            return false;
        }
        ControlLevelQueryDTO other = (ControlLevelQueryDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
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
        return !(this$resourceId == null ? other$resourceId != null : !((Object)this$resourceId).equals(other$resourceId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ControlLevelQueryDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $projectId = this.getProjectId();
        result = result * 59 + ($projectId == null ? 43 : ((Object)$projectId).hashCode());
        Integer $resourceTypeId = this.getResourceTypeId();
        result = result * 59 + ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
        Integer $resourceId = this.getResourceId();
        result = result * 59 + ($resourceId == null ? 43 : ((Object)$resourceId).hashCode());
        return result;
    }

    public String toString() {
        return "ControlLevelQueryDTO(userId=" + this.getUserId() + ", projectId=" + this.getProjectId() + ", resourceTypeId=" + this.getResourceTypeId() + ", resourceId=" + this.getResourceId() + ")";
    }
}

