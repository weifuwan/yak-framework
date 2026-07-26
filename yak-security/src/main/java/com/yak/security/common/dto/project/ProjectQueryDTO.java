/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.dto.project;

import com.yak.security.common.dto.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u9879\u76ee\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class ProjectQueryDTO
extends PageParamDTO {
    @ApiModelProperty(value="\u9879\u76ee\u540d\uff08\u6a21\u7cca\uff09", dataType="String", required=false)
    private String projectName;
    @ApiModelProperty(value="\u9879\u76ee\u7f16\u53f7\uff08\u7cbe\u786e\uff09", dataType="String", required=false)
    private String projectCode;
    @ApiModelProperty(value="\u8d1f\u8d23\u4eba\u7684\u8d26\u53f7\u540d\uff08\u6a21\u7cca\uff09", dataType="String", required=false)
    private String chargeUsername;
    @ApiModelProperty(value="\u6240\u5c5e\u90e8\u95e8id", dataType="Integer", required=false)
    private Integer deptId;
    @ApiModelProperty(value="\u9879\u76ee\u8fd0\u884c\u72b6\u6001\uff08\u4e3anull\uff0c\u8868\u793a\u6240\u6709\u72b6\u6001\uff09", dataType="Boolean", required=false)
    private Boolean running;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProjectQueryDTO)) {
            return false;
        }
        ProjectQueryDTO other = (ProjectQueryDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$deptId = this.getDeptId();
        Integer other$deptId = other.getDeptId();
        if (this$deptId == null ? other$deptId != null : !((Object)this$deptId).equals(other$deptId)) {
            return false;
        }
        Boolean this$running = this.getRunning();
        Boolean other$running = other.getRunning();
        if (this$running == null ? other$running != null : !((Object)this$running).equals(other$running)) {
            return false;
        }
        String this$projectName = this.getProjectName();
        String other$projectName = other.getProjectName();
        if (this$projectName == null ? other$projectName != null : !this$projectName.equals(other$projectName)) {
            return false;
        }
        String this$projectCode = this.getProjectCode();
        String other$projectCode = other.getProjectCode();
        if (this$projectCode == null ? other$projectCode != null : !this$projectCode.equals(other$projectCode)) {
            return false;
        }
        String this$chargeUsername = this.getChargeUsername();
        String other$chargeUsername = other.getChargeUsername();
        return !(this$chargeUsername == null ? other$chargeUsername != null : !this$chargeUsername.equals(other$chargeUsername));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof ProjectQueryDTO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        Boolean $running = this.getRunning();
        result = result * 59 + ($running == null ? 43 : ((Object)$running).hashCode());
        String $projectName = this.getProjectName();
        result = result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
        String $projectCode = this.getProjectCode();
        result = result * 59 + ($projectCode == null ? 43 : $projectCode.hashCode());
        String $chargeUsername = this.getChargeUsername();
        result = result * 59 + ($chargeUsername == null ? 43 : $chargeUsername.hashCode());
        return result;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public String getChargeUsername() {
        return this.chargeUsername;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public Boolean getRunning() {
        return this.running;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public void setChargeUsername(String chargeUsername) {
        this.chargeUsername = chargeUsername;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    @Override
    public String toString() {
        return "ProjectQueryDTO(projectName=" + this.getProjectName() + ", projectCode=" + this.getProjectCode() + ", chargeUsername=" + this.getChargeUsername() + ", deptId=" + this.getDeptId() + ", running=" + this.getRunning() + ")";
    }
}

