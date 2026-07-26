/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u5206\u9875\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class PageParamDTO {
    @ApiModelProperty(value="\u5f53\u524d\u9875", dataType="Integer", required=true)
    private int page = 1;
    @ApiModelProperty(value="\u6bcf\u9875\u5927\u5c0f", dataType="Integer", required=true)
    private int size = 10;

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PageParamDTO)) {
            return false;
        }
        PageParamDTO other = (PageParamDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPage() != other.getPage()) {
            return false;
        }
        return this.getSize() == other.getSize();
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageParamDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPage();
        result = result * 59 + this.getSize();
        return result;
    }

    public String toString() {
        return "PageParamDTO(page=" + this.getPage() + ", size=" + this.getSize() + ")";
    }
}

