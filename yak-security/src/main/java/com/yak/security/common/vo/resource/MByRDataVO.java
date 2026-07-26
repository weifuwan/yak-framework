/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.vo.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u6309\u8d44\u6e90\u7ba1\u7406/\u5206\u914d\u8d44\u6e90/\u6570\u636e\u5217\u8868")
public class MByRDataVO {
    @ApiModelProperty(value="\u7528\u6237id", dataType="Integer", required=false)
    private Integer userId;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u6237\u540d", dataType="String", required=false)
    private String userName;
    @ApiModelProperty(value="\u7528\u6237\u5b9e\u540d", dataType="String", required=false)
    private String realName;
    @ApiModelProperty(value="\u62e5\u6709\u7ea7\u522b\uff080 \u4e0d\u62e5\u6709\u30011 \u534a\u62e5\u6709\u30012 \u5168\u62e5\u6709\uff09", dataType="Integer", required=false)
    private Integer hasLevel;

    public Integer getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getRealName() {
        return this.realName;
    }

    public Integer getHasLevel() {
        return this.hasLevel;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setHasLevel(Integer hasLevel) {
        this.hasLevel = hasLevel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MByRDataVO)) {
            return false;
        }
        MByRDataVO other = (MByRDataVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$hasLevel = this.getHasLevel();
        Integer other$hasLevel = other.getHasLevel();
        if (this$hasLevel == null ? other$hasLevel != null : !((Object)this$hasLevel).equals(other$hasLevel)) {
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
        return other instanceof MByRDataVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $hasLevel = this.getHasLevel();
        result = result * 59 + ($hasLevel == null ? 43 : ((Object)$hasLevel).hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        return result;
    }

    public String toString() {
        return "MByRDataVO(userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", realName=" + this.getRealName() + ", hasLevel=" + this.getHasLevel() + ")";
    }
}

