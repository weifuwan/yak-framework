package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.message.MessageDTO;
import io.yak.framework.security.common.vo.message.MessageVO;

import java.util.List;

/**
 * 消息服务接口。
 *
 * @author weifuwan
 */
public interface MessageService {

  /**
   * 保存单条消息。
   *
   * @param messageDTO 消息信息
   */
  void saveMessage(
          MessageDTO messageDTO);

  /**
   * 根据用户名和已读状态查询消息。
   *
   * <p>已读状态为空时查询该用户的全部消息。
   *
   * @param username 用户名
   * @param readTag 已读状态
   * @return 消息列表
   */
  List<MessageVO> getMessageListByUsernameAndReadTag(
          String username,
          Boolean readTag);

  /**
   * 批量切换消息已读状态。
   *
   * @param messageIdList 消息 ID 列表
   */
  void changeMessageStatus(
          List<Long> messageIdList);

  /**
   * 批量保存消息。
   *
   * @param messageDTOList 消息列表
   */
  void saveMessages(
          List<MessageDTO> messageDTOList);
}