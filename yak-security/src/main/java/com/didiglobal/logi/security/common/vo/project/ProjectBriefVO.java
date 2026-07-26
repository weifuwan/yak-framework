/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.vo.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u9879\u76ee\u7b80\u8981\u4fe1\u606f")
public class ProjectBriefVO {
    @ApiModelProperty(value="\u9879\u76eeid", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u9879\u76ee\u7f16\u53f7", dataType="String", required=false)
    private String projectCode;
    @ApiModelProperty(value="\u9879\u76ee\u540d", dataType="String", required=false)
    private String projectName;

    public Integer getId() {
        return this.id;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectBriefVO)) {
            return false;
        }
        ProjectBriefVO other = (ProjectBriefVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$projectCode = this.getProjectCode();
        String other$projectCode = other.getProjectCode();
        if (this$projectCode == null ? other$projectCode != null : !this$projectCode.equals(other$projectCode)) {
            return false;
        }
        String this$projectName = this.getProjectName();
        String other$projectName = other.getProjectName();
        return !(this$projectName == null ? other$projectName != null : !this$projectName.equals(other$projectName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ProjectBriefVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $projectCode = this.getProjectCode();
        result = result * 59 + ($projectCode == null ? 43 : $projectCode.hashCode());
        String $projectName = this.getProjectName();
        result = result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
        return result;
    }

    public String toString() {
        return "ProjectBriefVO(id=" + this.getId() + ", projectCode=" + this.getProjectCode() + ", projectName=" + this.getProjectName() + ")";
    }
}

