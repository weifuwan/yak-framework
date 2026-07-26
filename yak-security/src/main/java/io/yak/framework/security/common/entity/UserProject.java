package io.yak.framework.security.common.entity;

public class UserProject {
  private Long userId;
  private Integer userType;
  private Long projectId;

  public Long getUserId() { return this.userId; }

  public Integer getUserType() { return this.userType; }

  public Long getProjectId() { return this.projectId; }

  public void setUserId(Long userId) { this.userId = userId; }

  public void setUserType(Integer userType) { this.userType = userType; }

  public void setProjectId(Long projectId) { this.projectId = projectId; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserProject)) {
      return false;
    }
    UserProject other = (UserProject)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$userId = this.getUserId();
    Long other$userId = other.getUserId();
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
    Long this$projectId = this.getProjectId();
    Long other$projectId = other.getProjectId();
    return !(this$projectId == null
                 ? other$projectId != null
                 : !((Object)this$projectId).equals(other$projectId));
  }

  protected boolean canEqual(Object other) {
    return other instanceof UserProject;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    Integer $userType = this.getUserType();
    result =
        result * 59 + ($userType == null ? 43 : ((Object)$userType).hashCode());
    Long $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    return result;
  }

  public String toString() {
    return "UserProject(userId=" + this.getUserId() +
        ", userType=" + this.getUserType() +
        ", projectId=" + this.getProjectId() + ")";
  }
}
