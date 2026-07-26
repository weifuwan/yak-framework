package io.yak.framework.security.common.dto.resource;

import java.util.List;
public class AssignToManyUserDTO {
  private Integer projectId;
  private Integer resourceTypeId;
  private Integer resourceId;
  private List<Integer> userIdList;
  private List<Integer> excludeUserIdList;
  private Integer controlLevel;

  public Integer getProjectId() { return this.projectId; }

  public Integer getResourceTypeId() { return this.resourceTypeId; }

  public Integer getResourceId() { return this.resourceId; }

  public List<Integer> getUserIdList() { return this.userIdList; }

  public List<Integer> getExcludeUserIdList() { return this.excludeUserIdList; }

  public Integer getControlLevel() { return this.controlLevel; }

  public void setProjectId(Integer projectId) { this.projectId = projectId; }

  public void setResourceTypeId(Integer resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setResourceId(Integer resourceId) {
    this.resourceId = resourceId;
  }

  public void setUserIdList(List<Integer> userIdList) {
    this.userIdList = userIdList;
  }

  public void setExcludeUserIdList(List<Integer> excludeUserIdList) {
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
    List<Integer> this$userIdList = this.getUserIdList();
    List<Integer> other$userIdList = other.getUserIdList();
    if (this$userIdList == null
            ? other$userIdList != null
            : !((Object)this$userIdList).equals(other$userIdList)) {
      return false;
    }
    List<Integer> this$excludeUserIdList = this.getExcludeUserIdList();
    List<Integer> other$excludeUserIdList = other.getExcludeUserIdList();
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
    List<Integer> $userIdList = this.getUserIdList();
    result = result * 59 +
             ($userIdList == null ? 43 : ((Object)$userIdList).hashCode());
    List<Integer> $excludeUserIdList = this.getExcludeUserIdList();
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
