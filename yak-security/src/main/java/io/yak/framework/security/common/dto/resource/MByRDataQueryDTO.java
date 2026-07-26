package io.yak.framework.security.common.dto.resource;

import lombok.Data;
/**
 * 按资源查询授权数据的数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class MByRDataQueryDTO {
  /** 项目标识。 */
  private Long projectId;
  /** 资源类型标识。 */
  private Long resourceTypeId;
  /** 资源标识。 */
  private Long resourceId;
  /** 管控级别。 */
  private Integer controlLevel;
  /** 是否批量查询。 */
  private Boolean batch;

  public Long getProjectId() { return this.projectId; }

  public Long getResourceTypeId() { return this.resourceTypeId; }

  public Long getResourceId() { return this.resourceId; }

  public Integer getControlLevel() { return this.controlLevel; }

  public Boolean getBatch() { return this.batch; }

  public void setProjectId(Long projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Long resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setResourceId(Long resourceId) {
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
    Long this$resourceId = this.getResourceId();
    Long other$resourceId = other.getResourceId();
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
    Long $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    Long $resourceTypeId = this.getResourceTypeId();
    result =
        result * 59 +
        ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
    Long $resourceId = this.getResourceId();
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
