package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.message.MessageDTO;
import io.yak.framework.security.common.vo.message.MessageVO;
import java.util.List;

/**
 * 消息服务接口。
 */
public interface MessageService {
  /**
   * 保存消息。
   */
  void saveMessage(MessageDTO var1);

  /**
   * 根据用户 ID 和已读标记查询消息。
   */
  List<MessageVO> getMessageListByUserIdAndReadTag(String var1,
                                                          Boolean var2);

  /**
   * 批量更新消息状态。
   */
  void changeMessageStatus(List<Long> var1);

  /**
   * 批量保存消息。
   */
  void saveMessages(List<MessageDTO> var1);
}
