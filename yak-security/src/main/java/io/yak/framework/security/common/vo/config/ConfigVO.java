package io.yak.framework.security.common.vo.config;

import lombok.Data;

import java.util.Date;
/**
 * 系统配置视图对象。
 *
 * @author weifuwan
 */
@Data
public class ConfigVO {
  /** 主键标识。 */
  private Long id;
  /** 配置分组。 */
  private String valueGroup;
  /** 配置名称。 */
  private String valueName;
  /** 配置值。 */
  private String value;
  /** 状态。 */
  private Integer status;
  /** 备注。 */
  private String memo;
  /** 创建时间。 */
  private Date createTime;
  /** 最后更新时间。 */
  private Date updateTime;
  /** 操作人。 */
  private String operator;

  public Long getId() { return this.id; }

  public String getValueGroup() { return this.valueGroup; }

  public String getValueName() { return this.valueName; }

  public String getValue() { return this.value; }

  public Integer getStatus() { return this.status; }

  public String getMemo() { return this.memo; }

  public Date getCreateTime() { return this.createTime; }

  public Date getUpdateTime() { return this.updateTime; }

  public String getOperator() { return this.operator; }

  public void setId(Long id) { this.id = id; }

  public void setValueGroup(String valueGroup) { this.valueGroup = valueGroup; }

  public void setValueName(String valueName) { this.valueName = valueName; }

  public void setValue(String value) { this.value = value; }

  public void setStatus(Integer status) { this.status = status; }

  public void setMemo(String memo) { this.memo = memo; }

  public void setCreateTime(Date createTime) { this.createTime = createTime; }

  public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

  public void setOperator(String operator) { this.operator = operator; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ConfigVO)) {
      return false;
    }
    ConfigVO other = (ConfigVO)o;
    if (!other.canEqual(this)) {
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
    Date this$createTime = this.getCreateTime();
    Date other$createTime = other.getCreateTime();
    if (this$createTime == null
            ? other$createTime != null
            : !((Object)this$createTime).equals(other$createTime)) {
      return false;
    }
    Date this$updateTime = this.getUpdateTime();
    Date other$updateTime = other.getUpdateTime();
    if (this$updateTime == null
            ? other$updateTime != null
            : !((Object)this$updateTime).equals(other$updateTime)) {
      return false;
    }
    String this$operator = this.getOperator();
    String other$operator = other.getOperator();
    return !(this$operator == null ? other$operator != null
                                   : !this$operator.equals(other$operator));
  }

  protected boolean canEqual(Object other) { return other instanceof ConfigVO; }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
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
    Date $createTime = this.getCreateTime();
    result = result * 59 +
             ($createTime == null ? 43 : ((Object)$createTime).hashCode());
    Date $updateTime = this.getUpdateTime();
    result = result * 59 +
             ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
    String $operator = this.getOperator();
    result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
    return result;
  }

  public String toString() {
    return "ConfigVO(id=" + this.getId() +
        ", valueGroup=" + this.getValueGroup() +
        ", valueName=" + this.getValueName() + ", value=" + this.getValue() +
        ", status=" + this.getStatus() + ", memo=" + this.getMemo() +
        ", createTime=" + this.getCreateTime() +
        ", updateTime=" + this.getUpdateTime() +
        ", operator=" + this.getOperator() + ")";
  }

  public ConfigVO() {}

  public ConfigVO(Long id, String valueGroup, String valueName, String value,
                  Integer status, String memo, Date createTime, Date updateTime,
                  String operator) {
    this.id = id;
    this.valueGroup = valueGroup;
    this.valueName = valueName;
    this.value = value;
    this.status = status;
    this.memo = memo;
    this.createTime = createTime;
    this.updateTime = updateTime;
    this.operator = operator;
  }
}
