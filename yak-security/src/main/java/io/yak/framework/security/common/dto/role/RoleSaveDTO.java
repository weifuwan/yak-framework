package io.yak.framework.security.common.dto.role;

import java.util.List;
public class RoleSaveDTO {
  private Integer id;
  private String roleName;
  private String description;
  private List<Integer> permissionIdList;

  public Integer getId() { return this.id; }

  public String getRoleName() { return this.roleName; }

  public String getDescription() { return this.description; }

  public List<Integer> getPermissionIdList() { return this.permissionIdList; }

  public void setId(Integer id) { this.id = id; }

  public void setRoleName(String roleName) { this.roleName = roleName; }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setPermissionIdList(List<Integer> permissionIdList) {
    this.permissionIdList = permissionIdList;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof RoleSaveDTO)) {
      return false;
    }
    RoleSaveDTO other = (RoleSaveDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Integer this$id = this.getId();
    Integer other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    String this$roleName = this.getRoleName();
    String other$roleName = other.getRoleName();
    if (this$roleName == null ? other$roleName != null
                              : !this$roleName.equals(other$roleName)) {
      return false;
    }
    String this$description = this.getDescription();
    String other$description = other.getDescription();
    if (this$description == null
            ? other$description != null
            : !this$description.equals(other$description)) {
      return false;
    }
    List<Integer> this$permissionIdList = this.getPermissionIdList();
    List<Integer> other$permissionIdList = other.getPermissionIdList();
    return !(
        this$permissionIdList == null
            ? other$permissionIdList != null
            : !((Object)this$permissionIdList).equals(other$permissionIdList));
  }

  protected boolean canEqual(Object other) {
    return other instanceof RoleSaveDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $roleName = this.getRoleName();
    result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
    String $description = this.getDescription();
    result =
        result * 59 + ($description == null ? 43 : $description.hashCode());
    List<Integer> $permissionIdList = this.getPermissionIdList();
    result = result * 59 + ($permissionIdList == null
                                ? 43
                                : ((Object)$permissionIdList).hashCode());
    return result;
  }

  public String toString() {
    return "RoleSaveDTO(id=" + this.getId() +
        ", roleName=" + this.getRoleName() +
        ", description=" + this.getDescription() +
        ", permissionIdList=" + this.getPermissionIdList() + ")";
  }
}
