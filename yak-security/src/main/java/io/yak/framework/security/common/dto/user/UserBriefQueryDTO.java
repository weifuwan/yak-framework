package io.yak.framework.security.common.dto.user;

import io.yak.framework.security.common.dto.PageParamDTO;
import io.yak.framework.security.common.dto.resource.MByUQueryDTO;

public class UserBriefQueryDTO extends PageParamDTO {
  private String userName;
  private String realName;
  private Long deptId;
  private String deptName;

  public UserBriefQueryDTO(MByUQueryDTO queryDTO) {
    this.setPage(queryDTO.getPage());
    this.setSize(queryDTO.getSize());
    this.userName = queryDTO.getUserName();
    this.realName = queryDTO.getRealName();
    this.deptId = queryDTO.getDeptId();
    this.deptName = queryDTO.getDeptName();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserBriefQueryDTO)) {
      return false;
    }
    UserBriefQueryDTO other = (UserBriefQueryDTO)o;
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
    String this$realName = this.getRealName();
    String other$realName = other.getRealName();
    if (this$realName == null ? other$realName != null
                              : !this$realName.equals(other$realName)) {
      return false;
    }
    String this$deptName = this.getDeptName();
    String other$deptName = other.getDeptName();
    return !(this$deptName == null ? other$deptName != null
                                   : !this$deptName.equals(other$deptName));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserBriefQueryDTO;
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
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    String $deptName = this.getDeptName();
    result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
    return result;
  }

  public String getUserName() { return this.userName; }

  public String getRealName() { return this.realName; }

  public Long getDeptId() { return this.deptId; }

  public String getDeptName() { return this.deptName; }

  public void setUserName(String userName) { this.userName = userName; }

  public void setRealName(String realName) { this.realName = realName; }

  public void setDeptId(Long deptId) { this.deptId = deptId; }

  public void setDeptName(String deptName) { this.deptName = deptName; }

  @Override
  public String toString() {
    return "UserBriefQueryDTO(userName=" + this.getUserName() +
        ", realName=" + this.getRealName() + ", deptId=" + this.getDeptId() +
        ", deptName=" + this.getDeptName() + ")";
  }
}
