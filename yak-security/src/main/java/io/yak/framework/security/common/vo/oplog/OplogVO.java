package io.yak.framework.security.common.vo.oplog;

import lombok.Data;

import java.util.Date;
/**
 * 操作日志视图对象。
 *
 * @author weifuwan
 */
@Data
public class OplogVO {
  /** 主键标识。 */
  private Long id;
  /** 操作人 IP 地址。 */
  private String operatorIp;
  /** 操作人。 */
  private String operator;
  /** 操作类型。 */
  private String operateType;
  /** 操作目标。 */
  private String target;
  /** 操作目标类型。 */
  private String targetType;
  /** 操作详情。 */
  private String detail;
  /** 创建时间。 */
  private Date createTime;
  /** 最后更新时间。 */
  private Date updateTime;

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof OplogVO)) {
      return false;
    }
    OplogVO other = (OplogVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    String this$operatorIp = this.getOperatorIp();
    String other$operatorIp = other.getOperatorIp();
    if (this$operatorIp == null ? other$operatorIp != null
                                : !this$operatorIp.equals(other$operatorIp)) {
      return false;
    }
    String this$operator = this.getOperator();
    String other$operator = other.getOperator();
    if (this$operator == null ? other$operator != null
                              : !this$operator.equals(other$operator)) {
      return false;
    }
    String this$operateType = this.getOperateType();
    String other$operateType = other.getOperateType();
    if (this$operateType == null
            ? other$operateType != null
            : !this$operateType.equals(other$operateType)) {
      return false;
    }
    String this$target = this.getTarget();
    String other$target = other.getTarget();
    if (this$target == null ? other$target != null
                            : !this$target.equals(other$target)) {
      return false;
    }
    String this$targetType = this.getTargetType();
    String other$targetType = other.getTargetType();
    if (this$targetType == null ? other$targetType != null
                                : !this$targetType.equals(other$targetType)) {
      return false;
    }
    String this$detail = this.getDetail();
    String other$detail = other.getDetail();
    if (this$detail == null ? other$detail != null
                            : !this$detail.equals(other$detail)) {
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
    return !(this$updateTime == null
                 ? other$updateTime != null
                 : !((Object)this$updateTime).equals(other$updateTime));
  }

  protected boolean canEqual(Object other) { return other instanceof OplogVO; }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $operatorIp = this.getOperatorIp();
    result = result * 59 + ($operatorIp == null ? 43 : $operatorIp.hashCode());
    String $operator = this.getOperator();
    result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
    String $operateType = this.getOperateType();
    result =
        result * 59 + ($operateType == null ? 43 : $operateType.hashCode());
    String $target = this.getTarget();
    result = result * 59 + ($target == null ? 43 : $target.hashCode());
    String $targetType = this.getTargetType();
    result = result * 59 + ($targetType == null ? 43 : $targetType.hashCode());
    String $detail = this.getDetail();
    result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
    Date $createTime = this.getCreateTime();
    result = result * 59 +
             ($createTime == null ? 43 : ((Object)$createTime).hashCode());
    Date $updateTime = this.getUpdateTime();
    result = result * 59 +
             ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
    return result;
  }

  public String toString() {
    return "OplogVO(id=" + this.getId() +
        ", operatorIp=" + this.getOperatorIp() +
        ", operator=" + this.getOperator() +
        ", operateType=" + this.getOperateType() +
        ", target=" + this.getTarget() +
        ", targetType=" + this.getTargetType() +
        ", detail=" + this.getDetail() +
        ", createTime=" + this.getCreateTime() +
        ", updateTime=" + this.getUpdateTime() + ")";
  }
}
