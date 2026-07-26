package io.yak.framework.security.common.dto.role;

import lombok.Data;

import java.util.List;
/**
 * 角色保存数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class RoleSaveDTO {
  /** 标识。 */
  private Long id;
  /** 角色名称。 */
  private String roleName;
  /** 描述。 */
  private String description;
  /** 权限标识列表。 */
  private List<Long> permissionIdList;

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
    Long this$id = this.getId();
    Long other$id = other.getId();
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
    List<Long> this$permissionIdList = this.getPermissionIdList();
    List<Long> other$permissionIdList = other.getPermissionIdList();
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
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $roleName = this.getRoleName();
    result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
    String $description = this.getDescription();
    result =
        result * 59 + ($description == null ? 43 : $description.hashCode());
    List<Long> $permissionIdList = this.getPermissionIdList();
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
