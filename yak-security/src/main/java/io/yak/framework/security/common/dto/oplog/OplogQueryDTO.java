package io.yak.framework.security.common.dto.oplog;

import lombok.Data;

import io.yak.framework.security.common.dto.PageParamDTO;
/**
 * 操作日志查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class OplogQueryDTO extends PageParamDTO {
  /** 操作类型。 */
  private String operateType;
  /** 操作详情。 */
  private String detail;
  /** 操作人。 */
  private String operator;
  /** 操作目标。 */
  private String target;
  /** 操作目标类型。 */
  private String targetType;
  /** 操作方法列表。 */
  private String operationMethods;
  /** 开始时间。 */
  private Long startTime;
  /** 结束时间。 */
  private Long endTime;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof OplogQueryDTO)) {
      return false;
    }
    OplogQueryDTO other = (OplogQueryDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Long this$startTime = this.getStartTime();
    Long other$startTime = other.getStartTime();
    if (this$startTime == null
            ? other$startTime != null
            : !((Object)this$startTime).equals(other$startTime)) {
      return false;
    }
    Long this$endTime = this.getEndTime();
    Long other$endTime = other.getEndTime();
    if (this$endTime == null ? other$endTime != null
                             : !((Object)this$endTime).equals(other$endTime)) {
      return false;
    }
    String this$operateType = this.getOperateType();
    String other$operateType = other.getOperateType();
    if (this$operateType == null
            ? other$operateType != null
            : !this$operateType.equals(other$operateType)) {
      return false;
    }
    String this$detail = this.getDetail();
    String other$detail = other.getDetail();
    if (this$detail == null ? other$detail != null
                            : !this$detail.equals(other$detail)) {
      return false;
    }
    String this$operator = this.getOperator();
    String other$operator = other.getOperator();
    if (this$operator == null ? other$operator != null
                              : !this$operator.equals(other$operator)) {
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
    String this$operationMethods = this.getOperationMethods();
    String other$operationMethods = other.getOperationMethods();
    return !(this$operationMethods == null
                 ? other$operationMethods != null
                 : !this$operationMethods.equals(other$operationMethods));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof OplogQueryDTO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Long $startTime = this.getStartTime();
    result = result * 59 +
             ($startTime == null ? 43 : ((Object)$startTime).hashCode());
    Long $endTime = this.getEndTime();
    result =
        result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
    String $operateType = this.getOperateType();
    result =
        result * 59 + ($operateType == null ? 43 : $operateType.hashCode());
    String $detail = this.getDetail();
    result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
    String $operator = this.getOperator();
    result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
    String $target = this.getTarget();
    result = result * 59 + ($target == null ? 43 : $target.hashCode());
    String $targetType = this.getTargetType();
    result = result * 59 + ($targetType == null ? 43 : $targetType.hashCode());
    String $operationMethods = this.getOperationMethods();
    result = result * 59 +
             ($operationMethods == null ? 43 : $operationMethods.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "OplogQueryDTO(operateType=" + this.getOperateType() +
        ", detail=" + this.getDetail() + ", operator=" + this.getOperator() +
        ", target=" + this.getTarget() +
        ", targetType=" + this.getTargetType() +
        ", operationMethods=" + this.getOperationMethods() +
        ", startTime=" + this.getStartTime() +
        ", endTime=" + this.getEndTime() + ")";
  }
}
