/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u9879\u76ee\u6dfb\u52a0\u6216\u66f4\u65b0\u4fe1\u606f")
public class ProjectSaveDTO {
    @ApiModelProperty(value="\u9879\u76eeid\uff08\u66f4\u65b0\u64cd\u4f5c\u5fc5\u5907\uff09", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u9879\u76ee\u540d", dataType="String", required=true)
    private String projectName;
    @ApiModelProperty(value="\u9879\u76ee\u6210\u5458idList", dataType="List<Integer>", required=true)
    private List<Integer> userIdList;
    @ApiModelProperty(value="\u9879\u76ee\u8d23\u4efb\u4ebaidList", dataType="List<Integer>", required=true)
    private List<Integer> ownerIdList;
    @ApiModelProperty(value="\u63cf\u8ff0", dataType="String", required=false)
    private String description;
    @ApiModelProperty(value="\u8fd0\u884c\u72b6\u6001\uff08true\u542f\u52a8 or false\u505c\u7528\uff09", dataType="Boolean", required=true)
    private Boolean running;
    @ApiModelProperty(value="\u4f7f\u7528\u90e8\u95e8id", dataType="Integer", required=true)
    private Integer deptId;

    public Integer getId() {
        return this.id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public List<Integer> getUserIdList() {
        return this.userIdList;
    }

    public List<Integer> getOwnerIdList() {
        return this.ownerIdList;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getRunning() {
        return this.running;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }

    public void setOwnerIdList(List<Integer> ownerIdList) {
        this.ownerIdList = ownerIdList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectSaveDTO)) {
            return false;
        }
        ProjectSaveDTO other = (ProjectSaveDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Boolean this$running = this.getRunning();
        Boolean other$running = other.getRunning();
        if (this$running == null ? other$running != null : !((Object)this$running).equals(other$running)) {
            return false;
        }
        Integer this$deptId = this.getDeptId();
        Integer other$deptId = other.getDeptId();
        if (this$deptId == null ? other$deptId != null : !((Object)this$deptId).equals(other$deptId)) {
            return false;
        }
        String this$projectName = this.getProjectName();
        String other$projectName = other.getProjectName();
        if (this$projectName == null ? other$projectName != null : !this$projectName.equals(other$projectName)) {
            return false;
        }
        List<Integer> this$userIdList = this.getUserIdList();
        List<Integer> other$userIdList = other.getUserIdList();
        if (this$userIdList == null ? other$userIdList != null : !((Object)this$userIdList).equals(other$userIdList)) {
            return false;
        }
        List<Integer> this$ownerIdList = this.getOwnerIdList();
        List<Integer> other$ownerIdList = other.getOwnerIdList();
        if (this$ownerIdList == null ? other$ownerIdList != null : !((Object)this$ownerIdList).equals(other$ownerIdList)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ProjectSaveDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Boolean $running = this.getRunning();
        result = result * 59 + ($running == null ? 43 : ((Object)$running).hashCode());
        Integer $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        String $projectName = this.getProjectName();
        result = result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
        List<Integer> $userIdList = this.getUserIdList();
        result = result * 59 + ($userIdList == null ? 43 : ((Object)$userIdList).hashCode());
        List<Integer> $ownerIdList = this.getOwnerIdList();
        result = result * 59 + ($ownerIdList == null ? 43 : ((Object)$ownerIdList).hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String toString() {
        return "ProjectSaveDTO(id=" + this.getId() + ", projectName=" + this.getProjectName() + ", userIdList=" + this.getUserIdList() + ", ownerIdList=" + this.getOwnerIdList() + ", description=" + this.getDescription() + ", running=" + this.getRunning() + ", deptId=" + this.getDeptId() + ")";
    }
}

