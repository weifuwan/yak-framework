package io.yak.framework.security.common.vo.resource;

import lombok.Data;
/**
 * 按资源查询的用户授权数据视图对象。
 *
 * @author weifuwan
 */
@Data
public class MByRDataVO {
  /** 用户标识。 */
  private Long userId;
  /** 用户名。 */
  private String userName;
  /** 用户真实姓名。 */
  private String realName;
  /** 授权级别。 */
  private Integer hasLevel;

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MByRDataVO)) {
      return false;
    }
    MByRDataVO other = (MByRDataVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$userId = this.getUserId();
    Long other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null
                            : !((Object)this$userId).equals(other$userId)) {
      return false;
    }
    Integer this$hasLevel = this.getHasLevel();
    Integer other$hasLevel = other.getHasLevel();
    if (this$hasLevel == null
            ? other$hasLevel != null
            : !((Object)this$hasLevel).equals(other$hasLevel)) {
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
    return other instanceof MByRDataVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    Integer $hasLevel = this.getHasLevel();
    result =
        result * 59 + ($hasLevel == null ? 43 : ((Object)$hasLevel).hashCode());
    String $userName = this.getUserName();
    result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
    String $realName = this.getRealName();
    result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
    return result;
  }

  public String toString() {
    return "MByRDataVO(userId=" + this.getUserId() +
        ", userName=" + this.getUserName() +
        ", realName=" + this.getRealName() +
        ", hasLevel=" + this.getHasLevel() + ")";
  }
}
