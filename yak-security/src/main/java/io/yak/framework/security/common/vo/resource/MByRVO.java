package io.yak.framework.security.common.vo.resource;
public class MByRVO {
  private Integer adminUserCnt;
  private Integer viewUserCnt;
  private Integer projectId;
  private String projectCode;
  private String projectName;
  private Integer resourceTypeId;
  private String resourceTypeName;
  private Integer resourceId;
  private String resourceName;

  public Integer getAdminUserCnt() { return this.adminUserCnt; }

  public Integer getViewUserCnt() { return this.viewUserCnt; }

  public Integer getProjectId() { return this.projectId; }

  public String getProjectCode() { return this.projectCode; }

  public String getProjectName() { return this.projectName; }

  public Integer getResourceTypeId() { return this.resourceTypeId; }

  public String getResourceTypeName() { return this.resourceTypeName; }

  public Integer getResourceId() { return this.resourceId; }

  public String getResourceName() { return this.resourceName; }

  public void setAdminUserCnt(Integer adminUserCnt) {
    this.adminUserCnt = adminUserCnt;
  }

  public void setViewUserCnt(Integer viewUserCnt) {
    this.viewUserCnt = viewUserCnt;
  }

  public void setProjectId(Integer projectId) { this.projectId = projectId; }

  public void setProjectCode(String projectCode) {
    this.projectCode = projectCode;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public void setResourceTypeId(Integer resourceTypeId) {
    this.resourceTypeId = resourceTypeId;
  }

  public void setResourceTypeName(String resourceTypeName) {
    this.resourceTypeName = resourceTypeName;
  }

  public void setResourceId(Integer resourceId) {
    this.resourceId = resourceId;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MByRVO)) {
      return false;
    }
    MByRVO other = (MByRVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Integer this$adminUserCnt = this.getAdminUserCnt();
    Integer other$adminUserCnt = other.getAdminUserCnt();
    if (this$adminUserCnt == null
            ? other$adminUserCnt != null
            : !((Object)this$adminUserCnt).equals(other$adminUserCnt)) {
      return false;
    }
    Integer this$viewUserCnt = this.getViewUserCnt();
    Integer other$viewUserCnt = other.getViewUserCnt();
    if (this$viewUserCnt == null
            ? other$viewUserCnt != null
            : !((Object)this$viewUserCnt).equals(other$viewUserCnt)) {
      return false;
    }
    Integer this$projectId = this.getProjectId();
    Integer other$projectId = other.getProjectId();
    if (this$projectId == null
            ? other$projectId != null
            : !((Object)this$projectId).equals(other$projectId)) {
      return false;
    }
    Integer this$resourceTypeId = this.getResourceTypeId();
    Integer other$resourceTypeId = other.getResourceTypeId();
    if (this$resourceTypeId == null
            ? other$resourceTypeId != null
            : !((Object)this$resourceTypeId).equals(other$resourceTypeId)) {
      return false;
    }
    Integer this$resourceId = this.getResourceId();
    Integer other$resourceId = other.getResourceId();
    if (this$resourceId == null
            ? other$resourceId != null
            : !((Object)this$resourceId).equals(other$resourceId)) {
      return false;
    }
    String this$projectCode = this.getProjectCode();
    String other$projectCode = other.getProjectCode();
    if (this$projectCode == null
            ? other$projectCode != null
            : !this$projectCode.equals(other$projectCode)) {
      return false;
    }
    String this$projectName = this.getProjectName();
    String other$projectName = other.getProjectName();
    if (this$projectName == null
            ? other$projectName != null
            : !this$projectName.equals(other$projectName)) {
      return false;
    }
    String this$resourceTypeName = this.getResourceTypeName();
    String other$resourceTypeName = other.getResourceTypeName();
    if (this$resourceTypeName == null
            ? other$resourceTypeName != null
            : !this$resourceTypeName.equals(other$resourceTypeName)) {
      return false;
    }
    String this$resourceName = this.getResourceName();
    String other$resourceName = other.getResourceName();
    return !(this$resourceName == null
                 ? other$resourceName != null
                 : !this$resourceName.equals(other$resourceName));
  }

  protected boolean canEqual(Object other) { return other instanceof MByRVO; }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $adminUserCnt = this.getAdminUserCnt();
    result = result * 59 +
             ($adminUserCnt == null ? 43 : ((Object)$adminUserCnt).hashCode());
    Integer $viewUserCnt = this.getViewUserCnt();
    result = result * 59 +
             ($viewUserCnt == null ? 43 : ((Object)$viewUserCnt).hashCode());
    Integer $projectId = this.getProjectId();
    result = result * 59 +
             ($projectId == null ? 43 : ((Object)$projectId).hashCode());
    Integer $resourceTypeId = this.getResourceTypeId();
    result =
        result * 59 +
        ($resourceTypeId == null ? 43 : ((Object)$resourceTypeId).hashCode());
    Integer $resourceId = this.getResourceId();
    result = result * 59 +
             ($resourceId == null ? 43 : ((Object)$resourceId).hashCode());
    String $projectCode = this.getProjectCode();
    result =
        result * 59 + ($projectCode == null ? 43 : $projectCode.hashCode());
    String $projectName = this.getProjectName();
    result =
        result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
    String $resourceTypeName = this.getResourceTypeName();
    result = result * 59 +
             ($resourceTypeName == null ? 43 : $resourceTypeName.hashCode());
    String $resourceName = this.getResourceName();
    result =
        result * 59 + ($resourceName == null ? 43 : $resourceName.hashCode());
    return result;
  }

  public String toString() {
    return "MByRVO(adminUserCnt=" + this.getAdminUserCnt() +
        ", viewUserCnt=" + this.getViewUserCnt() +
        ", projectId=" + this.getProjectId() +
        ", projectCode=" + this.getProjectCode() +
        ", projectName=" + this.getProjectName() +
        ", resourceTypeId=" + this.getResourceTypeId() +
        ", resourceTypeName=" + this.getResourceTypeName() +
        ", resourceId=" + this.getResourceId() +
        ", resourceName=" + this.getResourceName() + ")";
  }
}
