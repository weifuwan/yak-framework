package io.yak.framework.security.common.vo.resource;

import lombok.Data;
/**
 * 资源类型视图对象。
 *
 * @author weifuwan
 */
@Data
public class ResourceTypeVO {
  /** 主键标识。 */
  private Long id;
  /** 资源类型名称。 */
  private String typeName;

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ResourceTypeVO)) {
      return false;
    }
    ResourceTypeVO other = (ResourceTypeVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    String this$typeName = this.getTypeName();
    String other$typeName = other.getTypeName();
    return !(this$typeName == null ? other$typeName != null
                                   : !this$typeName.equals(other$typeName));
  }

  protected boolean canEqual(Object other) {
    return other instanceof ResourceTypeVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $typeName = this.getTypeName();
    result = result * 59 + ($typeName == null ? 43 : $typeName.hashCode());
    return result;
  }

  public String toString() {
    return "ResourceTypeVO(id=" + this.getId() +
        ", typeName=" + this.getTypeName() + ")";
  }
}
