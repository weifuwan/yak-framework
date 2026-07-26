/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.common.dto.resource;

public class UserResourceQueryDTO {
    private int controlLevel;
    private Integer projectId;
    private Integer resourceTypeId;
    private Integer resourceId;

    public UserResourceQueryDTO(int controlLevel, Integer projectId, Integer resourceTypeId, Integer resourceId) {
        this.controlLevel = controlLevel;
        this.projectId = projectId;
        this.resourceTypeId = resourceTypeId;
        this.resourceId = resourceId;
    }

    public UserResourceQueryDTO(int controlLevel, Integer projectId, Integer resourceTypeId) {
        this.controlLevel = controlLevel;
        this.projectId = projectId;
        this.resourceTypeId = resourceTypeId;
        this.resourceId = null;
    }

    public UserResourceQueryDTO(int controlLevel, Integer projectId) {
        this.controlLevel = controlLevel;
        this.projectId = projectId;
        this.resourceTypeId = null;
        this.resourceId = null;
    }

    public static UserResourceQueryDTO getOpenViewPermissionControlQueryEntity() {
        return new UserResourceQueryDTO(0, 0, 0, 0);
    }

    public int getControlLevel() {
        return this.controlLevel;
    }

    public Integer getProjectId() {
        return this.projectId;
    }

    public Integer getResourceTypeId() {
        return this.resourceTypeId;
    }

    public Integer getResourceId() {
        return this.resourceId;
    }

    public void setControlLevel(int controlLevel) {
        this.controlLevel = controlLevel;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserResourceQueryDTO)) {
            return false;
        }
        UserResourceQueryDTO other = (UserResourceQueryDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getControlLevel() != other.getControlLevel()) {
            return false;
        }
        Integer this$projectId = this.getProjectId();
        Integer other$projectId = other.getProjectId();
        if (this$projectId == null ? other$projectId != null : !((Object)this$projectId).equals(other$projectId)) {
            return false;
        }
        Integer this$resourceTypeId = this.getResourceTypeId();
        Integer other$resourceTypeId = other.getResourceTypeId();
        if (this$resourceTypeId == null ? other$resourceTypeId != null : !((Object)this$resourceTypeId).equals(other$resourceTypeId)) {
            return false;
        }
        Integer this$resourceId = this.getResourceId();
        Integer other$resourceId = other.getResourceId();
        return !(this$resourceId == null ? other$resourceId != null : !((Object)this$resourceId).equals(other$resourceId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserResourceQueryDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getControlLevel();
        Integer $projectId = this.getProjectId();
        result = result * 59 + ($projectId == null ? 43 : ((Object)$projectId).hashCode());
        Integer $resourceTypeId = this.getResourceTypeId();
        result = result * 59 + ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
        Integer $resourceId = this.getResourceId();
        result = result * 59 + ($resourceId == null ? 43 : ((Object)$resourceId).hashCode());
        return result;
    }

    public String toString() {
        return "UserResourceQueryDTO(controlLevel=" + this.getControlLevel() + ", projectId=" + this.getProjectId() + ", resourceTypeId=" + this.getResourceTypeId() + ", resourceId=" + this.getResourceId() + ")";
    }
}

