package io.yak.framework.security.common.vo.project;

import lombok.Data;

import io.yak.framework.security.common.vo.dept.DeptBriefVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import java.util.Date;
import java.util.List;
/**
 * 项目详情视图对象。
 *
 * @author weifuwan
 */
@Data
public class ProjectVO {
  /** 主键标识。 */
  private Long id;
  /** 项目编码。 */
  private String projectCode;
  /** 项目名称。 */
  private String projectName;
  /** 用户列表。 */
  private List<UserBriefVO> userList;
  /** 项目负责人列表。 */
  private List<UserBriefVO> ownerList;
  /** 描述信息。 */
  private String description;
  /** 项目是否运行中。 */
  private Boolean running;
  /** 部门列表。 */
  private List<DeptBriefVO> deptList;
  /** 部门标识。 */
  private Long deptId;
  /** 创建时间。 */
  private Date createTime;

  public Long getId() { return this.id; }

  public String getProjectCode() { return this.projectCode; }

  public String getProjectName() { return this.projectName; }

  public List<UserBriefVO> getUserList() { return this.userList; }

  public List<UserBriefVO> getOwnerList() { return this.ownerList; }

  public String getDescription() { return this.description; }

  public Boolean getRunning() { return this.running; }

  public List<DeptBriefVO> getDeptList() { return this.deptList; }

  public Long getDeptId() { return this.deptId; }

  public Date getCreateTime() { return this.createTime; }

  public void setId(Long id) { this.id = id; }

  public void setProjectCode(String projectCode) {
    this.projectCode = projectCode;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public void setUserList(List<UserBriefVO> userList) {
    this.userList = userList;
  }

  public void setOwnerList(List<UserBriefVO> ownerList) {
    this.ownerList = ownerList;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setRunning(Boolean running) { this.running = running; }

  public void setDeptList(List<DeptBriefVO> deptList) {
    this.deptList = deptList;
  }

  public void setDeptId(Long deptId) { this.deptId = deptId; }

  public void setCreateTime(Date createTime) { this.createTime = createTime; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ProjectVO)) {
      return false;
    }
    ProjectVO other = (ProjectVO)o;
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
    String this$projectCode = this.getProjectCode();
    String other$projectCode = other.getProjectCode();
    if (this$projectCode == null
            ? other$projectCode != null
            : !this$projectCode.equals(other$projectCode)) {
      return false;
    }
    String this$projectName = this.getProjectName();
    String other$projectName = other.getProjectName();
    if (this$projectName == null
            ? other$projectName != null
            : !this$projectName.equals(other$projectName)) {
      return false;
    }
    List<UserBriefVO> this$userList = this.getUserList();
    List<UserBriefVO> other$userList = other.getUserList();
    if (this$userList == null
            ? other$userList != null
            : !((Object)this$userList).equals(other$userList)) {
      return false;
    }
    List<UserBriefVO> this$ownerList = this.getOwnerList();
    List<UserBriefVO> other$ownerList = other.getOwnerList();
    if (this$ownerList == null
            ? other$ownerList != null
            : !((Object)this$ownerList).equals(other$ownerList)) {
      return false;
    }
    String this$description = this.getDescription();
    String other$description = other.getDescription();
    if (this$description == null
            ? other$description != null
            : !this$description.equals(other$description)) {
      return false;
    }
    List<DeptBriefVO> this$deptList = this.getDeptList();
    List<DeptBriefVO> other$deptList = other.getDeptList();
    if (this$deptList == null
            ? other$deptList != null
            : !((Object)this$deptList).equals(other$deptList)) {
      return false;
    }
    Date this$createTime = this.getCreateTime();
    Date other$createTime = other.getCreateTime();
    return !(this$createTime == null
                 ? other$createTime != null
                 : !((Object)this$createTime).equals(other$createTime));
  }

  protected boolean canEqual(Object other) {
    return other instanceof ProjectVO;
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
    String $projectCode = this.getProjectCode();
    result =
        result * 59 + ($projectCode == null ? 43 : $projectCode.hashCode());
    String $projectName = this.getProjectName();
    result =
        result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
    List<UserBriefVO> $userList = this.getUserList();
    result =
        result * 59 + ($userList == null ? 43 : ((Object)$userList).hashCode());
    List<UserBriefVO> $ownerList = this.getOwnerList();
    result = result * 59 +
             ($ownerList == null ? 43 : ((Object)$ownerList).hashCode());
    String $description = this.getDescription();
    result =
        result * 59 + ($description == null ? 43 : $description.hashCode());
    List<DeptBriefVO> $deptList = this.getDeptList();
    result =
        result * 59 + ($deptList == null ? 43 : ((Object)$deptList).hashCode());
    Date $createTime = this.getCreateTime();
    result = result * 59 +
             ($createTime == null ? 43 : ((Object)$createTime).hashCode());
    return result;
  }

  public String toString() {
    return "ProjectVO(id=" + this.getId() +
        ", projectCode=" + this.getProjectCode() +
        ", projectName=" + this.getProjectName() +
        ", userList=" + this.getUserList() +
        ", ownerList=" + this.getOwnerList() +
        ", description=" + this.getDescription() +
        ", running=" + this.getRunning() +
        ", deptList=" + this.getDeptList() + ", deptId=" + this.getDeptId() +
        ", createTime=" + this.getCreateTime() + ")";
  }
}
