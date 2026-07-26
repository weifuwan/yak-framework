package io.yak.framework.security.common.vo.user;

import lombok.Data;

import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import io.yak.framework.security.common.vo.project.ProjectBriefVO;
import io.yak.framework.security.common.vo.role.RoleBriefVO;
import java.util.Date;
import java.util.List;
/**
 * 用户详情视图对象。
 *
 * @author weifuwan
 */
@Data
public class UserVO {
  /** 主键标识。 */
  private Long id;
  /** 用户名。 */
  private String userName;
  /** 用户真实姓名。 */
  private String realName;
  /** 手机号码。 */
  private String phone;
  /** 电子邮箱。 */
  private String email;
  /** 最后更新时间。 */
  private Date updateTime;
  /** 创建时间。 */
  private Date createTime;
  /** 角色列表。 */
  private List<RoleBriefVO> roleList;
  /** 权限树。 */
  private PermissionTreeVO permissionTreeVO;
  /** 项目列表。 */
  private List<ProjectBriefVO> projectList;

  public Long getId() { return this.id; }

  public String getUserName() { return this.userName; }

  public String getRealName() { return this.realName; }

  public String getPhone() { return this.phone; }

  public String getEmail() { return this.email; }

  public Date getUpdateTime() { return this.updateTime; }

  public Date getCreateTime() { return this.createTime; }

  public List<RoleBriefVO> getRoleList() { return this.roleList; }

  public PermissionTreeVO getPermissionTreeVO() {
    return this.permissionTreeVO;
  }

  public List<ProjectBriefVO> getProjectList() { return this.projectList; }

  public void setId(Long id) { this.id = id; }

  public void setUserName(String userName) { this.userName = userName; }

  public void setRealName(String realName) { this.realName = realName; }

  public void setPhone(String phone) { this.phone = phone; }

  public void setEmail(String email) { this.email = email; }

  public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

  public void setCreateTime(Date createTime) { this.createTime = createTime; }

  public void setRoleList(List<RoleBriefVO> roleList) {
    this.roleList = roleList;
  }

  public void setPermissionTreeVO(PermissionTreeVO permissionTreeVO) {
    this.permissionTreeVO = permissionTreeVO;
  }

  public void setProjectList(List<ProjectBriefVO> projectList) {
    this.projectList = projectList;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserVO)) {
      return false;
    }
    UserVO other = (UserVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    String this$userName = this.getUserName();
    String other$userName = other.getUserName();
    if (this$userName == null ? other$userName != null
                              : !this$userName.equals(other$userName)) {
      return false;
    }
    String this$realName = this.getRealName();
    String other$realName = other.getRealName();
    if (this$realName == null ? other$realName != null
                              : !this$realName.equals(other$realName)) {
      return false;
    }
    String this$phone = this.getPhone();
    String other$phone = other.getPhone();
    if (this$phone == null ? other$phone != null
                           : !this$phone.equals(other$phone)) {
      return false;
    }
    String this$email = this.getEmail();
    String other$email = other.getEmail();
    if (this$email == null ? other$email != null
                           : !this$email.equals(other$email)) {
      return false;
    }
    Date this$updateTime = this.getUpdateTime();
    Date other$updateTime = other.getUpdateTime();
    if (this$updateTime == null
            ? other$updateTime != null
            : !((Object)this$updateTime).equals(other$updateTime)) {
      return false;
    }
    Date this$createTime = this.getCreateTime();
    Date other$createTime = other.getCreateTime();
    if (this$createTime == null
            ? other$createTime != null
            : !((Object)this$createTime).equals(other$createTime)) {
      return false;
    }
    List<RoleBriefVO> this$roleList = this.getRoleList();
    List<RoleBriefVO> other$roleList = other.getRoleList();
    if (this$roleList == null
            ? other$roleList != null
            : !((Object)this$roleList).equals(other$roleList)) {
      return false;
    }
    PermissionTreeVO this$permissionTreeVO = this.getPermissionTreeVO();
    PermissionTreeVO other$permissionTreeVO = other.getPermissionTreeVO();
    if (this$permissionTreeVO == null
            ? other$permissionTreeVO != null
            : !((Object)this$permissionTreeVO).equals(other$permissionTreeVO)) {
      return false;
    }
    List<ProjectBriefVO> this$projectList = this.getProjectList();
    List<ProjectBriefVO> other$projectList = other.getProjectList();
    return !(this$projectList == null
                 ? other$projectList != null
                 : !((Object)this$projectList).equals(other$projectList));
  }

  protected boolean canEqual(Object other) { return other instanceof UserVO; }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    String $phone = this.getPhone();
    result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
    String $email = this.getEmail();
    result = result * 59 + ($email == null ? 43 : $email.hashCode());
    Date $updateTime = this.getUpdateTime();
    result = result * 59 +
             ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
    Date $createTime = this.getCreateTime();
    result = result * 59 +
             ($createTime == null ? 43 : ((Object)$createTime).hashCode());
    List<RoleBriefVO> $roleList = this.getRoleList();
    result =
        result * 59 + ($roleList == null ? 43 : ((Object)$roleList).hashCode());
    PermissionTreeVO $permissionTreeVO = this.getPermissionTreeVO();
    result = result * 59 + ($permissionTreeVO == null
                                ? 43
                                : ((Object)$permissionTreeVO).hashCode());
    List<ProjectBriefVO> $projectList = this.getProjectList();
    result = result * 59 +
             ($projectList == null ? 43 : ((Object)$projectList).hashCode());
    return result;
  }

  public String toString() {
    return "UserVO(id=" + this.getId() + ", userName=" + this.getUserName() +
        ", realName=" + this.getRealName() + ", phone=" + this.getPhone() +
        ", email=" + this.getEmail() +
        ", updateTime=" + this.getUpdateTime() +
        ", createTime=" + this.getCreateTime() +
        ", roleList=" + this.getRoleList() +
        ", permissionTreeVO=" + this.getPermissionTreeVO() +
        ", projectList=" + this.getProjectList() + ")";
  }
}
