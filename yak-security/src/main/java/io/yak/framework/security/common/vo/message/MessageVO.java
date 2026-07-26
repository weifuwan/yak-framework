package io.yak.framework.security.common.vo.message;

import lombok.Data;
/**
 * 消息视图对象。
 *
 * @author weifuwan
 */
@Data
public class MessageVO {
  /** 主键标识。 */
  private Long id;
  /** 消息标题。 */
  private String title;
  /** 消息内容。 */
  private String content;
  /** 是否已读。 */
  private Boolean readTag;
  /** 创建时间。 */
  private Long createTime;
  /** 关联的操作日志标识。 */
  private Long oplogId;

}
