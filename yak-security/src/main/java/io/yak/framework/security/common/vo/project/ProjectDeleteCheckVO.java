package io.yak.framework.security.common.vo.project;

import java.util.List;
public class ProjectDeleteCheckVO {
  private Integer projectId;
  private List<String> resourceNameList;

  public ProjectDeleteCheckVO(Integer projectId,
                              List<String> resourceNameList) {
    this.projectId = projectId;
    this.resourceNameList = resourceNameList;
  }

  public Integer getProjectId() { return this.projectId; }

  public List<String> getResourceNameList() { return this.resourceNameList; }

  public void setProjectId(Integer projectId) { this.projectId = projectId; }

  public void setResourceNameList(List<String> resourceNameList) {
    this.resourceNameList = resourceNameList;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ProjectDeleteCheckVO)) {
      return false;
    }
    ProjectDeleteCheckVO other = (ProjectDeleteCheckVO)o;
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
    List<String> this$resourceNameList = this.getResourceNameList();
    List<String> other$resourceNameList = other.getResourceNameList();
    return !(
        this$resourceNameList == null
            ? other$resourceNameList != null
            : !((Object)this$resourceNameList).equals(other$resourceNameList));
  }

  protected boolean canEqual(Object other) {
    return other instanceof ProjectDeleteCheckVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    List<String> $resourceNameList = this.getResourceNameList();
    result = result * 59 + ($resourceNameList == null
                                ? 43
                                : ((Object)$resourceNameList).hashCode());
    return result;
  }

  public String toString() {
    return "ProjectDeleteCheckVO(projectId=" + this.getProjectId() +
        ", resourceNameList=" + this.getResourceNameList() + ")";
  }
}
