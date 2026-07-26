package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.yak.framework.security.common.po.BasePO;

@TableName(value = "yak_security_user_project")
public class UserProjectPO extends BasePO {
  private Integer userId;
  private Integer userType;
  private Integer projectId;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserProjectPO)) {
      return false;
    }
    UserProjectPO other = (UserProjectPO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Integer this$userId = this.getUserId();
    Integer other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null
                            : !((Object)this$userId).equals(other$userId)) {
      return false;
    }
    Integer this$userType = this.getUserType();
    Integer other$userType = other.getUserType();
    if (this$userType == null
            ? other$userType != null
            : !((Object)this$userType).equals(other$userType)) {
      return false;
    }
    Integer this$projectId = this.getProjectId();
    Integer other$projectId = other.getProjectId();
    return !(this$projectId == null
                 ? other$projectId != null
                 : !((Object)this$projectId).equals(other$projectId));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserProjectPO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Integer $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    Integer $userType = this.getUserType();
    result =
        result * 59 + ($userType == null ? 43 : ((Object)$userType).hashCode());
    Integer $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    return result;
  }

  public Integer getUserId() { return this.userId; }

  public Integer getUserType() { return this.userType; }

  public Integer getProjectId() { return this.projectId; }

  public void setUserId(Integer userId) { this.userId = userId; }

  public void setUserType(Integer userType) { this.userType = userType; }

  public void setProjectId(Integer projectId) { this.projectId = projectId; }

  @Override
  public String toString() {
    return "UserProjectPO(userId=" + this.getUserId() +
        ", userType=" + this.getUserType() +
        ", projectId=" + this.getProjectId() + ")";
  }
}
