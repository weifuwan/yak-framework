package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.yak.framework.security.common.po.BasePO;

@TableName(value = "yak_security_config")
public class ConfigPO extends BasePO {
  @TableId(type = IdType.AUTO) private Long id;
  private String valueGroup;
  private String valueName;
  private String value;
  private Integer status;
  private String memo;
  private String operator;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ConfigPO)) {
      return false;
    }
    ConfigPO other = (ConfigPO)o;
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
    Integer this$status = this.getStatus();
    Integer other$status = other.getStatus();
    if (this$status == null ? other$status != null
                            : !((Object)this$status).equals(other$status)) {
      return false;
    }
    String this$valueGroup = this.getValueGroup();
    String other$valueGroup = other.getValueGroup();
    if (this$valueGroup == null ? other$valueGroup != null
                                : !this$valueGroup.equals(other$valueGroup)) {
      return false;
    }
    String this$valueName = this.getValueName();
    String other$valueName = other.getValueName();
    if (this$valueName == null ? other$valueName != null
                               : !this$valueName.equals(other$valueName)) {
      return false;
    }
    String this$value = this.getValue();
    String other$value = other.getValue();
    if (this$value == null ? other$value != null
                           : !this$value.equals(other$value)) {
      return false;
    }
    String this$memo = this.getMemo();
    String other$memo = other.getMemo();
    if (this$memo == null ? other$memo != null
                          : !this$memo.equals(other$memo)) {
      return false;
    }
    String this$operator = this.getOperator();
    String other$operator = other.getOperator();
    return !(this$operator == null ? other$operator != null
                                   : !this$operator.equals(other$operator));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof ConfigPO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Integer $status = this.getStatus();
    result =
        result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
    String $valueGroup = this.getValueGroup();
    result = result * 59 + ($valueGroup == null ? 43 : $valueGroup.hashCode());
    String $valueName = this.getValueName();
    result = result * 59 + ($valueName == null ? 43 : $valueName.hashCode());
    String $value = this.getValue();
    result = result * 59 + ($value == null ? 43 : $value.hashCode());
    String $memo = this.getMemo();
    result = result * 59 + ($memo == null ? 43 : $memo.hashCode());
    String $operator = this.getOperator();
    result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
    return result;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  public String getValueGroup() { return this.valueGroup; }

  public String getValueName() { return this.valueName; }

  public String getValue() { return this.value; }

  public Integer getStatus() { return this.status; }

  public String getMemo() { return this.memo; }

  public String getOperator() { return this.operator; }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public void setValueGroup(String valueGroup) { this.valueGroup = valueGroup; }

  public void setValueName(String valueName) { this.valueName = valueName; }

  public void setValue(String value) { this.value = value; }

  public void setStatus(Integer status) { this.status = status; }

  public void setMemo(String memo) { this.memo = memo; }

  public void setOperator(String operator) { this.operator = operator; }

  @Override
  public String toString() {
    return "ConfigPO(id=" + this.getId() +
        ", valueGroup=" + this.getValueGroup() +
        ", valueName=" + this.getValueName() + ", value=" + this.getValue() +
        ", status=" + this.getStatus() + ", memo=" + this.getMemo() +
        ", operator=" + this.getOperator() + ")";
  }
}
