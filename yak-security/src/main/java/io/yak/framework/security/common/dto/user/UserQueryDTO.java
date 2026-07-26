package io.yak.framework.security.common.dto.user;

import io.yak.framework.security.common.dto.PageParamDTO;
public class UserQueryDTO extends PageParamDTO {
  private Integer id;
  private Integer roleId;
  private String userName;
  private String realName;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserQueryDTO)) {
      return false;
    }
    UserQueryDTO other = (UserQueryDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Integer this$id = this.getId();
    Integer other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Integer this$roleId = this.getRoleId();
    Integer other$roleId = other.getRoleId();
    if (this$roleId == null ? other$roleId != null
                            : !((Object)this$roleId).equals(other$roleId)) {
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
    return !(this$realName == null ? other$realName != null
                                   : !this$realName.equals(other$realName));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserQueryDTO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Integer $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Integer $roleId = this.getRoleId();
    result =
        result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    return result;
  }

  public Integer getId() { return this.id; }

  public Integer getRoleId() { return this.roleId; }

  public String getUserName() { return this.userName; }

  public String getRealName() { return this.realName; }

  public void setId(Integer id) { this.id = id; }

  public void setRoleId(Integer roleId) { this.roleId = roleId; }

  public void setUserName(String userName) { this.userName = userName; }

  public void setRealName(String realName) { this.realName = realName; }

  @Override
  public String toString() {
    return "UserQueryDTO(id=" + this.getId() + ", roleId=" + this.getRoleId() +
        ", userName=" + this.getUserName() +
        ", realName=" + this.getRealName() + ")";
  }
}
