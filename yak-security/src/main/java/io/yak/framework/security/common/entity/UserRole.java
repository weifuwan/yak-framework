package io.yak.framework.security.common.entity;

public class UserRole {
  private Integer userId;
  private Integer roleId;

  public UserRole() {}

  public UserRole(Integer userId, Integer roleId) {
    this.userId = userId;
    this.roleId = roleId;
  }

  public Integer getUserId() { return this.userId; }

  public Integer getRoleId() { return this.roleId; }

  public void setUserId(Integer userId) { this.userId = userId; }

  public void setRoleId(Integer roleId) { this.roleId = roleId; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserRole)) {
      return false;
    }
    UserRole other = (UserRole)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Integer this$userId = this.getUserId();
    Integer other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null
                            : !((Object)this$userId).equals(other$userId)) {
      return false;
    }
    Integer this$roleId = this.getRoleId();
    Integer other$roleId = other.getRoleId();
    return !(this$roleId == null ? other$roleId != null
                                 : !((Object)this$roleId).equals(other$roleId));
  }

  protected boolean canEqual(Object other) { return other instanceof UserRole; }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    Integer $roleId = this.getRoleId();
    result =
        result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
    return result;
  }

  public String toString() {
    return "UserRole(userId=" + this.getUserId() +
        ", roleId=" + this.getRoleId() + ")";
  }
}
