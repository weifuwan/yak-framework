package io.yak.framework.security.common.entity.user;

public class UserBrief {
  private Long id;
  private String userName;
  private String realName;
  private Long deptId;

  public Long getId() { return this.id; }

  public String getUserName() { return this.userName; }

  public String getRealName() { return this.realName; }

  public Long getDeptId() { return this.deptId; }

  public void setId(Long id) { this.id = id; }

  public void setUserName(String userName) { this.userName = userName; }

  public void setRealName(String realName) { this.realName = realName; }

  public void setDeptId(Long deptId) { this.deptId = deptId; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserBrief)) {
      return false;
    }
    UserBrief other = (UserBrief)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
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
    String this$realName = this.getRealName();
    String other$realName = other.getRealName();
    return !(this$realName == null ? other$realName != null
                                   : !this$realName.equals(other$realName));
  }

  protected boolean canEqual(Object other) {
    return other instanceof UserBrief;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Long $deptId = this.getDeptId();
    result =
        result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    return result;
  }

  public String toString() {
    return "UserBrief(id=" + this.getId() + ", userName=" + this.getUserName() +
        ", realName=" + this.getRealName() + ", deptId=" + this.getDeptId() +
        ")";
  }
}
