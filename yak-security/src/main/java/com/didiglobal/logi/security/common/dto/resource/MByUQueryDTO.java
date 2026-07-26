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

@ApiModel(description="\u8d44\u6e90\u6743\u9650\u7ba1\u7406\uff08\u6309\u7528\u6237\u7ba1\u7406\u7684\u5217\u8868\u67e5\u8be2\u6761\u4ef6\uff09")
public class MByUQueryDTO
extends PageParamDTO {
    @ApiModelProperty(value="\u90e8\u95e8id", dataType="Integer", required=false)
    private Integer deptId;
    @ApiModelProperty(value="\u90e8\u95e8\u540d\uff08\u6a21\u7cca\uff09", dataType="String", required=false)
    private String deptName;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7", dataType="String", required=false)
    private String userName;
    @ApiModelProperty(value="\u7528\u6237\u5b9e\u540d", dataType="String", required=false)
    private String realName;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MByUQueryDTO)) {
            return false;
        }
        MByUQueryDTO other = (MByUQueryDTO)o;
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
        String this$deptName = this.getDeptName();
        String other$deptName = other.getDeptName();
        if (this$deptName == null ? other$deptName != null : !this$deptName.equals(other$deptName)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$realName = this.getRealName();
        String other$realName = other.getRealName();
        return !(this$realName == null ? other$realName != null : !this$realName.equals(other$realName));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof MByUQueryDTO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        String $deptName = this.getDeptName();
        result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        return result;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "MByUQueryDTO(deptId=" + this.getDeptId() + ", deptName=" + this.getDeptName() + ", userName=" + this.getUserName() + ", realName=" + this.getRealName() + ")";
    }
}

