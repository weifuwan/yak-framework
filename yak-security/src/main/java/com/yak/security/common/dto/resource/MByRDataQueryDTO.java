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

@ApiModel(description="\u6309\u8d44\u6e90\u7ba1\u7406/\u5206\u914d\u7528\u6237/\u6570\u636e\u5217\u8868\u7684\u67e5\u8be2\u6761\u4ef6")
public class MByRDataQueryDTO {
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=true)
    private Integer projectId;
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u522bid", dataType="Integer", required=false)
    private Integer resourceTypeId;
    @ApiModelProperty(value="\u5177\u4f53\u8d44\u6e90id", dataType="Integer", required=false)
    private Integer resourceId;
    @ApiModelProperty(value="\u8d44\u6e90\u7ba1\u7406\u7ea7\u522b\uff1a1\uff08\u67e5\u770b\u6743\u9650\uff09\u30012\uff08\u7ba1\u7406\u6743\u9650\uff09", dataType="Integer", required=true)
    private Integer controlLevel;
    @ApiModelProperty(value="\u662f\u5426\u662f\u6279\u91cf\u64cd\u4f5c\uff0c\u662f\u5426\u662f\u9875\u9762\u70b9\u51fb\u6279\u91cf\u64cd\u4f5c\u8df3\u8f6c\u7684", dataType="Boolean", required=true)
    private Boolean batch;

    public Integer getProjectId() {
        return this.projectId;
    }

    public Integer getResourceTypeId() {
        return this.resourceTypeId;
    }

    public Integer getResourceId() {
        return this.resourceId;
    }

    public Integer getControlLevel() {
        return this.controlLevel;
    }

    public Boolean getBatch() {
        return this.batch;
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
        if (!(o instanceof MByRDataQueryDTO)) {
            return false;
        }
        MByRDataQueryDTO other = (MByRDataQueryDTO)o;
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
        Boolean this$batch = this.getBatch();
        Boolean other$batch = other.getBatch();
        return !(this$batch == null ? other$batch != null : !((Object)this$batch).equals(other$batch));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MByRDataQueryDTO;
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
        Boolean $batch = this.getBatch();
        result = result * 59 + ($batch == null ? 43 : ((Object)$batch).hashCode());
        return result;
    }

    public String toString() {
        return "MByRDataQueryDTO(projectId=" + this.getProjectId() + ", resourceTypeId=" + this.getResourceTypeId() + ", resourceId=" + this.getResourceId() + ", controlLevel=" + this.getControlLevel() + ", batch=" + this.getBatch() + ")";
    }
}

