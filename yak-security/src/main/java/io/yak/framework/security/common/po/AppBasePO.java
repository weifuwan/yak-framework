package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

public class AppBasePO {
  @TableField(fill = FieldFill.INSERT) private String appName;

  public String getAppName() { return this.appName; }

  public void setAppName(String appName) { this.appName = appName; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AppBasePO)) {
      return false;
    }
    AppBasePO other = (AppBasePO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    String this$appName = this.getAppName();
    String other$appName = other.getAppName();
    return !(this$appName == null ? other$appName != null
                                  : !this$appName.equals(other$appName));
  }

  protected boolean canEqual(Object other) {
    return other instanceof AppBasePO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    String $appName = this.getAppName();
    result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
    return result;
  }

  public String toString() {
    return "AppBasePO(appName=" + this.getAppName() + ")";
  }
}
