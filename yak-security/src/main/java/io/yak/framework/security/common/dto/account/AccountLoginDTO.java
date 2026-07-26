package io.yak.framework.security.common.dto.account;
/**
 * 账号登录数据传输对象。
 *
 * @author weifuwan
 */
public class AccountLoginDTO {
  /** 用户名。 */
  private String userName;
  /** 密码。 */
  private String pw;

  public String getUserName() { return this.userName; }

  public String getPw() { return this.pw; }

  public void setUserName(String userName) { this.userName = userName; }

  public void setPw(String pw) { this.pw = pw; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AccountLoginDTO)) {
      return false;
    }
    AccountLoginDTO other = (AccountLoginDTO)o;
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
    return !(this$pw == null ? other$pw != null : !this$pw.equals(other$pw));
  }

  protected boolean canEqual(Object other) {
    return other instanceof AccountLoginDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $pw = this.getPw();
    result = result * 59 + ($pw == null ? 43 : $pw.hashCode());
    return result;
  }

  public String toString() {
    return "AccountLoginDTO(userName=" + this.getUserName() + ")";
  }
}
