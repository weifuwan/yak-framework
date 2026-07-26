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

@ApiModel(description="\u6309\u7528\u6237\u7ba1\u7406/\u5206\u914d\u8d44\u6e90/\u6570\u636e\u5217\u8868\u7684\u67e5\u8be2\u6761\u4ef6")
public class MByUDataQueryDTO {
    @ApiModelProperty(value="\u7528\u6237id", dataType="Integer", required=true)
    private Integer userId;
    @ApiModelProperty(value="\u9879\u76eeid\uff082\uff0c3\u5c55\u793a\u7ea7\u522b\u4e0d\u53ef\u4e3anull\uff09", dataType="Integer", required=false)
    private Integer projectId;
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u522bid\uff083\u5c55\u793a\u7ea7\u522b\u4e0d\u53ef\u4e3anull\uff09", dataType="Integer", required=false)
    private Integer resourceTypeId;
    @ApiModelProperty(value="\u6309\u8d44\u6e90\u7ba1\u7406\u5217\u8868\u5c55\u793a\u7ea7\u522b\uff1a1 \u9879\u76ee\u5c55\u793a\u7ea7\u522b\u30012 \u8d44\u6e90\u7c7b\u522b\u5c55\u793a\u7ea7\u522b\u30013 \u5177\u4f53\u8d44\u6e90\u5c55\u793a\u7ea7\u522b", dataType="Integer", required=true)
    private Integer showLevel;
    @ApiModelProperty(value="\u8d44\u6e90\u7ba1\u7406\u7ea7\u522b\uff1a1\uff08\u67e5\u770b\u6743\u9650\uff09\u30012\uff08\u7ba1\u7406\u6743\u9650\uff09", dataType="Integer", required=true)
    private Integer controlLevel;
    @ApiModelProperty(value="\u662f\u5426\u662f\u6279\u91cf\u64cd\u4f5c\uff0c\u662f\u5426\u662f\u9875\u9762\u70b9\u51fb\u6279\u91cf\u64cd\u4f5c\u8df3\u8f6c\u7684", dataType="Boolean", required=true)
    private Boolean batch;

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public Integer getResourceTypeId() {
        return this.resourceTypeId;
    }

    public Integer getShowLevel() {
        return this.showLevel;
    }

    public Integer getControlLevel() {
        return this.controlLevel;
    }

    public Boolean getBatch() {
        return this.batch;
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

    public void setShowLevel(Integer showLevel) {
        this.showLevel = showLevel;
    }

    public void setControlLevel(Integer controlLevel) {
        this.controlLevel = controlLevel;
    }

    public void setBatch(Boolean batch) {
        this.batch = batch;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MByUDataQueryDTO)) {
            return false;
        }
        MByUDataQueryDTO other = (MByUDataQueryDTO)o;
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
        Integer this$showLevel = this.getShowLevel();
        Integer other$showLevel = other.getShowLevel();
        if (this$showLevel == null ? other$showLevel != null : !((Object)this$showLevel).equals(other$showLevel)) {
            return false;
        }
        Integer this$controlLevel = this.getControlLevel();
        Integer other$controlLevel = other.getControlLevel();
        if (this$controlLevel == null ? other$controlLevel != null : !((Object)this$controlLevel).equals(other$controlLevel)) {
            return false;
        }
        Boolean this$batch = this.getBatch();
        Boolean other$batch = other.getBatch();
        return !(this$batch == null ? other$batch != null : !((Object)this$batch).equals(other$batch));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MByUDataQueryDTO;
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
        Integer $showLevel = this.getShowLevel();
        result = result * 59 + ($showLevel == null ? 43 : ((Object)$showLevel).hashCode());
        Integer $controlLevel = this.getControlLevel();
        result = result * 59 + ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
        Boolean $batch = this.getBatch();
        result = result * 59 + ($batch == null ? 43 : ((Object)$batch).hashCode());
        return result;
    }

    public String toString() {
        return "MByUDataQueryDTO(userId=" + this.getUserId() + ", projectId=" + this.getProjectId() + ", resourceTypeId=" + this.getResourceTypeId() + ", showLevel=" + this.getShowLevel() + ", controlLevel=" + this.getControlLevel() + ", batch=" + this.getBatch() + ")";
    }
}

