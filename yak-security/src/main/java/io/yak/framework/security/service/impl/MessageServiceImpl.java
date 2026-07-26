package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.dto.message.MessageDTO;
import io.yak.framework.security.common.entity.Message;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.message.MessageVO;
import io.yak.framework.security.dao.MessageDao;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.MessageService;
import io.yak.framework.security.util.CopyBeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 消息服务实现类。
 *
 * @author weifuwan
 */
@Service("yakSecurityMessageServiceImpl")
public class MessageServiceImpl implements MessageService {

  private final MessageDao messageDao;

  private final UserDao userDao;

  /**
   * 创建消息服务。
   *
   * @param messageDao 消息数据访问对象
   * @param userDao 用户数据访问对象
   */
  public MessageServiceImpl(
          MessageDao messageDao,
          UserDao userDao) {

    this.messageDao = messageDao;
    this.userDao = userDao;
  }

  /**
   * 保存单条消息。
   *
   * @param messageDTO 消息信息
   */
  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void saveMessage(
          MessageDTO messageDTO) {

    if (messageDTO == null) {
      return;
    }

    Message message =
            convertToEntity(messageDTO);

    messageDao.insert(message);
  }

  /**
   * 根据用户名和已读状态查询消息。
   *
   * @param username 用户名
   * @param readTag 已读状态
   * @return 消息列表
   */
  @Override
  public List<MessageVO> getMessageListByUsernameAndReadTag(
          String username,
          Boolean readTag) {

    if (!StringUtils.hasText(username)) {
      return new ArrayList<>();
    }

    User user =
            userDao.selectByUsername(
                    username.trim());

    if (user == null) {
      throw new YakSecurityException(
              ResultCode.USER_NOT_EXISTS);
    }

    List<Message> messageList =
            messageDao.selectListByUserIdAndReadTag(
                    user.getId(),
                    readTag);

    if (CollectionUtils.isEmpty(messageList)) {
      return new ArrayList<>();
    }

    List<MessageVO> resultList =
            new ArrayList<>(messageList.size());

    for (Message message : messageList) {
      MessageVO messageVO =
              convertToVO(message);

      if (messageVO != null) {
        resultList.add(messageVO);
      }
    }

    return resultList;
  }

  /**
   * 批量切换消息已读状态。
   *
   * @param messageIdList 消息 ID 列表
   */
  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void changeMessageStatus(
          List<Long> messageIdList) {

    List<Long> validMessageIds =
            normalizeIds(messageIdList);

    if (validMessageIds.isEmpty()) {
      return;
    }

    List<Message> messageList =
            messageDao.selectListByMessageIdList(
                    validMessageIds);

    if (CollectionUtils.isEmpty(messageList)) {
      return;
    }

    for (Message message : messageList) {
      if (message == null) {
        continue;
      }

      /*
       * 使用 Boolean.TRUE.equals 避免 readTag 为空时
       * 自动拆箱产生 NullPointerException。
       *
       * null 和 false 都会切换为 true。
       */
      message.setReadTag(
              !Boolean.TRUE.equals(
                      message.getReadTag()));

      messageDao.update(message);
    }
  }

  /**
   * 批量保存消息。
   *
   * @param messageDTOList 消息列表
   */
  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void saveMessages(
          List<MessageDTO> messageDTOList) {

    if (CollectionUtils.isEmpty(messageDTOList)) {
      return;
    }

    List<Message> messageList =
            messageDTOList.stream()
                    .filter(Objects::nonNull)
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());

    if (messageList.isEmpty()) {
      return;
    }

    messageDao.insertBatch(messageList);
  }

  /**
   * 将消息 DTO 转换为消息实体。
   *
   * @param messageDTO 消息信息
   * @return 消息实体
   */
  private Message convertToEntity(
          MessageDTO messageDTO) {

    Message message =
            CopyBeanUtil.copy(
                    messageDTO,
                    Message.class);

    if (message == null) {
      throw new IllegalStateException(
              "消息对象转换失败");
    }

    return message;
  }

  /**
   * 将消息实体转换为消息视图对象。
   *
   * @param message 消息实体
   * @return 消息视图对象
   */
  private MessageVO convertToVO(
          Message message) {

    if (message == null) {
      return null;
    }

    MessageVO messageVO =
            CopyBeanUtil.copy(
                    message,
                    MessageVO.class);

    if (messageVO == null) {
      throw new IllegalStateException(
              "消息视图对象转换失败");
    }

    if (message.getCreateTime() != null) {
      messageVO.setCreateTime(
              message.getCreateTime().getTime());
    }

    return messageVO;
  }

  /**
   * 过滤空 ID 并去重。
   *
   * @param idList ID 列表
   * @return 有效 ID 列表
   */
  private List<Long> normalizeIds(
          List<Long> idList) {

    if (CollectionUtils.isEmpty(idList)) {
      return new ArrayList<>();
    }

    return idList.stream()
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
  }
}
