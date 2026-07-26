package io.yak.framework.security.common.vo.role;

import lombok.Data;

import java.util.List;
/**
 * 角色删除检查结果视图对象。
 *
 * @author weifuwan
 */
@Data
public class RoleDeleteCheckVO {
  /** 角色标识。 */
  private Long roleId;
  /** 关联的用户名列表。 */
  private List<String> userNameList;

  public Long getRoleId() { return this.roleId; }

  public List<String> getUserNameList() { return this.userNameList; }

  public void setRoleId(Long roleId) { this.roleId = roleId; }

  public void setUserNameList(List<String> userNameList) {
    this.userNameList = userNameList;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof RoleDeleteCheckVO)) {
      return false;
    }
    RoleDeleteCheckVO other = (RoleDeleteCheckVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$roleId = this.getRoleId();
    Long other$roleId = other.getRoleId();
    if (this$roleId == null ? other$roleId != null
                            : !((Object)this$roleId).equals(other$roleId)) {
      return false;
    }
    List<String> this$userNameList = this.getUserNameList();
    List<String> other$userNameList = other.getUserNameList();
    return !(this$userNameList == null
                 ? other$userNameList != null
                 : !((Object)this$userNameList).equals(other$userNameList));
  }

  protected boolean canEqual(Object other) {
    return other instanceof RoleDeleteCheckVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $roleId = this.getRoleId();
    result =
        result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
    List<String> $userNameList = this.getUserNameList();
    result = result * 59 +
             ($userNameList == null ? 43 : ((Object)$userNameList).hashCode());
    return result;
  }

  public String toString() {
    return "RoleDeleteCheckVO(roleId=" + this.getRoleId() +
        ", userNameList=" + this.getUserNameList() + ")";
  }
}
