package io.yak.framework.security.common.dto.resource;

import java.util.List;
public class AssignToOneUserDTO {
  private Integer userId;
  private Integer projectId;
  private Integer resourceTypeId;
  private List<Integer> idList;
  private List<Integer> excludeIdList;
  private Integer controlLevel;

  public Integer getUserId() { return this.userId; }

  public Integer getProjectId() { return this.projectId; }

  public Integer getResourceTypeId() { return this.resourceTypeId; }

  public List<Integer> getIdList() { return this.idList; }

  public List<Integer> getExcludeIdList() { return this.excludeIdList; }

  public Integer getControlLevel() { return this.controlLevel; }

  public void setUserId(Integer userId) { this.userId = userId; }

  public void setProjectId(Integer projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Integer resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setIdList(List<Integer> idList) { this.idList = idList; }

  public void setExcludeIdList(List<Integer> excludeIdList) {
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
    Integer this$controlLevel = this.getControlLevel();
    Integer other$controlLevel = other.getControlLevel();
    if (this$controlLevel == null
            ? other$controlLevel != null
            : !((Object)this$controlLevel).equals(other$controlLevel)) {
      return false;
    }
    List<Integer> this$idList = this.getIdList();
    List<Integer> other$idList = other.getIdList();
    if (this$idList == null ? other$idList != null
                            : !((Object)this$idList).equals(other$idList)) {
      return false;
    }
    List<Integer> this$excludeIdList = this.getExcludeIdList();
    List<Integer> other$excludeIdList = other.getExcludeIdList();
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
    Integer $controlLevel = this.getControlLevel();
    result = result * 59 +
             ($controlLevel == null ? 43 : ((Object)$controlLevel).hashCode());
    List<Integer> $idList = this.getIdList();
    result =
        result * 59 + ($idList == null ? 43 : ((Object)$idList).hashCode());
    List<Integer> $excludeIdList = this.getExcludeIdList();
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
