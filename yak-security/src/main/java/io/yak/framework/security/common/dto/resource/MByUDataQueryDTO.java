package io.yak.framework.security.common.dto.resource;
public class MByUDataQueryDTO {
  private Integer userId;
  private Integer projectId;
  private Integer resourceTypeId;
  private Integer showLevel;
  private Integer controlLevel;
  private Boolean batch;

  public Integer getUserId() { return this.userId; }

  public Integer getProjectId() { return this.projectId; }

  public Integer getResourceTypeId() { return this.resourceTypeId; }

  public Integer getShowLevel() { return this.showLevel; }

  public Integer getControlLevel() { return this.controlLevel; }

  public Boolean getBatch() { return this.batch; }

  public void setUserId(Integer userId) { this.userId = userId; }

  public void setProjectId(Integer projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Integer resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setShowLevel(Integer showLevel) { this.showLevel = showLevel; }

  public void setControlLevel(Integer controlLevel) {
    this.controlLevel = controlLevel;
  }

  public void setBatch(Boolean batch) { this.batch = batch; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MByUDataQueryDTO)) {
      return false;
    }
    MByUDataQueryDTO other = (MByUDataQueryDTO)o;
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
    Integer this$showLevel = this.getShowLevel();
    Integer other$showLevel = other.getShowLevel();
    if (this$showLevel == null
            ? other$showLevel != null
            : !((Object)this$showLevel).equals(other$showLevel)) {
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
    return other instanceof MByUDataQueryDTO;
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
    Integer $showLevel = this.getShowLevel();
    result = result * 59 +
             ($showLevel == null ? 43 : ((Object)$showLevel).hashCode());
    Integer $controlLevel = this.getControlLevel();
    result = result * 59 +
             ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
    Boolean $batch = this.getBatch();
    result = result * 59 + ($batch == null ? 43 : ((Object)$batch).hashCode());
    return result;
  }

  public String toString() {
    return "MByUDataQueryDTO(userId=" + this.getUserId() +
        ", projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", showLevel=" + this.getShowLevel() +
        ", controlLevel=" + this.getControlLevel() +
        ", batch=" + this.getBatch() + ")";
  }
}
