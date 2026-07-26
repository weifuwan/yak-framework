package io.yak.framework.security.common.dto.resource;

import lombok.Data;

/**
 * 用户资源查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class UserResourceQueryDTO {
  /** 管控级别。 */
  private int controlLevel;
  /** 项目标识。 */
  private Long projectId;
  /** 资源类型标识。 */
  private Long resourceTypeId;
  /** 资源标识。 */
  private Long resourceId;

  public UserResourceQueryDTO(int controlLevel, Long projectId,
                              Long resourceTypeId, Long resourceId) {
    this.controlLevel = controlLevel;
    this.projectId = projectId;
    this.resourceTypeId = resourceTypeId;
    this.resourceId = resourceId;
  }

  public UserResourceQueryDTO(int controlLevel, Long projectId,
                              Long resourceTypeId) {
    this.controlLevel = controlLevel;
    this.projectId = projectId;
    this.resourceTypeId = resourceTypeId;
    this.resourceId = null;
  }

  public UserResourceQueryDTO(int controlLevel, Long projectId) {
    this.controlLevel = controlLevel;
    this.projectId = projectId;
    this.resourceTypeId = null;
    this.resourceId = null;
  }

  public static UserResourceQueryDTO getOpenViewPermissionControlQueryEntity() {
    return new UserResourceQueryDTO(0, 0, 0, 0);
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
    return !(this$resourceId == null
                 ? other$resourceId != null
                 : !((Object)this$resourceId).equals(other$resourceId));
  }

  protected boolean canEqual(Object other) {
    return other instanceof UserResourceQueryDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    result = result * 59 + this.getControlLevel();
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
    return result;
  }

  public String toString() {
    return "UserResourceQueryDTO(controlLevel=" + this.getControlLevel() +
        ", projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", resourceId=" + this.getResourceId() + ")";
  }
}
