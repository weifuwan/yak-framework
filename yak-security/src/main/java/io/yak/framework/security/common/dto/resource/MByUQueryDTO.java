package io.yak.framework.security.common.dto.resource;

import lombok.Data;

import io.yak.framework.security.common.dto.PageParamDTO;
/**
 * 按用户查询授权的数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class MByUQueryDTO extends PageParamDTO {
  /** 所属部门标识。 */
  private Long deptId;
  /** 部门名称。 */
  private String deptName;
  /** 用户名。 */
  private String userName;
  /** 真实姓名。 */
  private String realName;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MByUQueryDTO)) {
      return false;
    }
    MByUQueryDTO other = (MByUQueryDTO)o;
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
    String this$deptName = this.getDeptName();
    String other$deptName = other.getDeptName();
    if (this$deptName == null ? other$deptName != null
                              : !this$deptName.equals(other$deptName)) {
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
    return other instanceof MByUQueryDTO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Long $deptId = this.getDeptId();
    result =
        result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
    String $deptName = this.getDeptName();
    result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    return result;
  }

  public Long getDeptId() { return this.deptId; }

  public String getDeptName() { return this.deptName; }

  public String getUserName() { return this.userName; }

  public String getRealName() { return this.realName; }

  public void setDeptId(Long deptId) { this.deptId = deptId; }

  public void setDeptName(String deptName) { this.deptName = deptName; }

  public void setUserName(String userName) { this.userName = userName; }

  public void setRealName(String realName) { this.realName = realName; }

  @Override
  public String toString() {
    return "MByUQueryDTO(deptId=" + this.getDeptId() +
        ", deptName=" + this.getDeptName() +
        ", userName=" + this.getUserName() +
        ", realName=" + this.getRealName() + ")";
  }
}
