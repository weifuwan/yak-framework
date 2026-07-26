/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.common.entity;

public class ResourceType {
    private Integer id;
    private String typeName;

    public Integer getId() {
        return this.id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResourceType)) {
            return false;
        }
        ResourceType other = (ResourceType)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$typeName = this.getTypeName();
        String other$typeName = other.getTypeName();
        return !(this$typeName == null ? other$typeName != null : !this$typeName.equals(other$typeName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourceType;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $typeName = this.getTypeName();
        result = result * 59 + ($typeName == null ? 43 : $typeName.hashCode());
        return result;
    }

    public String toString() {
        return "ResourceType(id=" + this.getId() + ", typeName=" + this.getTypeName() + ")";
    }
}

