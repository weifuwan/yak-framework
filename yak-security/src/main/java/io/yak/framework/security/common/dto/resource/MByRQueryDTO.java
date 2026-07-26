package io.yak.framework.security.common.dto.resource;

import io.yak.framework.security.common.dto.PageParamDTO;
public class MByRQueryDTO extends PageParamDTO {
  private Long projectId;
  private Long resourceTypeId;
  private Integer showLevel;
  private String name;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MByRQueryDTO)) {
      return false;
    }
    MByRQueryDTO other = (MByRQueryDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
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
    Integer this$showLevel = this.getShowLevel();
    Integer other$showLevel = other.getShowLevel();
    if (this$showLevel == null
            ? other$showLevel != null
            : !((Object)this$showLevel).equals(other$showLevel)) {
      return false;
    }
    String this$name = this.getName();
    String other$name = other.getName();
    return !(this$name == null ? other$name != null
                               : !this$name.equals(other$name));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof MByRQueryDTO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Long $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    Long $resourceTypeId = this.getResourceTypeId();
    result =
        result * 59 +
        ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
    Integer $showLevel = this.getShowLevel();
    result = result * 59 +
             ($showLevel == null ? 43 : ((Object)$showLevel).hashCode());
    String $name = this.getName();
    result = result * 59 + ($name == null ? 43 : $name.hashCode());
    return result;
  }

  public Long getProjectId() { return this.projectId; }

  public Long getResourceTypeId() { return this.resourceTypeId; }

  public Integer getShowLevel() { return this.showLevel; }

  public String getName() { return this.name; }

  public void setProjectId(Long projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Long resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setShowLevel(Integer showLevel) { this.showLevel = showLevel; }

  public void setName(String name) { this.name = name; }

  @Override
  public String toString() {
    return "MByRQueryDTO(projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", showLevel=" + this.getShowLevel() + ", name=" + this.getName() +
        ")";
  }
}
