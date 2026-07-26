package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户消息持久化对象。
 *
 * @author weifuwan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@TableName(value = "yak_security_message")
public class MessagePO extends BasePO {
  /** 消息标题。 */
  private String title;

  /** 消息内容。 */
  private String content;

  /** 消息是否已读。 */
  private Boolean readTag;

  /** 关联操作日志标识。 */
  private Long oplogId;

  /** 接收用户标识。 */
  private Long userId;
}
