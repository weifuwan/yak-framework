package io.yak.framework.security.common.vo.project;
public class ProjectBriefVO {
  private Long id;
  private String projectCode;
  private String projectName;

  public Long getId() { return this.id; }

  public String getProjectCode() { return this.projectCode; }

  public String getProjectName() { return this.projectName; }

  public void setId(Long id) { this.id = id; }

  public void setProjectCode(String projectCode) {
    this.projectCode = projectCode;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ProjectBriefVO)) {
      return false;
    }
    ProjectBriefVO other = (ProjectBriefVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    String this$projectCode = this.getProjectCode();
    String other$projectCode = other.getProjectCode();
    if (this$projectCode == null
            ? other$projectCode != null
            : !this$projectCode.equals(other$projectCode)) {
      return false;
    }
    String this$projectName = this.getProjectName();
    String other$projectName = other.getProjectName();
    return !(this$projectName == null
                 ? other$projectName != null
                 : !this$projectName.equals(other$projectName));
  }

  protected boolean canEqual(Object other) {
    return other instanceof ProjectBriefVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $projectCode = this.getProjectCode();
    result =
        result * 59 + ($projectCode == null ? 43 : $projectCode.hashCode());
    String $projectName = this.getProjectName();
    result =
        result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
    return result;
  }

  public String toString() {
    return "ProjectBriefVO(id=" + this.getId() +
        ", projectCode=" + this.getProjectCode() +
        ", projectName=" + this.getProjectName() + ")";
  }
}
