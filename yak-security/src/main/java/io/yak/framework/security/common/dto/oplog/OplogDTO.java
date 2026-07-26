package io.yak.framework.security.common.dto.oplog;

public class OplogDTO {
  private String operator;
  private String operateType;
  private String targetType;
  private String target;
  private String detail;
  private String operationMethods;

  public OplogDTO(String operator, String operateType, String targetType,
                  String target, String detail, String operationMethods) {
    this.operator = operator;
    this.operateType = operateType;
    this.targetType = targetType;
    this.target = target;
    this.detail = detail;
    this.operationMethods = operationMethods;
  }

  public OplogDTO() {}

  public OplogDTO(String operator, String operateType, String targetType,
                  String target, String detail) {
    this.operator = operator;
    this.operateType = operateType;
    this.targetType = targetType;
    this.target = target;
    this.detail = detail;
  }

  public OplogDTO(String operator, String operateType, String targetType,
                  String target) {
    this.operator = operator;
    this.operateType = operateType;
    this.targetType = targetType;
    this.target = target;
  }

  public String getOperator() { return this.operator; }

  public String getOperateType() { return this.operateType; }

  public String getTargetType() { return this.targetType; }

  public String getTarget() { return this.target; }

  public String getDetail() { return this.detail; }

  public String getOperationMethods() { return this.operationMethods; }

  public void setOperator(String operator) { this.operator = operator; }

  public void setOperateType(String operateType) {
    this.operateType = operateType;
  }

  public void setTargetType(String targetType) { this.targetType = targetType; }

  public void setTarget(String target) { this.target = target; }

  public void setDetail(String detail) { this.detail = detail; }

  public void setOperationMethods(String operationMethods) {
    this.operationMethods = operationMethods;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof OplogDTO)) {
      return false;
    }
    OplogDTO other = (OplogDTO)o;
    if (!other.canEqual(this)) {
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
    String this$targetType = this.getTargetType();
    String other$targetType = other.getTargetType();
    if (this$targetType == null ? other$targetType != null
                                : !this$targetType.equals(other$targetType)) {
      return false;
    }
    String this$target = this.getTarget();
    String other$target = other.getTarget();
    if (this$target == null ? other$target != null
                            : !this$target.equals(other$target)) {
      return false;
    }
    String this$detail = this.getDetail();
    String other$detail = other.getDetail();
    if (this$detail == null ? other$detail != null
                            : !this$detail.equals(other$detail)) {
      return false;
    }
    String this$operationMethods = this.getOperationMethods();
    String other$operationMethods = other.getOperationMethods();
    return !(this$operationMethods == null
                 ? other$operationMethods != null
                 : !this$operationMethods.equals(other$operationMethods));
  }

  protected boolean canEqual(Object other) { return other instanceof OplogDTO; }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    String $operator = this.getOperator();
    result = result * 59 + ($operator == null ? 43 : $operator.hashCode());
    String $operateType = this.getOperateType();
    result =
        result * 59 + ($operateType == null ? 43 : $operateType.hashCode());
    String $targetType = this.getTargetType();
    result = result * 59 + ($targetType == null ? 43 : $targetType.hashCode());
    String $target = this.getTarget();
    result = result * 59 + ($target == null ? 43 : $target.hashCode());
    String $detail = this.getDetail();
    result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
    String $operationMethods = this.getOperationMethods();
    result = result * 59 +
             ($operationMethods == null ? 43 : $operationMethods.hashCode());
    return result;
  }

  public String toString() {
    return "OplogDTO(operator=" + this.getOperator() +
        ", operateType=" + this.getOperateType() +
        ", targetType=" + this.getTargetType() +
        ", target=" + this.getTarget() + ", detail=" + this.getDetail() +
        ", operationMethods=" + this.getOperationMethods() + ")";
  }
}
