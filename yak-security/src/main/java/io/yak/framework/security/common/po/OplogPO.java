package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.yak.framework.security.common.po.BasePO;

@TableName(value = "yak_security_oplog")
public class OplogPO extends BasePO {
  private String operatorIp;
  private String operator;
  private String operateType;
  private String target;
  private String targetType;
  private String detail;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof OplogPO)) {
      return false;
    }
    OplogPO other = (OplogPO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
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
    return !(this$detail == null ? other$detail != null
                                 : !this$detail.equals(other$detail));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof OplogPO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
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
    return result;
  }

  public String getOperatorIp() { return this.operatorIp; }

  public String getOperator() { return this.operator; }

  public String getOperateType() { return this.operateType; }

  public String getTarget() { return this.target; }

  public String getTargetType() { return this.targetType; }

  public String getDetail() { return this.detail; }

  public void setOperatorIp(String operatorIp) { this.operatorIp = operatorIp; }

  public void setOperator(String operator) { this.operator = operator; }

  public void setOperateType(String operateType) {
    this.operateType = operateType;
  }

  public void setTarget(String target) { this.target = target; }

  public void setTargetType(String targetType) { this.targetType = targetType; }

  public void setDetail(String detail) { this.detail = detail; }

  @Override
  public String toString() {
    return "OplogPO(operatorIp=" + this.getOperatorIp() +
        ", operator=" + this.getOperator() +
        ", operateType=" + this.getOperateType() +
        ", target=" + this.getTarget() +
        ", targetType=" + this.getTargetType() +
        ", detail=" + this.getDetail() + ")";
  }
}
