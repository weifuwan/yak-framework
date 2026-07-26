package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.message.MessageDTO;
import io.yak.framework.security.common.vo.message.MessageVO;
import java.util.List;

/**
 * 消息服务接口。
 */
public interface MessageService {
  void saveMessage(MessageDTO var1);

  List<MessageVO> getMessageListByUserIdAndReadTag(String var1,
                                                          Boolean var2);

  void changeMessageStatus(List<Long> var1);

  void saveMessages(List<MessageDTO> var1);
}
