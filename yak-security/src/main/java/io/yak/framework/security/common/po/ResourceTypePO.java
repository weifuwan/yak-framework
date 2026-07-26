package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.yak.framework.security.common.po.BasePO;

@TableName(value = "yak_security_resource_type")
public class ResourceTypePO extends BasePO {
  private String typeName;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ResourceTypePO)) {
      return false;
    }
    ResourceTypePO other = (ResourceTypePO)o;
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
    return other instanceof ResourceTypePO;
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
    return "ResourceTypePO(typeName=" + this.getTypeName() + ")";
  }
}
