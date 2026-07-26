package io.yak.framework.security.common.dto.user;

public class UserProjectDTO {
  private Integer id;
  private Integer userId;
  private Integer userType;
  private Integer projectId;
  private Boolean isDelete;

  public Integer getId() { return this.id; }

  public Integer getUserId() { return this.userId; }

  public Integer getUserType() { return this.userType; }

  public Integer getProjectId() { return this.projectId; }

  public Boolean getIsDelete() { return this.isDelete; }

  public void setId(Integer id) { this.id = id; }

  public void setUserId(Integer userId) { this.userId = userId; }

  public void setUserType(Integer userType) { this.userType = userType; }

  public void setProjectId(Integer projectId) { this.projectId = projectId; }

  public void setIsDelete(Boolean isDelete) { this.isDelete = isDelete; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof UserProjectDTO)) {
      return false;
    }
    UserProjectDTO other = (UserProjectDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Integer this$id = this.getId();
    Integer other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
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
    if (this$projectId == null
            ? other$projectId != null
            : !((Object)this$projectId).equals(other$projectId)) {
      return false;
    }
    Boolean this$isDelete = this.getIsDelete();
    Boolean other$isDelete = other.getIsDelete();
    return !(this$isDelete == null
                 ? other$isDelete != null
                 : !((Object)this$isDelete).equals(other$isDelete));
  }

  protected boolean canEqual(Object other) {
    return other instanceof UserProjectDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Integer $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    Integer $userType = this.getUserType();
    result =
        result * 59 + ($userType == null ? 43 : ((Object)$userType).hashCode());
    Integer $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    Boolean $isDelete = this.getIsDelete();
    result =
        result * 59 + ($isDelete == null ? 43 : ((Object)$isDelete).hashCode());
    return result;
  }

  public String toString() {
    return "UserProjectDTO(id=" + this.getId() +
        ", userId=" + this.getUserId() + ", userType=" + this.getUserType() +
        ", projectId=" + this.getProjectId() +
        ", isDelete=" + this.getIsDelete() + ")";
  }
}
