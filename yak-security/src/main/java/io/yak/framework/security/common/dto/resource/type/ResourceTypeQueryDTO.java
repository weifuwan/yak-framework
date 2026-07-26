package io.yak.framework.security.common.dto.resource.type;

import io.yak.framework.security.common.dto.PageParamDTO;
import io.yak.framework.security.common.dto.resource.MByRQueryDTO;

/**
 * 资源类型查询数据传输对象。
 *
 * @author weifuwan
 */
public class ResourceTypeQueryDTO extends PageParamDTO {
  /** 资源类型名称。 */
  private String typeName;

  public ResourceTypeQueryDTO(MByRQueryDTO queryDTO) {
    this.setPage(queryDTO.getPage());
    this.setSize(queryDTO.getSize());
    this.typeName = queryDTO.getName();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ResourceTypeQueryDTO)) {
      return false;
    }
    ResourceTypeQueryDTO other = (ResourceTypeQueryDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    String this$typeName = this.getTypeName();
    String other$typeName = other.getTypeName();
    return !(this$typeName == null ? other$typeName != null
                                   : !this$typeName.equals(other$typeName));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof ResourceTypeQueryDTO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    String $typeName = this.getTypeName();
    result = result * 59 + ($typeName == null ? 43 : $typeName.hashCode());
    return result;
  }

  public String getTypeName() { return this.typeName; }

  public void setTypeName(String typeName) { this.typeName = typeName; }

  @Override
  public String toString() {
    return "ResourceTypeQueryDTO(typeName=" + this.getTypeName() + ")";
  }
}
