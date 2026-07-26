/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u89d2\u8272\u5206\u914d\u4fe1\u606f")
public class RoleAssignDTO {
    @ApiModelProperty(value="\u89d2\u8272id\u6216\u7528\u6237id", dataType="Integer", required=true)
    private Integer id;
    @ApiModelProperty(value="\u89d2\u8272idList\u6216\u7528\u6237idList", dataType="List<Integer>", required=true)
    private List<Integer> idList;
    @ApiModelProperty(value="true\uff1aN\u4e2a\u89d2\u8272\u5206\u914d\u7ed91\u4e2a\u7528\u6237\u3001false\uff1a1\u4e2a\u89d2\u8272\u5206\u914d\u7ed9N\u4e2a\u7528\u6237", dataType="Boolean", required=true)
    private Boolean flag;

    public Integer getId() {
        return this.id;
    }

    public List<Integer> getIdList() {
        return this.idList;
    }

    public Boolean getFlag() {
        return this.flag;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoleAssignDTO)) {
            return false;
        }
        RoleAssignDTO other = (RoleAssignDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Boolean this$flag = this.getFlag();
        Boolean other$flag = other.getFlag();
        if (this$flag == null ? other$flag != null : !((Object)this$flag).equals(other$flag)) {
            return false;
        }
        List<Integer> this$idList = this.getIdList();
        List<Integer> other$idList = other.getIdList();
        return !(this$idList == null ? other$idList != null : !((Object)this$idList).equals(other$idList));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RoleAssignDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Boolean $flag = this.getFlag();
        result = result * 59 + ($flag == null ? 43 : ((Object)$flag).hashCode());
        List<Integer> $idList = this.getIdList();
        result = result * 59 + ($idList == null ? 43 : ((Object)$idList).hashCode());
        return result;
    }

    public String toString() {
        return "RoleAssignDTO(id=" + this.getId() + ", idList=" + this.getIdList() + ", flag=" + this.getFlag() + ")";
    }
}

