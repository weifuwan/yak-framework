package io.yak.framework.security.common.vo.role;

import lombok.Data;
/**
 * 角色简要信息视图对象。
 *
 * @author weifuwan
 */
@Data
public class RoleBriefVO {
  /** 主键标识。 */
  private Long id;
  /** 角色名称。 */
  private String roleName;

  public Long getId() { return this.id; }

  public String getRoleName() { return this.roleName; }

  public void setId(Long id) { this.id = id; }

  public void setRoleName(String roleName) { this.roleName = roleName; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof RoleBriefVO)) {
      return false;
    }
    RoleBriefVO other = (RoleBriefVO)o;
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
    return !(this$roleName == null ? other$roleName != null
                                   : !this$roleName.equals(other$roleName));
  }

  protected boolean canEqual(Object other) {
    return other instanceof RoleBriefVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $roleName = this.getRoleName();
    result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
    return result;
  }

  public String toString() {
    return "RoleBriefVO(id=" + this.getId() +
        ", roleName=" + this.getRoleName() + ")";
  }
}
