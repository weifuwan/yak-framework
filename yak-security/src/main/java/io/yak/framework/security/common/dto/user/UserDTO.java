package io.yak.framework.security.common.dto.user;

import lombok.Data;

import java.util.List;
/**
 * 用户数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class UserDTO {
  /** 用户名。 */
  private String userName;
  /** 密码。 */
  private String pw;
  /** 真实姓名。 */
  private String realName;
  /** 手机号码。 */
  private String phone;
  /** 电子邮箱。 */
  private String email;
  /** 角色标识列表。 */
  private List<Long> roleIds;

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserDTO)) {
      return false;
    }
    UserDTO other = (UserDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    String this$userName = this.getUserName();
    String other$userName = other.getUserName();
    if (this$userName == null ? other$userName != null
                              : !this$userName.equals(other$userName)) {
      return false;
    }
    String this$pw = this.getPw();
    String other$pw = other.getPw();
    if (this$pw == null ? other$pw != null : !this$pw.equals(other$pw)) {
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
    List<Long> this$roleIds = this.getRoleIds();
    List<Long> other$roleIds = other.getRoleIds();
    return !(this$roleIds == null
                 ? other$roleIds != null
                 : !((Object)this$roleIds).equals(other$roleIds));
  }

  protected boolean canEqual(Object other) { return other instanceof UserDTO; }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $pw = this.getPw();
    result = result * 59 + ($pw == null ? 43 : $pw.hashCode());
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    String $phone = this.getPhone();
    result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
    String $email = this.getEmail();
    result = result * 59 + ($email == null ? 43 : $email.hashCode());
    List<Long> $roleIds = this.getRoleIds();
    result =
        result * 59 + ($roleIds == null ? 43 : ((Object)$roleIds).hashCode());
    return result;
  }

  public String toString() {
    return "UserDTO(userName=" + this.getUserName() + ", realName=" + this.getRealName() + ", phone=" + this.getPhone() +
        ", email=" + this.getEmail() + ", roleIds=" + this.getRoleIds() + ")";
  }
}
