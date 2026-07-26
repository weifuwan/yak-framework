/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.dto.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

@ApiModel(description="\u6743\u9650\u5bfc\u5165\u4fe1\u606f")
public class PermissionDTO {
    @ApiModelProperty(value="\u6743\u9650\u540d", dataType="String", required=true)
    private String permissionName;
    @ApiModelProperty(value="\u6743\u9650\u63cf\u8ff0", dataType="String", required=false)
    private String description;
    @ApiModelProperty(value="\u5b50\u6743\u9650", dataType="List<PermissionDTO>", required=false)
    private List<PermissionDTO> childPermissionDTOList;

    public List<PermissionDTO> getChildPermissionDTOList() {
        if (this.childPermissionDTOList == null) {
            this.childPermissionDTOList = new ArrayList<PermissionDTO>();
        }
        return this.childPermissionDTOList;
    }

    public PermissionDTO() {
    }

    public PermissionDTO(String permissionName, String description) {
        this.permissionName = permissionName;
        this.description = description;
    }

    public PermissionDTO(String permissionName) {
        this.permissionName = permissionName;
        this.description = permissionName;
    }

    public String getPermissionName() {
        return this.permissionName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChildPermissionDTOList(List<PermissionDTO> childPermissionDTOList) {
        this.childPermissionDTOList = childPermissionDTOList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PermissionDTO)) {
            return false;
        }
        PermissionDTO other = (PermissionDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$permissionName = this.getPermissionName();
        String other$permissionName = other.getPermissionName();
        if (this$permissionName == null ? other$permissionName != null : !this$permissionName.equals(other$permissionName)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        List<PermissionDTO> this$childPermissionDTOList = this.getChildPermissionDTOList();
        List<PermissionDTO> other$childPermissionDTOList = other.getChildPermissionDTOList();
        return !(this$childPermissionDTOList == null ? other$childPermissionDTOList != null : !((Object)this$childPermissionDTOList).equals(other$childPermissionDTOList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PermissionDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $permissionName = this.getPermissionName();
        result = result * 59 + ($permissionName == null ? 43 : $permissionName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        List<PermissionDTO> $childPermissionDTOList = this.getChildPermissionDTOList();
        result = result * 59 + ($childPermissionDTOList == null ? 43 : ((Object)$childPermissionDTOList).hashCode());
        return result;
    }

    public String toString() {
        return "PermissionDTO(permissionName=" + this.getPermissionName() + ", description=" + this.getDescription() + ", childPermissionDTOList=" + this.getChildPermissionDTOList() + ")";
    }
}

