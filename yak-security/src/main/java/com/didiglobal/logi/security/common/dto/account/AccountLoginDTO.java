/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u7528\u6237\u767b\u9646\u4fe1\u606f")
public class AccountLoginDTO {
    @ApiModelProperty(name="userName", value="\u7528\u6237\u767b\u5f55\u540d\uff08\u53ef\u4ee5\u662f\u7528\u6237\u540d\u767b\u5f55\u6216\u8005\u90ae\u7bb1\u767b\u5f55\uff09", dataType="String")
    private String userName;
    @ApiModelProperty(name="pw", value="\u7528\u6237\u767b\u5f55\u5bc6\u7801", dataType="String")
    private String pw;

    public String getUserName() {
        return this.userName;
    }

    public String getPw() {
        return this.pw;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccountLoginDTO)) {
            return false;
        }
        AccountLoginDTO other = (AccountLoginDTO)o;
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
        return !(this$pw == null ? other$pw != null : !this$pw.equals(other$pw));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AccountLoginDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $pw = this.getPw();
        result = result * 59 + ($pw == null ? 43 : $pw.hashCode());
        return result;
    }

    public String toString() {
        return "AccountLoginDTO(userName=" + this.getUserName() + ", pw=" + this.getPw() + ")";
    }
}

