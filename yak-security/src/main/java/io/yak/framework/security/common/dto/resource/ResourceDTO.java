package io.yak.framework.security.common.dto.resource;

public class ResourceDTO {
  private Long resourceId;
  private String resourceName;
  private Long projectId;
  private Long resourceTypeId;

  public ResourceDTO() {}

  public ResourceDTO(Long projectId, Long resourceTypeId,
                     Long resourceId) {
    this.projectId = projectId;
    this.resourceTypeId = resourceTypeId;
    this.resourceId = resourceId;
  }

  public Long getResourceId() { return this.resourceId; }

  public String getResourceName() { return this.resourceName; }

  public Long getProjectId() { return this.projectId; }

  public Long getResourceTypeId() { return this.resourceTypeId; }

  public void setResourceId(Long resourceId) {
    this.resourceId = resourceId;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public void setProjectId(Long projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Long resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ResourceDTO)) {
      return false;
    }
    ResourceDTO other = (ResourceDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$resourceId = this.getResourceId();
    Long other$resourceId = other.getResourceId();
    if (this$resourceId == null
            ? other$resourceId != null
            : !((Object)this$resourceId).equals(other$resourceId)) {
      return false;
    }
    Long this$projectId = this.getProjectId();
    Long other$projectId = other.getProjectId();
    if (this$projectId == null
            ? other$projectId != null
            : !((Object)this$projectId).equals(other$projectId)) {
      return false;
    }
    Long this$resourceTypeId = this.getResourceTypeId();
    Long other$resourceTypeId = other.getResourceTypeId();
    if (this$resourceTypeId == null
            ? other$resourceTypeId != null
            : !((Object)this$resourceTypeId).equals(other$resourceTypeId)) {
      return false;
    }
    String this$resourceName = this.getResourceName();
    String other$resourceName = other.getResourceName();
    return !(this$resourceName == null
                 ? other$resourceName != null
                 : !this$resourceName.equals(other$resourceName));
  }

  protected boolean canEqual(Object other) {
    return other instanceof ResourceDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $resourceId = this.getResourceId();
    result = result * 59 +
             ($resourceId == null ? 43 : ((Object)$resourceId).hashCode());
    Long $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    Long $resourceTypeId = this.getResourceTypeId();
    result =
        result * 59 +
        ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
    String $resourceName = this.getResourceName();
    result =
        result * 59 + ($resourceName == null ? 43 : $resourceName.hashCode());
    return result;
  }

  public String toString() {
    return "ResourceDTO(resourceId=" + this.getResourceId() +
        ", resourceName=" + this.getResourceName() +
        ", projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() + ")";
  }
}
