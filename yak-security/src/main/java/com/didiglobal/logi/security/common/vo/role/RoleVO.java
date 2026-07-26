/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.vo.role;

import com.didiglobal.logi.security.common.vo.permission.PermissionTreeVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

@ApiModel(description="\u89d2\u8272\u4fe1\u606f")
public class RoleVO {
    @ApiModelProperty(value="\u89d2\u8272id", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u89d2\u8272\u540d", dataType="String", required=false)
    private String roleName;
    @ApiModelProperty(value="\u89d2\u8272\u7f16\u53f7", dataType="String", required=false)
    private String roleCode;
    @ApiModelProperty(value="\u89d2\u8272\u63cf\u8ff0", dataType="String", required=false)
    private String description;
    @ApiModelProperty(value="\u6388\u6743\u7528\u6237\u6570\uff08\u62e5\u6709\u8be5\u89d2\u8272\u7684\u7528\u6237\u6570\uff09", dataType="Integer", required=false)
    private Integer authedUserCnt;
    @ApiModelProperty(value="\u6388\u6743\u7528\u6237\u5217\u8868\uff09", dataType="List", required=false)
    private List<String> authedUsers;
    @ApiModelProperty(value="\u6700\u540e\u4fee\u6539\u8005\uff08\u7528\u6237\u8d26\u53f7\uff09", dataType="String", required=false)
    private String lastReviser;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4\uff08\u65f6\u95f4\u6233ms\uff09", dataType="Long", required=false)
    private Date createTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4\uff08\u65f6\u95f4\u6233ms\uff09", dataType="Long", required=false)
    private Date updateTime;
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value="\u89d2\u8272\u62e5\u6709\u7684\u6743\u9650\uff08\u6811\uff09", dataType="PermissionTreeVO", required=false)
    private PermissionTreeVO permissionTreeVO;

    public Integer getId() {
        return this.id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getAuthedUserCnt() {
        return this.authedUserCnt;
    }

    public List<String> getAuthedUsers() {
        return this.authedUsers;
    }

    public String getLastReviser() {
        return this.lastReviser;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public PermissionTreeVO getPermissionTreeVO() {
        return this.permissionTreeVO;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthedUserCnt(Integer authedUserCnt) {
        this.authedUserCnt = authedUserCnt;
    }

    public void setAuthedUsers(List<String> authedUsers) {
        this.authedUsers = authedUsers;
    }

    public void setLastReviser(String lastReviser) {
        this.lastReviser = lastReviser;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setPermissionTreeVO(PermissionTreeVO permissionTreeVO) {
        this.permissionTreeVO = permissionTreeVO;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoleVO)) {
            return false;
        }
        RoleVO other = (RoleVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$authedUserCnt = this.getAuthedUserCnt();
        Integer other$authedUserCnt = other.getAuthedUserCnt();
        if (this$authedUserCnt == null ? other$authedUserCnt != null : !((Object)this$authedUserCnt).equals(other$authedUserCnt)) {
            return false;
        }
        String this$roleName = this.getRoleName();
        String other$roleName = other.getRoleName();
        if (this$roleName == null ? other$roleName != null : !this$roleName.equals(other$roleName)) {
            return false;
        }
        String this$roleCode = this.getRoleCode();
        String other$roleCode = other.getRoleCode();
        if (this$roleCode == null ? other$roleCode != null : !this$roleCode.equals(other$roleCode)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        List<String> this$authedUsers = this.getAuthedUsers();
        List<String> other$authedUsers = other.getAuthedUsers();
        if (this$authedUsers == null ? other$authedUsers != null : !((Object)this$authedUsers).equals(other$authedUsers)) {
            return false;
        }
        String this$lastReviser = this.getLastReviser();
        String other$lastReviser = other.getLastReviser();
        if (this$lastReviser == null ? other$lastReviser != null : !this$lastReviser.equals(other$lastReviser)) {
            return false;
        }
        Date this$createTime = this.getCreateTime();
        Date other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Date this$updateTime = this.getUpdateTime();
        Date other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        PermissionTreeVO this$permissionTreeVO = this.getPermissionTreeVO();
        PermissionTreeVO other$permissionTreeVO = other.getPermissionTreeVO();
        return !(this$permissionTreeVO == null ? other$permissionTreeVO != null : !((Object)this$permissionTreeVO).equals(other$permissionTreeVO));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RoleVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $authedUserCnt = this.getAuthedUserCnt();
        result = result * 59 + ($authedUserCnt == null ? 43 : ((Object)$authedUserCnt).hashCode());
        String $roleName = this.getRoleName();
        result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
        String $roleCode = this.getRoleCode();
        result = result * 59 + ($roleCode == null ? 43 : $roleCode.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        List<String> $authedUsers = this.getAuthedUsers();
        result = result * 59 + ($authedUsers == null ? 43 : ((Object)$authedUsers).hashCode());
        String $lastReviser = this.getLastReviser();
        result = result * 59 + ($lastReviser == null ? 43 : $lastReviser.hashCode());
        Date $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Date $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        PermissionTreeVO $permissionTreeVO = this.getPermissionTreeVO();
        result = result * 59 + ($permissionTreeVO == null ? 43 : ((Object)$permissionTreeVO).hashCode());
        return result;
    }

    public String toString() {
        return "RoleVO(id=" + this.getId() + ", roleName=" + this.getRoleName() + ", roleCode=" + this.getRoleCode() + ", description=" + this.getDescription() + ", authedUserCnt=" + this.getAuthedUserCnt() + ", authedUsers=" + this.getAuthedUsers() + ", lastReviser=" + this.getLastReviser() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", permissionTreeVO=" + this.getPermissionTreeVO() + ")";
    }
}

