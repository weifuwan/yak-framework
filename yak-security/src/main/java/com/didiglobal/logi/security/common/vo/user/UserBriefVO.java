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
import java.util.List;

@ApiModel(description="\u7528\u6237\u7b80\u8981\u4fe1\u606f")
public class UserBriefVO {
    @ApiModelProperty(value="\u7528\u6237id", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7", dataType="String", required=false)
    private String userName;
    @ApiModelProperty(value="\u771f\u5b9e\u59d3\u540d", dataType="String", required=false)
    private String realName;
    @ApiModelProperty(value="\u90e8\u95e8id", dataType="Integer", required=false)
    private Integer deptId;
    @ApiModelProperty(value="\u7535\u8bdd", dataType="String", required=false)
    private String phone;
    @ApiModelProperty(value="\u90ae\u7bb1", dataType="String", required=false)
    private String email;
    @ApiModelProperty(value="\u89d2\u8272\u4fe1\u606f", dataType="List<String>", required=false)
    private List<String> roleList;

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

    public String getPhone() {
        return this.phone;
    }

    public String getEmail() {
        return this.email;
    }

    public List<String> getRoleList() {
        return this.roleList;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserBriefVO)) {
            return false;
        }
        UserBriefVO other = (UserBriefVO)o;
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
        if (this$realName == null ? other$realName != null : !this$realName.equals(other$realName)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        List<String> this$roleList = this.getRoleList();
        List<String> other$roleList = other.getRoleList();
        return !(this$roleList == null ? other$roleList != null : !((Object)this$roleList).equals(other$roleList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserBriefVO;
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
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        List<String> $roleList = this.getRoleList();
        result = result * 59 + ($roleList == null ? 43 : ((Object)$roleList).hashCode());
        return result;
    }

    public String toString() {
        return "UserBriefVO(id=" + this.getId() + ", userName=" + this.getUserName() + ", realName=" + this.getRealName() + ", deptId=" + this.getDeptId() + ", phone=" + this.getPhone() + ", email=" + this.getEmail() + ", roleList=" + this.getRoleList() + ")";
    }
}

