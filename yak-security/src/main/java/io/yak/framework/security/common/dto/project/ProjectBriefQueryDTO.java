package io.yak.framework.security.common.dto.project;

import io.yak.framework.security.common.dto.PageParamDTO;
import io.yak.framework.security.common.dto.resource.MByRQueryDTO;
public class ProjectBriefQueryDTO extends PageParamDTO {
  private String projectName;

  public ProjectBriefQueryDTO(MByRQueryDTO queryDTO) {
    this.setPage(queryDTO.getPage());
    this.setSize(queryDTO.getSize());
    this.projectName = queryDTO.getName();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ProjectBriefQueryDTO)) {
      return false;
    }
    ProjectBriefQueryDTO other = (ProjectBriefQueryDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    String this$projectName = this.getProjectName();
    String other$projectName = other.getProjectName();
    return !(this$projectName == null
                 ? other$projectName != null
                 : !this$projectName.equals(other$projectName));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof ProjectBriefQueryDTO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    String $projectName = this.getProjectName();
    result =
        result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
    return result;
  }

  public String getProjectName() { return this.projectName; }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  @Override
  public String toString() {
    return "ProjectBriefQueryDTO(projectName=" + this.getProjectName() + ")";
  }
}
