package io.yak.framework.security.common.dto.project;

import lombok.Data;

import java.util.List;
/**
 * 项目保存数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class ProjectSaveDTO {
  /** 标识。 */
  private Long id;
  /** 项目名称。 */
  private String projectName;
  /** 用户标识列表。 */
  private List<Long> userIdList;
  /** 项目负责人标识列表。 */
  private List<Long> ownerIdList;
  /** 描述。 */
  private String description;
  /** 项目是否正在运行。 */
  private Boolean running;
  /** 所属部门标识。 */
  private Long deptId;

  public Long getId() { return this.id; }

  public String getProjectName() { return this.projectName; }

  public List<Long> getUserIdList() { return this.userIdList; }

  public List<Long> getOwnerIdList() { return this.ownerIdList; }

  public String getDescription() { return this.description; }

  public Boolean getRunning() { return this.running; }

  public Long getDeptId() { return this.deptId; }

  public void setId(Long id) { this.id = id; }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public void setUserIdList(List<Long> userIdList) {
    this.userIdList = userIdList;
  }

  public void setOwnerIdList(List<Long> ownerIdList) {
    this.ownerIdList = ownerIdList;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setRunning(Boolean running) { this.running = running; }

  public void setDeptId(Long deptId) { this.deptId = deptId; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ProjectSaveDTO)) {
      return false;
    }
    ProjectSaveDTO other = (ProjectSaveDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Boolean this$running = this.getRunning();
    Boolean other$running = other.getRunning();
    if (this$running == null ? other$running != null
                             : !((Object)this$running).equals(other$running)) {
      return false;
    }
    Long this$deptId = this.getDeptId();
    Long other$deptId = other.getDeptId();
    if (this$deptId == null ? other$deptId != null
                            : !((Object)this$deptId).equals(other$deptId)) {
      return false;
    }
    String this$projectName = this.getProjectName();
    String other$projectName = other.getProjectName();
    if (this$projectName == null
            ? other$projectName != null
            : !this$projectName.equals(other$projectName)) {
      return false;
    }
    List<Long> this$userIdList = this.getUserIdList();
    List<Long> other$userIdList = other.getUserIdList();
    if (this$userIdList == null
            ? other$userIdList != null
            : !((Object)this$userIdList).equals(other$userIdList)) {
      return false;
    }
    List<Long> this$ownerIdList = this.getOwnerIdList();
    List<Long> other$ownerIdList = other.getOwnerIdList();
    if (this$ownerIdList == null
            ? other$ownerIdList != null
            : !((Object)this$ownerIdList).equals(other$ownerIdList)) {
      return false;
    }
    String this$description = this.getDescription();
    String other$description = other.getDescription();
    return !(this$description == null
                 ? other$description != null
                 : !this$description.equals(other$description));
  }

  protected boolean canEqual(Object other) {
    return other instanceof ProjectSaveDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Boolean $running = this.getRunning();
    result =
        result * 59 + ($running == null ? 43 : ((Object)$running).hashCode());
    Long $deptId = this.getDeptId();
    result =
        result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
    String $projectName = this.getProjectName();
    result =
        result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
    List<Long> $userIdList = this.getUserIdList();
    result = result * 59 +
             ($userIdList == null ? 43 : ((Object)$userIdList).hashCode());
    List<Long> $ownerIdList = this.getOwnerIdList();
    result = result * 59 +
             ($ownerIdList == null ? 43 : ((Object)$ownerIdList).hashCode());
    String $description = this.getDescription();
    result =
        result * 59 + ($description == null ? 43 : $description.hashCode());
    return result;
  }

  public String toString() {
    return "ProjectSaveDTO(id=" + this.getId() +
        ", projectName=" + this.getProjectName() +
        ", userIdList=" + this.getUserIdList() +
        ", ownerIdList=" + this.getOwnerIdList() +
        ", description=" + this.getDescription() +
        ", running=" + this.getRunning() + ", deptId=" + this.getDeptId() +
        ")";
  }
}
