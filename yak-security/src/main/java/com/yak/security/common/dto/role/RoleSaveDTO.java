/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u89d2\u8272\u6dfb\u52a0\u6216\u66f4\u65b0\u4fe1\u606f")
public class RoleSaveDTO {
    @ApiModelProperty(value="\u89d2\u8272id\uff08\u66f4\u65b0\u64cd\u4f5c\u5fc5\u5907\uff09", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u89d2\u8272\u540d", dataType="String", required=true)
    private String roleName;
    @ApiModelProperty(value="\u89d2\u8272\u63cf\u8ff0", dataType="String", required=true)
    private String description;
    @ApiModelProperty(value="\u89d2\u8272\u62e5\u6709\u7684\u6743\u9650idList\uff08\u89d2\u8272\u6743\u9650\u4e0d\u53ef\u4e3a\u7a7a\uff09", dataType="String", required=true)
    private List<Integer> permissionIdList;

    public Integer getId() {
        return this.id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Integer> getPermissionIdList() {
        return this.permissionIdList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPermissionIdList(List<Integer> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoleSaveDTO)) {
            return false;
        }
        RoleSaveDTO other = (RoleSaveDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$roleName = this.getRoleName();
        String other$roleName = other.getRoleName();
        if (this$roleName == null ? other$roleName != null : !this$roleName.equals(other$roleName)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        List<Integer> this$permissionIdList = this.getPermissionIdList();
        List<Integer> other$permissionIdList = other.getPermissionIdList();
        return !(this$permissionIdList == null ? other$permissionIdList != null : !((Object)this$permissionIdList).equals(other$permissionIdList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RoleSaveDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $roleName = this.getRoleName();
        result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        List<Integer> $permissionIdList = this.getPermissionIdList();
        result = result * 59 + ($permissionIdList == null ? 43 : ((Object)$permissionIdList).hashCode());
        return result;
    }

    public String toString() {
        return "RoleSaveDTO(id=" + this.getId() + ", roleName=" + this.getRoleName() + ", description=" + this.getDescription() + ", permissionIdList=" + this.getPermissionIdList() + ")";
    }
}

