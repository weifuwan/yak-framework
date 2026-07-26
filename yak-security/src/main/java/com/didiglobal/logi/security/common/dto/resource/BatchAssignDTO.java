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

@ApiModel(description="\u8d44\u6e90\u6743\u9650\u7ba1\u7406\uff0c\u6279\u91cf\u5206\u914d\u7528\u6237\u548c\u6279\u91cf\u5206\u914d\u8d44\u6e90")
public class BatchAssignDTO {
    @ApiModelProperty(value="\u7528\u6237idList", dataType="Integer", required=true)
    private List<Integer> userIdList;
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=false)
    private Integer projectId;
    @ApiModelProperty(value="\u8d44\u6e90\u7c7b\u522bid\uff08\u5982\u679c\u4e3anull\uff0c\u5219\u8868\u793a\u8be5\u9879\u76ee\u4e0b\u7684\u6240\u6709\u5177\u4f53\u8d44\u6e90\u6743\u9650\u90fd\u5206\u914d\u7ed9\u7528\u6237list\uff09", dataType="Integer", required=false)
    private Integer resourceTypeId;
    @ApiModelProperty(value="projectId == null\uff0cresourceTypeId == null\uff0c\u5219\u8868\u793a\u9879\u76eeidList\nprojectId != null\uff0cresourceTypeId == null\uff0c\u5219\u8868\u793a\u8d44\u6e90\u7c7b\u522bidList\nprojectId != null\uff0cresourceTypeId != null\uff0c\u5219\u8868\u793a\u5177\u4f53\u8d44\u6e90idList\n\uff08\u6570\u7ec4\u957f\u5ea6\u53ef\u4ee5\u4e3a0\uff0c\u4f46\u662f\u4e0d\u53ef\u4e3anull\uff09", dataType="List<Integer>", required=true)
    private List<Integer> idList;
    @ApiModelProperty(value="\u8d44\u6e90\u7ba1\u7406\u7ea7\u522b\uff1a1\uff08\u67e5\u770b\u6743\u9650\uff09\u30012\uff08\u7ba1\u7406\u6743\u9650\uff09", dataType="Integer", required=true)
    private Integer controlLevel;
    @ApiModelProperty(value="\u5206\u914d\u6807\u8bb0\uff1atrue\uff08\u6309\u8d44\u6e90\u7ba1\u7406\u4e0b\u7684\u6279\u91cf\u5206\u914d\u7528\u6237\uff09\u3001false\uff08\u6309\u7528\u6237\u7ba1\u7406\u4e0b\u7684\u6279\u91cf\u5206\u914d\u8d44\u6e90\uff09", dataType="Boolean", required=true)
    private Boolean assignFlag;

    public List<Integer> getUserIdList() {
        return this.userIdList;
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

    public Integer getControlLevel() {
        return this.controlLevel;
    }

    public Boolean getAssignFlag() {
        return this.assignFlag;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
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

    public void setControlLevel(Integer controlLevel) {
        this.controlLevel = controlLevel;
    }

    public void setAssignFlag(Boolean assignFlag) {
        this.assignFlag = assignFlag;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BatchAssignDTO)) {
            return false;
        }
        BatchAssignDTO other = (BatchAssignDTO)o;
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
        Integer this$controlLevel = this.getControlLevel();
        Integer other$controlLevel = other.getControlLevel();
        if (this$controlLevel == null ? other$controlLevel != null : !((Object)this$controlLevel).equals(other$controlLevel)) {
            return false;
        }
        Boolean this$assignFlag = this.getAssignFlag();
        Boolean other$assignFlag = other.getAssignFlag();
        if (this$assignFlag == null ? other$assignFlag != null : !((Object)this$assignFlag).equals(other$assignFlag)) {
            return false;
        }
        List<Integer> this$userIdList = this.getUserIdList();
        List<Integer> other$userIdList = other.getUserIdList();
        if (this$userIdList == null ? other$userIdList != null : !((Object)this$userIdList).equals(other$userIdList)) {
            return false;
        }
        List<Integer> this$idList = this.getIdList();
        List<Integer> other$idList = other.getIdList();
        return !(this$idList == null ? other$idList != null : !((Object)this$idList).equals(other$idList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof BatchAssignDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $projectId = this.getProjectId();
        result = result * 59 + ($projectId == null ? 43 : ((Object)$projectId).hashCode());
        Integer $resourceTypeId = this.getResourceTypeId();
        result = result * 59 + ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
        Integer $controlLevel = this.getControlLevel();
        result = result * 59 + ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
        Boolean $assignFlag = this.getAssignFlag();
        result = result * 59 + ($assignFlag == null ? 43 : ((Object)$assignFlag).hashCode());
        List<Integer> $userIdList = this.getUserIdList();
        result = result * 59 + ($userIdList == null ? 43 : ((Object)$userIdList).hashCode());
        List<Integer> $idList = this.getIdList();
        result = result * 59 + ($idList == null ? 43 : ((Object)$idList).hashCode());
        return result;
    }

    public String toString() {
        return "BatchAssignDTO(userIdList=" + this.getUserIdList() + ", projectId=" + this.getProjectId() + ", resourceTypeId=" + this.getResourceTypeId() + ", idList=" + this.getIdList() + ", controlLevel=" + this.getControlLevel() + ", assignFlag=" + this.getAssignFlag() + ")";
    }
}

