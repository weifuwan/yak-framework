package io.yak.framework.security.common.entity;

import io.yak.framework.security.common.dto.resource.ResourceDTO;

public class UserResource {
  private Integer userId;
  private Integer projectId;
  private Integer resourceTypeId;
  private Integer resourceId;
  private Integer controlLevel;

  public UserResource(ResourceDTO resourceDTO) {
    this.projectId = resourceDTO.getProjectId();
    this.resourceTypeId = resourceDTO.getResourceTypeId();
    this.resourceId = resourceDTO.getResourceId();
  }

  public UserResource() {}

  public UserResource(Integer userId, Integer projectId, Integer resourceTypeId,
                      Integer resourceId, Integer controlLevel) {
    this.userId = userId;
    this.projectId = projectId;
    this.resourceTypeId = resourceTypeId;
    this.resourceId = resourceId;
    this.controlLevel = controlLevel;
  }

  public static UserResource getOpenViewPermissionControlEntity() {
    return new UserResource(0, 0, 0, 0, 0);
  }

  public Integer getUserId() { return this.userId; }

  public Integer getProjectId() { return this.projectId; }

  public Integer getResourceTypeId() { return this.resourceTypeId; }

  public Integer getResourceId() { return this.resourceId; }

  public Integer getControlLevel() { return this.controlLevel; }

  public void setUserId(Integer userId) { this.userId = userId; }

  public void setProjectId(Integer projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Integer resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setResourceId(Integer resourceId) {
    this.resourceId = resourceId;
  }

  public void setControlLevel(Integer controlLevel) {
    this.controlLevel = controlLevel;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserResource)) {
      return false;
    }
    UserResource other = (UserResource)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Integer this$userId = this.getUserId();
    Integer other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null
                            : !((Object)this$userId).equals(other$userId)) {
      return false;
    }
    Integer this$projectId = this.getProjectId();
    Integer other$projectId = other.getProjectId();
    if (this$projectId == null
            ? other$projectId != null
            : !((Object)this$projectId).equals(other$projectId)) {
      return false;
    }
    Integer this$resourceTypeId = this.getResourceTypeId();
    Integer other$resourceTypeId = other.getResourceTypeId();
    if (this$resourceTypeId == null
            ? other$resourceTypeId != null
            : !((Object)this$resourceTypeId).equals(other$resourceTypeId)) {
      return false;
    }
    Integer this$resourceId = this.getResourceId();
    Integer other$resourceId = other.getResourceId();
    if (this$resourceId == null
            ? other$resourceId != null
            : !((Object)this$resourceId).equals(other$resourceId)) {
      return false;
    }
    Integer this$controlLevel = this.getControlLevel();
    Integer other$controlLevel = other.getControlLevel();
    return !(this$controlLevel == null
                 ? other$controlLevel != null
                 : !((Object)this$controlLevel).equals(other$controlLevel));
  }

  protected boolean canEqual(Object other) {
    return other instanceof UserResource;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    Integer $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    Integer $resourceTypeId = this.getResourceTypeId();
    result =
        result * 59 +
        ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
    Integer $resourceId = this.getResourceId();
    result = result * 59 +
             ($resourceId == null ? 43 : ((Object)$resourceId).hashCode());
    Integer $controlLevel = this.getControlLevel();
    result = result * 59 +
             ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
    return result;
  }

  public String toString() {
    return "UserResource(userId=" + this.getUserId() +
        ", projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", resourceId=" + this.getResourceId() +
        ", controlLevel=" + this.getControlLevel() + ")";
  }
}
