package io.yak.framework.security.common.dto.project;

import lombok.Data;

import io.yak.framework.security.common.dto.PageParamDTO;
import io.yak.framework.security.common.dto.resource.MByRQueryDTO;
/**
 * 项目简要查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class ProjectBriefQueryDTO extends PageParamDTO {
  /** 项目名称。 */
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

  @Override
  public String toString() {
    return "ProjectBriefQueryDTO(projectName=" + this.getProjectName() + ")";
  }
}
