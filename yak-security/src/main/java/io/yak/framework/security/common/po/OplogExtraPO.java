package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 操作日志扩展信息持久化对象。
 *
 * @author weifuwan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@TableName(value = "yak_security_oplog_extra")
public class OplogExtraPO extends BasePO {
  /** 扩展信息内容。 */
  private String info;

  /** 扩展信息类型。 */
  private Integer type;
}
