package io.yak.framework.security.common.vo.project;

import lombok.Data;

import java.util.List;
/**
 * 项目删除检查结果视图对象。
 *
 * @author weifuwan
 */
@Data
public class ProjectDeleteCheckVO {
  /** 项目标识。 */
  private Long projectId;
  /** 关联的资源名称列表。 */
  private List<String> resourceNameList;

  public ProjectDeleteCheckVO(Long projectId,
                              List<String> resourceNameList) {
    this.projectId = projectId;
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
    Long this$projectId = this.getProjectId();
    Long other$projectId = other.getProjectId();
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
    Long $projectId = this.getProjectId();
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
