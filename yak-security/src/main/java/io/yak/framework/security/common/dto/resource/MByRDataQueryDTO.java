package io.yak.framework.security.common.dto.resource;
public class MByRDataQueryDTO {
  private Integer projectId;
  private Integer resourceTypeId;
  private Integer resourceId;
  private Integer controlLevel;
  private Boolean batch;

  public Integer getProjectId() { return this.projectId; }

  public Integer getResourceTypeId() { return this.resourceTypeId; }

  public Integer getResourceId() { return this.resourceId; }

  public Integer getControlLevel() { return this.controlLevel; }

  public Boolean getBatch() { return this.batch; }

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

  public void setBatch(Boolean batch) { this.batch = batch; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MByRDataQueryDTO)) {
      return false;
    }
    MByRDataQueryDTO other = (MByRDataQueryDTO)o;
    if (!other.canEqual(this)) {
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
    if (this$controlLevel == null
            ? other$controlLevel != null
            : !((Object)this$controlLevel).equals(other$controlLevel)) {
      return false;
    }
    Boolean this$batch = this.getBatch();
    Boolean other$batch = other.getBatch();
    return !(this$batch == null ? other$batch != null
                                : !((Object)this$batch).equals(other$batch));
  }

  protected boolean canEqual(Object other) {
    return other instanceof MByRDataQueryDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
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
    Boolean $batch = this.getBatch();
    result = result * 59 + ($batch == null ? 43 : ((Object)$batch).hashCode());
    return result;
  }

  public String toString() {
    return "MByRDataQueryDTO(projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", resourceId=" + this.getResourceId() +
        ", controlLevel=" + this.getControlLevel() +
        ", batch=" + this.getBatch() + ")";
  }
}
