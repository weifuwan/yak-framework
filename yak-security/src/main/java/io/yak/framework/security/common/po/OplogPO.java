package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 操作日志持久化对象。
 *
 * @author weifuwan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@TableName(value = "yak_security_oplog")
public class OplogPO extends BasePO {
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
}
