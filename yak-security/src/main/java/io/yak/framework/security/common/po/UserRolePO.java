package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.yak.framework.security.common.po.BasePO;

@TableName(value = "yak_security_user_role")
public class UserRolePO extends BasePO {
  private Long userId;
  private Long roleId;

  public UserRolePO() {}

  public UserRolePO(Long userId, Long roleId) {
    this.userId = userId;
    this.roleId = roleId;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserRolePO)) {
      return false;
    }
    UserRolePO other = (UserRolePO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Long this$userId = this.getUserId();
    Long other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null
                            : !((Object)this$userId).equals(other$userId)) {
      return false;
    }
    Long this$roleId = this.getRoleId();
    Long other$roleId = other.getRoleId();
    return !(this$roleId == null ? other$roleId != null
                                 : !((Object)this$roleId).equals(other$roleId));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserRolePO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Long $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    Long $roleId = this.getRoleId();
    result =
        result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
    return result;
  }

  public Long getUserId() { return this.userId; }

  public Long getRoleId() { return this.roleId; }

  public void setUserId(Long userId) { this.userId = userId; }

  public void setRoleId(Long roleId) { this.roleId = roleId; }

  @Override
  public String toString() {
    return "UserRolePO(userId=" + this.getUserId() +
        ", roleId=" + this.getRoleId() + ")";
  }
}
