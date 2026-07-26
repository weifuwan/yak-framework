/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package com.didiglobal.logi.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.didiglobal.logi.security.common.po.BasePO;

@TableName(value="logi_security_user_role")
public class UserRolePO
extends BasePO {
    private Integer userId;
    private Integer roleId;

    public UserRolePO() {
    }

    public UserRolePO(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserRolePO)) {
            return false;
        }
        UserRolePO other = (UserRolePO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$roleId = this.getRoleId();
        Integer other$roleId = other.getRoleId();
        return !(this$roleId == null ? other$roleId != null : !((Object)this$roleId).equals(other$roleId));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof UserRolePO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        return result;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRolePO(userId=" + this.getUserId() + ", roleId=" + this.getRoleId() + ")";
    }
}

