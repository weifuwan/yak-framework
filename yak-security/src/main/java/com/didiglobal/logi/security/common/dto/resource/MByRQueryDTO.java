/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.resource;

import com.didiglobal.logi.security.common.dto.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u8d44\u6e90\u6743\u9650\u7ba1\u7406\uff08\u6309\u8d44\u6e90\u7ba1\u7406\u7684\u5217\u8868\u67e5\u8be2\u6761\u4ef6\uff09")
public class MByRQueryDTO
extends PageParamDTO {
    @ApiModelProperty(value="\u9879\u76eeid\uff082\uff0c3\u5c55\u793a\u7ea7\u522b\u4e0d\u53ef\u4e3anull\uff09", dataType="Integer", required=false)
    private Integer projectId;
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u522bid\uff083\u5c55\u793a\u7ea7\u522b\u4e0d\u53ef\u4e3anull\uff09", dataType="Integer", required=false)
    private Integer resourceTypeId;
    @ApiModelProperty(value="\u6309\u8d44\u6e90\u7ba1\u7406\u5217\u8868\u5c55\u793a\u7ea7\u522b\uff1a1 \u9879\u76ee\u5c55\u793a\u7ea7\u522b\u30012 \u8d44\u6e90\u7c7b\u522b\u5c55\u793a\u7ea7\u522b\u30013 \u5177\u4f53\u8d44\u6e90\u5c55\u793a\u7ea7\u522b", dataType="Integer", required=true)
    private Integer showLevel;
    @ApiModelProperty(value="\u9879\u76ee\u5c55\u793a\u7ea7\u522b\uff0c\u5219name\u8868\u793a\u9879\u76ee\u540d\u79f0\u3001\u8d44\u6e90\u7c7b\u522b\u5c55\u793a\u7ea7\u522b\uff0c\u5219name\u8868\u793a\u8d44\u6e90\u7c7b\u522b\u540d\u79f0\u3001\u5177\u4f53\u8d44\u6e90\u5c55\u793a\u7ea7\u522b\uff0c\u5219name\u8868\u793a\u5177\u4f53\u8d44\u6e90\u540d\u79f0\uff09", dataType="Integer", required=false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MByRQueryDTO)) {
            return false;
        }
        MByRQueryDTO other = (MByRQueryDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
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
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof MByRQueryDTO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $projectId = this.getProjectId();
        result = result * 59 + ($projectId == null ? 43 : ((Object)$projectId).hashCode());
        Integer $resourceTypeId = this.getResourceTypeId();
        result = result * 59 + ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
        Integer $showLevel = this.getShowLevel();
        result = result * 59 + ($showLevel == null ? 43 : ((Object)$showLevel).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
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

    public String getName() {
        return this.name;
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MByRQueryDTO(projectId=" + this.getProjectId() + ", resourceTypeId=" + this.getResourceTypeId() + ", showLevel=" + this.getShowLevel() + ", name=" + this.getName() + ")";
    }
}

