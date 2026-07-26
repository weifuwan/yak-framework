/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u7528\u6237\u4fe1\u606f")
public class UserDTO {
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7", dataType="Integer", required=true)
    private String userName;
    @ApiModelProperty(value="\u7528\u6237\u5bc6\u7801", dataType="Integer", required=false)
    private String pw;
    @ApiModelProperty(value="\u7528\u6237\u771f\u5b9e\u59d3\u540d", dataType="Integer", required=false)
    private String realName;
    @ApiModelProperty(value="\u7535\u8bdd", dataType="String", required=false)
    private String phone;
    @ApiModelProperty(value="\u90ae\u7bb1", dataType="String", required=false)
    private String email;
    @ApiModelProperty(value="\u7528\u6237\u89d2\u8272id", dataType="Integer", required=false)
    private List<Integer> roleIds;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserDTO)) {
            return false;
        }
        UserDTO other = (UserDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$pw = this.getPw();
        String other$pw = other.getPw();
        if (this$pw == null ? other$pw != null : !this$pw.equals(other$pw)) {
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
        List<Integer> this$roleIds = this.getRoleIds();
        List<Integer> other$roleIds = other.getRoleIds();
        return !(this$roleIds == null ? other$roleIds != null : !((Object)this$roleIds).equals(other$roleIds));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $pw = this.getPw();
        result = result * 59 + ($pw == null ? 43 : $pw.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        List<Integer> $roleIds = this.getRoleIds();
        result = result * 59 + ($roleIds == null ? 43 : ((Object)$roleIds).hashCode());
        return result;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPw() {
        return this.pw;
    }

    public String getRealName() {
        return this.realName;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getEmail() {
        return this.email;
    }

    public List<Integer> getRoleIds() {
        return this.roleIds;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public String toString() {
        return "UserDTO(userName=" + this.getUserName() + ", pw=" + this.getPw() + ", realName=" + this.getRealName() + ", phone=" + this.getPhone() + ", email=" + this.getEmail() + ", roleIds=" + this.getRoleIds() + ")";
    }
}

