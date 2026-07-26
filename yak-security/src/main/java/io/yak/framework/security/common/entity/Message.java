package io.yak.framework.security.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息实体。
 *
 * @author weifuwan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends BaseEntity {
  /** 消息标题。 */
  private String title;
  /** 消息内容。 */
  private String content;
  /** 已读标记。 */
  private Boolean readTag;
  /** 用户标识。 */
  private Long userId;
  /** 操作日志标识。 */
  private Long oplogId;

}
