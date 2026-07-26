package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.yak.framework.security.common.po.BasePO;

@TableName(value = "yak_security_oplog_extra")
public class OplogExtraPO extends BasePO {
  private String info;
  private Integer type;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof OplogExtraPO)) {
      return false;
    }
    OplogExtraPO other = (OplogExtraPO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Integer this$type = this.getType();
    Integer other$type = other.getType();
    if (this$type == null ? other$type != null
                          : !((Object)this$type).equals(other$type)) {
      return false;
    }
    String this$info = this.getInfo();
    String other$info = other.getInfo();
    return !(this$info == null ? other$info != null
                               : !this$info.equals(other$info));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof OplogExtraPO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Integer $type = this.getType();
    result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
    String $info = this.getInfo();
    result = result * 59 + ($info == null ? 43 : $info.hashCode());
    return result;
  }

  public String getInfo() { return this.info; }

  public Integer getType() { return this.type; }

  public void setInfo(String info) { this.info = info; }

  public void setType(Integer type) { this.type = type; }

  @Override
  public String toString() {
    return "OplogExtraPO(info=" + this.getInfo() + ", type=" + this.getType() +
        ")";
  }
}
