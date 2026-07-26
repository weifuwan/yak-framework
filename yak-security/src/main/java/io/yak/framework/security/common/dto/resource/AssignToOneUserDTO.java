package io.yak.framework.security.common.dto.resource;

import java.util.List;
/**
 * 用户资源分配数据传输对象。
 *
 * @author weifuwan
 */
public class AssignToOneUserDTO {
  /** 用户标识。 */
  private Long userId;
  /** 项目标识。 */
  private Long projectId;
  /** 资源类型标识。 */
  private Long resourceTypeId;
  /** 资源标识列表。 */
  private List<Long> idList;
  /** 排除的资源标识列表。 */
  private List<Long> excludeIdList;
  /** 管控级别。 */
  private Integer controlLevel;

  public Long getUserId() { return this.userId; }

  public Long getProjectId() { return this.projectId; }

  public Long getResourceTypeId() { return this.resourceTypeId; }

  public List<Long> getIdList() { return this.idList; }

  public List<Long> getExcludeIdList() { return this.excludeIdList; }

  public Integer getControlLevel() { return this.controlLevel; }

  public void setUserId(Long userId) { this.userId = userId; }

  public void setProjectId(Long projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Long resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setIdList(List<Long> idList) { this.idList = idList; }

  public void setExcludeIdList(List<Long> excludeIdList) {
    this.excludeIdList = excludeIdList;
  }

  public void setControlLevel(Integer controlLevel) {
    this.controlLevel = controlLevel;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AssignToOneUserDTO)) {
      return false;
    }
    AssignToOneUserDTO other = (AssignToOneUserDTO)o;
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
    Integer this$controlLevel = this.getControlLevel();
    Integer other$controlLevel = other.getControlLevel();
    if (this$controlLevel == null
            ? other$controlLevel != null
            : !((Object)this$controlLevel).equals(other$controlLevel)) {
      return false;
    }
    List<Long> this$idList = this.getIdList();
    List<Long> other$idList = other.getIdList();
    if (this$idList == null ? other$idList != null
                            : !((Object)this$idList).equals(other$idList)) {
      return false;
    }
    List<Long> this$excludeIdList = this.getExcludeIdList();
    List<Long> other$excludeIdList = other.getExcludeIdList();
    return !(this$excludeIdList == null
                 ? other$excludeIdList != null
                 : !((Object)this$excludeIdList).equals(other$excludeIdList));
  }

  protected boolean canEqual(Object other) {
    return other instanceof AssignToOneUserDTO;
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
    Integer $controlLevel = this.getControlLevel();
    result = result * 59 +
             ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
    List<Long> $idList = this.getIdList();
    result =
        result * 59 + ($idList == null ? 43 : ((Object)$idList).hashCode());
    List<Long> $excludeIdList = this.getExcludeIdList();
    result =
        result * 59 +
        ($excludeIdList == null ? 43 : ((Object)$excludeIdList).hashCode());
    return result;
  }

  public String toString() {
    return "AssignToOneUserDTO(userId=" + this.getUserId() +
        ", projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", idList=" + this.getIdList() +
        ", excludeIdList=" + this.getExcludeIdList() +
        ", controlLevel=" + this.getControlLevel() + ")";
  }
}
