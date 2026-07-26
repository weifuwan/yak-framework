package io.yak.framework.security.common.dto.role;

import lombok.Data;

import io.yak.framework.security.common.dto.PageParamDTO;
/**
 * 角色查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class RoleQueryDTO extends PageParamDTO {
  /** 角色编码。 */
  private String roleCode;
  /** 标识。 */
  private Long id;
  /** 角色名称。 */
  private String roleName;
  /** 描述。 */
  private String description;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof RoleQueryDTO)) {
      return false;
    }
    RoleQueryDTO other = (RoleQueryDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    String this$roleCode = this.getRoleCode();
    String other$roleCode = other.getRoleCode();
    if (this$roleCode == null ? other$roleCode != null
                              : !this$roleCode.equals(other$roleCode)) {
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
    return !(this$description == null
                 ? other$description != null
                 : !this$description.equals(other$description));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof RoleQueryDTO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $roleCode = this.getRoleCode();
    result = result * 59 + ($roleCode == null ? 43 : $roleCode.hashCode());
    String $roleName = this.getRoleName();
    result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
    String $description = this.getDescription();
    result =
        result * 59 + ($description == null ? 43 : $description.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "RoleQueryDTO(roleCode=" + this.getRoleCode() +
        ", id=" + this.getId() + ", roleName=" + this.getRoleName() +
        ", description=" + this.getDescription() + ")";
  }
}
