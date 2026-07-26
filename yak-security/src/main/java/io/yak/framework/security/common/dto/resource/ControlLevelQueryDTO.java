package io.yak.framework.security.common.dto.resource;

import lombok.Data;
/**
 * 管控级别查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class ControlLevelQueryDTO {
  /** 用户标识。 */
  private Long userId;
  /** 项目标识。 */
  private Long projectId;
  /** 资源类型标识。 */
  private Long resourceTypeId;
  /** 资源标识。 */
  private Long resourceId;

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ControlLevelQueryDTO)) {
      return false;
    }
    ControlLevelQueryDTO other = (ControlLevelQueryDTO)o;
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
    Long this$resourceId = this.getResourceId();
    Long other$resourceId = other.getResourceId();
    return !(this$resourceId == null
                 ? other$resourceId != null
                 : !((Object)this$resourceId).equals(other$resourceId));
  }

  protected boolean canEqual(Object other) {
    return other instanceof ControlLevelQueryDTO;
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
    Long $resourceId = this.getResourceId();
    result = result * 59 +
             ($resourceId == null ? 43 : ((Object)$resourceId).hashCode());
    return result;
  }

  public String toString() {
    return "ControlLevelQueryDTO(userId=" + this.getUserId() +
        ", projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", resourceId=" + this.getResourceId() + ")";
  }
}
