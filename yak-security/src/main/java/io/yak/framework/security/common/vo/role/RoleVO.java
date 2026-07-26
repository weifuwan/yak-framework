package io.yak.framework.security.common.vo.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.yak.framework.security.common.vo.permission.PermissionTreeVO;
import java.util.Date;
import java.util.List;
public class RoleVO {
  private Long id;
  private String roleName;
  private String roleCode;
  private String description;
  private Integer authedUserCnt;
  private List<String> authedUsers;
  private String lastReviser;
  private Date createTime;
  private Date updateTime;
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private PermissionTreeVO permissionTreeVO;

  public Long getId() { return this.id; }

  public String getRoleName() { return this.roleName; }

  public String getRoleCode() { return this.roleCode; }

  public String getDescription() { return this.description; }

  public Integer getAuthedUserCnt() { return this.authedUserCnt; }

  public List<String> getAuthedUsers() { return this.authedUsers; }

  public String getLastReviser() { return this.lastReviser; }

  public Date getCreateTime() { return this.createTime; }

  public Date getUpdateTime() { return this.updateTime; }

  public PermissionTreeVO getPermissionTreeVO() {
    return this.permissionTreeVO;
  }

  public void setId(Long id) { this.id = id; }

  public void setRoleName(String roleName) { this.roleName = roleName; }

  public void setRoleCode(String roleCode) { this.roleCode = roleCode; }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setAuthedUserCnt(Integer authedUserCnt) {
    this.authedUserCnt = authedUserCnt;
  }

  public void setAuthedUsers(List<String> authedUsers) {
    this.authedUsers = authedUsers;
  }

  public void setLastReviser(String lastReviser) {
    this.lastReviser = lastReviser;
  }

  public void setCreateTime(Date createTime) { this.createTime = createTime; }

  public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

  public void setPermissionTreeVO(PermissionTreeVO permissionTreeVO) {
    this.permissionTreeVO = permissionTreeVO;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof RoleVO)) {
      return false;
    }
    RoleVO other = (RoleVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Integer this$authedUserCnt = this.getAuthedUserCnt();
    Integer other$authedUserCnt = other.getAuthedUserCnt();
    if (this$authedUserCnt == null
            ? other$authedUserCnt != null
            : !((Object)this$authedUserCnt).equals(other$authedUserCnt)) {
      return false;
    }
    String this$roleName = this.getRoleName();
    String other$roleName = other.getRoleName();
    if (this$roleName == null ? other$roleName != null
                              : !this$roleName.equals(other$roleName)) {
      return false;
    }
    String this$roleCode = this.getRoleCode();
    String other$roleCode = other.getRoleCode();
    if (this$roleCode == null ? other$roleCode != null
                              : !this$roleCode.equals(other$roleCode)) {
      return false;
    }
    String this$description = this.getDescription();
    String other$description = other.getDescription();
    if (this$description == null
            ? other$description != null
            : !this$description.equals(other$description)) {
      return false;
    }
    List<String> this$authedUsers = this.getAuthedUsers();
    List<String> other$authedUsers = other.getAuthedUsers();
    if (this$authedUsers == null
            ? other$authedUsers != null
            : !((Object)this$authedUsers).equals(other$authedUsers)) {
      return false;
    }
    String this$lastReviser = this.getLastReviser();
    String other$lastReviser = other.getLastReviser();
    if (this$lastReviser == null
            ? other$lastReviser != null
            : !this$lastReviser.equals(other$lastReviser)) {
      return false;
    }
    Date this$createTime = this.getCreateTime();
    Date other$createTime = other.getCreateTime();
    if (this$createTime == null
            ? other$createTime != null
            : !((Object)this$createTime).equals(other$createTime)) {
      return false;
    }
    Date this$updateTime = this.getUpdateTime();
    Date other$updateTime = other.getUpdateTime();
    if (this$updateTime == null
            ? other$updateTime != null
            : !((Object)this$updateTime).equals(other$updateTime)) {
      return false;
    }
    PermissionTreeVO this$permissionTreeVO = this.getPermissionTreeVO();
    PermissionTreeVO other$permissionTreeVO = other.getPermissionTreeVO();
    return !(
        this$permissionTreeVO == null
            ? other$permissionTreeVO != null
            : !((Object)this$permissionTreeVO).equals(other$permissionTreeVO));
  }

  protected boolean canEqual(Object other) { return other instanceof RoleVO; }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Integer $authedUserCnt = this.getAuthedUserCnt();
    result =
        result * 59 +
        ($authedUserCnt == null ? 43 : ((Object)$authedUserCnt).hashCode());
    String $roleName = this.getRoleName();
    result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
    String $roleCode = this.getRoleCode();
    result = result * 59 + ($roleCode == null ? 43 : $roleCode.hashCode());
    String $description = this.getDescription();
    result =
        result * 59 + ($description == null ? 43 : $description.hashCode());
    List<String> $authedUsers = this.getAuthedUsers();
    result = result * 59 +
             ($authedUsers == null ? 43 : ((Object)$authedUsers).hashCode());
    String $lastReviser = this.getLastReviser();
    result =
        result * 59 + ($lastReviser == null ? 43 : $lastReviser.hashCode());
    Date $createTime = this.getCreateTime();
    result = result * 59 +
             ($createTime == null ? 43 : ((Object)$createTime).hashCode());
    Date $updateTime = this.getUpdateTime();
    result = result * 59 +
             ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
    PermissionTreeVO $permissionTreeVO = this.getPermissionTreeVO();
    result = result * 59 + ($permissionTreeVO == null
                                ? 43
                                : ((Object)$permissionTreeVO).hashCode());
    return result;
  }

  public String toString() {
    return "RoleVO(id=" + this.getId() + ", roleName=" + this.getRoleName() +
        ", roleCode=" + this.getRoleCode() +
        ", description=" + this.getDescription() +
        ", authedUserCnt=" + this.getAuthedUserCnt() +
        ", authedUsers=" + this.getAuthedUsers() +
        ", lastReviser=" + this.getLastReviser() +
        ", createTime=" + this.getCreateTime() +
        ", updateTime=" + this.getUpdateTime() +
        ", permissionTreeVO=" + this.getPermissionTreeVO() + ")";
  }
}
