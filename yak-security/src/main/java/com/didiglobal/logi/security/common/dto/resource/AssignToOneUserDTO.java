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

@ApiModel(description="\u8d44\u6e90\u6743\u9650\u5206\u914d\u4fe1\u606f\uff0c\u5206\u914d\u8d44\u6e90\uff08N\u9879\u76ee\u3001\u67d0\u9879\u76ee\u4e0bN\u8d44\u6e90\u7c7b\u522b\u3001\u67d0\u9879\u76ee\u4e0b\u67d0\u8d44\u6e90\u7c7b\u522b\u4e0bN\u5177\u4f53\u8d44\u6e90\u6743\u9650->\u5206\u914d\u7ed9\u67d0\u7528\u6237\uff09")
public class AssignToOneUserDTO {
    @ApiModelProperty(value="\u7528\u6237id", dataType="Integer", required=true)
    private Integer userId;
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=false)
    private Integer projectId;
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u522bid\uff08\u5982\u679c\u4e3anull\uff0c\u5219\u8868\u793a\u8be5\u9879\u76ee\u4e0b\u7684\u6240\u6709\u5177\u4f53\u8d44\u6e90\u6743\u9650\u90fd\u5206\u914d\u7ed9\u7528\u6237list\uff09", dataType="Integer", required=false)
    private Integer resourceTypeId;
    @ApiModelProperty(value="projectId == null\uff0cresourceTypeId == null\uff0c\u5219\u8868\u793a\u9879\u76eeidList\nprojectId != null\uff0cresourceTypeId == null\uff0c\u5219\u8868\u793a\u8d44\u6e90\u7c7b\u522bidList\nprojectId != null\uff0cresourceTypeId != null\uff0c\u5219\u8868\u793a\u5177\u4f53\u8d44\u6e90idList\n\uff08\u6570\u7ec4\u957f\u5ea6\u53ef\u4ee5\u4e3a0\uff0c\u4f46\u662f\u4e0d\u53ef\u4e3anull\uff09", dataType="List<Integer>", required=true)
    private List<Integer> idList;
    @ApiModelProperty(value="\u6392\u9664\u7684idList\uff0c\u5bf9\u4e8e\u534a\u9009\u4e2d\u72b6\u6001\u7684\u6570\u636e\uff0c\u5982\u679c\u7528\u6237\u4e0d\u53d6\u6d88\u6216\u8005\u52fe\u9009\uff0c\u5219\u653e\u5165\u6b64\u6570\u7ec4\nprojectId == null\uff0cresourceTypeId == null\uff0c\u5219\u8868\u793a\u9879\u76eeidList\nprojectId != null\uff0cresourceTypeId == null\uff0c\u5219\u8868\u793a\u8d44\u6e90\u7c7b\u522bidList\n\u5177\u4f53\u8d44\u6e90\u65e0\u534a\u9009\u4e2d\u72b6\u6001", dataType="Integer", required=true)
    private List<Integer> excludeIdList;
    @ApiModelProperty(value="\u8d44\u6e90\u7ba1\u7406\u7ea7\u522b\uff1a1\uff08\u67e5\u770b\u6743\u9650\uff09\u30012\uff08\u7ba1\u7406\u6743\u9650\uff09", dataType="Integer", required=true)
    private Integer controlLevel;

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public Integer getResourceTypeId() {
        return this.resourceTypeId;
    }

    public List<Integer> getIdList() {
        return this.idList;
    }

    public List<Integer> getExcludeIdList() {
        return this.excludeIdList;
    }

    public Integer getControlLevel() {
        return this.controlLevel;
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

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public void setExcludeIdList(List<Integer> excludeIdList) {
        this.excludeIdList = excludeIdList;
    }

    public void setControlLevel(Integer controlLevel) {
        this.controlLevel = controlLevel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AssignToOneUserDTO)) {
            return false;
        }
        AssignToOneUserDTO other = (AssignToOneUserDTO)o;
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
        Integer this$controlLevel = this.getControlLevel();
        Integer other$controlLevel = other.getControlLevel();
        if (this$controlLevel == null ? other$controlLevel != null : !((Object)this$controlLevel).equals(other$controlLevel)) {
            return false;
        }
        List<Integer> this$idList = this.getIdList();
        List<Integer> other$idList = other.getIdList();
        if (this$idList == null ? other$idList != null : !((Object)this$idList).equals(other$idList)) {
            return false;
        }
        List<Integer> this$excludeIdList = this.getExcludeIdList();
        List<Integer> other$excludeIdList = other.getExcludeIdList();
        return !(this$excludeIdList == null ? other$excludeIdList != null : !((Object)this$excludeIdList).equals(other$excludeIdList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AssignToOneUserDTO;
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
        Integer $controlLevel = this.getControlLevel();
        result = result * 59 + ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
        List<Integer> $idList = this.getIdList();
        result = result * 59 + ($idList == null ? 43 : ((Object)$idList).hashCode());
        List<Integer> $excludeIdList = this.getExcludeIdList();
        result = result * 59 + ($excludeIdList == null ? 43 : ((Object)$excludeIdList).hashCode());
        return result;
    }

    public String toString() {
        return "AssignToOneUserDTO(userId=" + this.getUserId() + ", projectId=" + this.getProjectId() + ", resourceTypeId=" + this.getResourceTypeId() + ", idList=" + this.getIdList() + ", excludeIdList=" + this.getExcludeIdList() + ", controlLevel=" + this.getControlLevel() + ")";
    }
}

