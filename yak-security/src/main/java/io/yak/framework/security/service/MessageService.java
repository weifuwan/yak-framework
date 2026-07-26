package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.message.MessageDTO;
import io.yak.framework.security.common.vo.message.MessageVO;
import java.util.List;

public interface MessageService {
  public void saveMessage(MessageDTO var1);

  public List<MessageVO> getMessageListByUserIdAndReadTag(String var1,
                                                          Boolean var2);

  public void changeMessageStatus(List<Integer> var1);

  public void saveMessages(List<MessageDTO> var1);
}
