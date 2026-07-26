package io.yak.framework.security.common.dto.user;

import lombok.Data;

import io.yak.framework.security.common.dto.PageParamDTO;
/**
 * 用户查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class UserQueryDTO extends PageParamDTO {
  /** 标识。 */
  private Long id;
  /** 角色标识。 */
  private Long roleId;
  /** 用户名。 */
  private String userName;
  /** 真实姓名。 */
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
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Long this$roleId = this.getRoleId();
    Long other$roleId = other.getRoleId();
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
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Long $roleId = this.getRoleId();
    result =
        result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "UserQueryDTO(id=" + this.getId() + ", roleId=" + this.getRoleId() +
        ", userName=" + this.getUserName() +
        ", realName=" + this.getRealName() + ")";
  }
}
