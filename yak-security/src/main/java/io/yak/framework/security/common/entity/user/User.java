package io.yak.framework.security.common.entity.user;

import io.yak.framework.security.common.entity.BaseEntity;

public class User extends BaseEntity {
  private String userName;
  private String pw;
  private String salt;
  private String realName;
  private String phone;
  private String email;
  private Long deptId;
  /** 1 = active, 2 = disabled. */
  private Integer status = 1;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User other = (User)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Long this$deptId = this.getDeptId();
    Long other$deptId = other.getDeptId();
    if (this$deptId == null ? other$deptId != null
                            : !((Object)this$deptId).equals(other$deptId)) {
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
    String this$salt = this.getSalt();
    String other$salt = other.getSalt();
    if (this$salt == null ? other$salt != null
                          : !this$salt.equals(other$salt)) {
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
    return !(this$email == null ? other$email != null
                                : !this$email.equals(other$email));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof User;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Long $deptId = this.getDeptId();
    result =
        result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $pw = this.getPw();
    result = result * 59 + ($pw == null ? 43 : $pw.hashCode());
    String $salt = this.getSalt();
    result = result * 59 + ($salt == null ? 43 : $salt.hashCode());
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    String $phone = this.getPhone();
    result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
    String $email = this.getEmail();
    result = result * 59 + ($email == null ? 43 : $email.hashCode());
    return result;
  }

  public String getUserName() { return this.userName; }

  public String getPw() { return this.pw; }

  public String getSalt() { return this.salt; }

  public String getRealName() { return this.realName; }

  public String getPhone() { return this.phone; }

  public String getEmail() { return this.email; }

  public Long getDeptId() { return this.deptId; }

  public Integer getStatus() { return this.status; }

  public void setUserName(String userName) { this.userName = userName; }

  public void setPw(String pw) { this.pw = pw; }

  public void setSalt(String salt) { this.salt = salt; }

  public void setRealName(String realName) { this.realName = realName; }

  public void setPhone(String phone) { this.phone = phone; }

  public void setEmail(String email) { this.email = email; }

  public void setDeptId(Long deptId) { this.deptId = deptId; }

  public void setStatus(Integer status) { this.status = status; }

  @Override
  public String toString() {
    return "User(userName=" + this.getUserName() +
        ", realName=" + this.getRealName() + ", phone=" + this.getPhone() +
        ", email=" + this.getEmail() + ", deptId=" + this.getDeptId() +
        ", status=" + this.getStatus() + ")";
  }
}
