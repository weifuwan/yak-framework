package io.yak.framework.security.common.dto.resource;
/**
 * 按用户查询授权数据的数据传输对象。
 *
 * @author weifuwan
 */
public class MByUDataQueryDTO {
  /** 用户标识。 */
  private Long userId;
  /** 项目标识。 */
  private Long projectId;
  /** 资源类型标识。 */
  private Long resourceTypeId;
  /** 展示级别。 */
  private Integer showLevel;
  /** 管控级别。 */
  private Integer controlLevel;
  /** 是否批量查询。 */
  private Boolean batch;

  public Long getUserId() { return this.userId; }

  public Long getProjectId() { return this.projectId; }

  public Long getResourceTypeId() { return this.resourceTypeId; }

  public Integer getShowLevel() { return this.showLevel; }

  public Integer getControlLevel() { return this.controlLevel; }

  public Boolean getBatch() { return this.batch; }

  public void setUserId(Long userId) { this.userId = userId; }

  public void setProjectId(Long projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Long resourceTypeId) {
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
    Long this$userId = this.getUserId();
    Long other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null
                            : !((Object)this$userId).equals(other$userId)) {
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
    Long $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
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
