/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u53ea\u5305\u542b\u7528\u6237")
public class UserBasicVO {
    @ApiModelProperty(value="\u7528\u6237 id", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7", dataType="String", required=false)
    private String userName;
    @ApiModelProperty(value="\u771f\u5b9e\u59d3\u540d", dataType="String", required=false)
    private String realName;
    @ApiModelProperty(value="\u90e8\u95e8 id", dataType="Integer", required=false)
    private Integer deptId;

    public Integer getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getRealName() {
        return this.realName;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserBasicVO)) {
            return false;
        }
        UserBasicVO other = (UserBasicVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$deptId = this.getDeptId();
        Integer other$deptId = other.getDeptId();
        if (this$deptId == null ? other$deptId != null : !((Object)this$deptId).equals(other$deptId)) {
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

    protected boolean canEqual(Object other) {
        return other instanceof UserBasicVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        return result;
    }

    public String toString() {
        return "UserBasicVO(id=" + this.getId() + ", userName=" + this.getUserName() + ", realName=" + this.getRealName() + ", deptId=" + this.getDeptId() + ")";
    }
}

