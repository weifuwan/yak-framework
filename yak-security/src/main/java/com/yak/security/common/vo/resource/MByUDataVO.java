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

@ApiModel(description="\u6309\u7528\u6237\u7ba1\u7406/\u5206\u914d\u8d44\u6e90/\u6570\u636e\u5217\u8868")
public class MByUDataVO {
    @ApiModelProperty(value="\u6570\u636eid\uff08\u9879\u76eeid\u3001\u8d44\u6e90\u7c7b\u522bid\u3001\u8d44\u6e90id\uff09", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u6570\u636e\u540d\uff08\u9879\u76ee\u540d\u3001\u8d44\u6e90\u7c7b\u522b\u540d\u3001\u8d44\u6e90\u540d\uff09", dataType="String", required=false)
    private String name;
    @ApiModelProperty(value="\u62e5\u6709\u7ea7\u522b\uff080 \u4e0d\u62e5\u6709\u30011 \u534a\u62e5\u6709\u30012 \u5168\u62e5\u6709\uff09", dataType="Integer", required=false)
    private Integer hasLevel;

    public MByUDataVO() {
    }

    public MByUDataVO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getHasLevel() {
        return this.hasLevel;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHasLevel(Integer hasLevel) {
        this.hasLevel = hasLevel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MByUDataVO)) {
            return false;
        }
        MByUDataVO other = (MByUDataVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$hasLevel = this.getHasLevel();
        Integer other$hasLevel = other.getHasLevel();
        if (this$hasLevel == null ? other$hasLevel != null : !((Object)this$hasLevel).equals(other$hasLevel)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MByUDataVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $hasLevel = this.getHasLevel();
        result = result * 59 + ($hasLevel == null ? 43 : ((Object)$hasLevel).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "MByUDataVO(id=" + this.getId() + ", name=" + this.getName() + ", hasLevel=" + this.getHasLevel() + ")";
    }
}

