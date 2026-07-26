package io.yak.framework.security.common.dto.resource;

import java.util.List;
/**
 * 资源批量分配用户数据传输对象。
 *
 * @author weifuwan
 */
public class AssignToManyUserDTO {
  /** 项目标识。 */
  private Long projectId;
  /** 资源类型标识。 */
  private Long resourceTypeId;
  /** 资源标识。 */
  private Long resourceId;
  /** 用户标识列表。 */
  private List<Long> userIdList;
  /** 排除的用户标识列表。 */
  private List<Long> excludeUserIdList;
  /** 管控级别。 */
  private Integer controlLevel;

  public Long getProjectId() { return this.projectId; }

  public Long getResourceTypeId() { return this.resourceTypeId; }

  public Long getResourceId() { return this.resourceId; }

  public List<Long> getUserIdList() { return this.userIdList; }

  public List<Long> getExcludeUserIdList() { return this.excludeUserIdList; }

  public Integer getControlLevel() { return this.controlLevel; }

  public void setProjectId(Long projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Long resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setResourceId(Long resourceId) {
    this.resourceId = resourceId;
  }

  public void setUserIdList(List<Long> userIdList) {
    this.userIdList = userIdList;
  }

  public void setExcludeUserIdList(List<Long> excludeUserIdList) {
    this.excludeUserIdList = excludeUserIdList;
  }

  public void setControlLevel(Integer controlLevel) {
    this.controlLevel = controlLevel;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AssignToManyUserDTO)) {
      return false;
    }
    AssignToManyUserDTO other = (AssignToManyUserDTO)o;
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
    List<Long> this$userIdList = this.getUserIdList();
    List<Long> other$userIdList = other.getUserIdList();
    if (this$userIdList == null
            ? other$userIdList != null
            : !((Object)this$userIdList).equals(other$userIdList)) {
      return false;
    }
    List<Long> this$excludeUserIdList = this.getExcludeUserIdList();
    List<Long> other$excludeUserIdList = other.getExcludeUserIdList();
    return !(this$excludeUserIdList == null
                 ? other$excludeUserIdList != null
                 : !((Object)this$excludeUserIdList)
                        .equals(other$excludeUserIdList));
  }

  protected boolean canEqual(Object other) {
    return other instanceof AssignToManyUserDTO;
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
    List<Long> $userIdList = this.getUserIdList();
    result = result * 59 +
             ($userIdList == null ? 43 : ((Object)$userIdList).hashCode());
    List<Long> $excludeUserIdList = this.getExcludeUserIdList();
    result = result * 59 + ($excludeUserIdList == null
                                ? 43
                                : ((Object)$excludeUserIdList).hashCode());
    return result;
  }

  public String toString() {
    return "AssignToManyUserDTO(projectId=" + this.getProjectId() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", resourceId=" + this.getResourceId() +
        ", userIdList=" + this.getUserIdList() +
        ", excludeUserIdList=" + this.getExcludeUserIdList() +
        ", controlLevel=" + this.getControlLevel() + ")";
  }
}
