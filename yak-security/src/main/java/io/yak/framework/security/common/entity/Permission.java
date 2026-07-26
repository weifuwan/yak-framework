package io.yak.framework.security.common.entity;

public class Permission {
  private Long id;
  private String permissionCode;
  private String permissionName;
  private Long parentId;
  private Boolean leaf;
  private Integer level;
  private String description;

  public Long getId() { return this.id; }


  public String getPermissionCode() { return this.permissionCode; }

  public void setPermissionCode(String permissionCode) {
    this.permissionCode = permissionCode;
  }

  public String getPermissionName() { return this.permissionName; }

  public Long getParentId() { return this.parentId; }

  public Boolean getLeaf() { return this.leaf; }

  public Integer getLevel() { return this.level; }

  public String getDescription() { return this.description; }

  public void setId(Long id) { this.id = id; }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }

  public void setParentId(Long parentId) { this.parentId = parentId; }

  public void setLeaf(Boolean leaf) { this.leaf = leaf; }

  public void setLevel(Integer level) { this.level = level; }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Permission)) {
      return false;
    }
    Permission other = (Permission)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Long this$parentId = this.getParentId();
    Long other$parentId = other.getParentId();
    if (this$parentId == null
            ? other$parentId != null
            : !((Object)this$parentId).equals(other$parentId)) {
      return false;
    }
    Boolean this$leaf = this.getLeaf();
    Boolean other$leaf = other.getLeaf();
    if (this$leaf == null ? other$leaf != null
                          : !((Object)this$leaf).equals(other$leaf)) {
      return false;
    }
    Integer this$level = this.getLevel();
    Integer other$level = other.getLevel();
    if (this$level == null ? other$level != null
                           : !((Object)this$level).equals(other$level)) {
      return false;
    }
    String this$permissionName = this.getPermissionName();
    String other$permissionName = other.getPermissionName();
    if (this$permissionName == null
            ? other$permissionName != null
            : !this$permissionName.equals(other$permissionName)) {
      return false;
    }
    String this$description = this.getDescription();
    String other$description = other.getDescription();
    return !(this$description == null
                 ? other$description != null
                 : !this$description.equals(other$description));
  }

  protected boolean canEqual(Object other) {
    return other instanceof Permission;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Long $parentId = this.getParentId();
    result =
        result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
    Boolean $leaf = this.getLeaf();
    result = result * 59 + ($leaf == null ? 43 : ((Object)$leaf).hashCode());
    Integer $level = this.getLevel();
    result = result * 59 + ($level == null ? 43 : ((Object)$level).hashCode());
    String $permissionName = this.getPermissionName();
    result = result * 59 +
             ($permissionName == null ? 43 : $permissionName.hashCode());
    String $description = this.getDescription();
    result =
        result * 59 + ($description == null ? 43 : $description.hashCode());
    return result;
  }

  public String toString() {
    return "Permission(id=" + this.getId() +
        ", permissionName=" + this.getPermissionName() +
        ", parentId=" + this.getParentId() + ", leaf=" + this.getLeaf() +
        ", level=" + this.getLevel() +
        ", description=" + this.getDescription() + ")";
  }
}