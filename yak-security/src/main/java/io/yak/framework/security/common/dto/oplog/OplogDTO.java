package io.yak.framework.security.common.dto.oplog;

import lombok.Data;

/**
 * 操作日志数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class OplogDTO {
  /** 操作人。 */
  private String operator;
  /** 操作类型。 */
  private String operateType;
  /** 操作目标类型。 */
  private String targetType;
  /** 操作目标。 */
  private String target;
  /** 操作详情。 */
  private String detail;
  /** 操作方法列表。 */
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

}
