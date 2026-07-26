package io.yak.framework.security.common.dto.message;

import lombok.Data;

/**
 * 消息数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class MessageDTO {
  /** 消息标题。 */
  private String title;
  /** 消息内容。 */
  private String content;
  /** 操作日志标识。 */
  private Long oplogId;
  /** 用户标识。 */
  private Long userId;

  public MessageDTO(Long userId, Long oplogId) {
    this.userId = userId;
    this.oplogId = oplogId;
  }

}
