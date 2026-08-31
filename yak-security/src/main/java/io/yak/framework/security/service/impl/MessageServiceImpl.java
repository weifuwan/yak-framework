package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.dto.message.MessageDTO;
import io.yak.framework.security.common.dto.message.MessagePageQueryDTO;
import io.yak.framework.security.common.entity.Message;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.message.MessagePageVO;
import io.yak.framework.security.common.vo.message.MessageVO;
import io.yak.framework.security.dao.MessageDao;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.MessageService;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** 消息服务实现类。 */
@Service("yakSecurityMessageServiceImpl")
public class MessageServiceImpl implements MessageService {

  private static final String STATUS_READ = "READ";
  private static final String STATUS_UNREAD = "UNREAD";
  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int MAX_PAGE_SIZE = 100;

  private final MessageDao messageDao;
  private final UserDao userDao;

  public MessageServiceImpl(
          MessageDao messageDao,
          UserDao userDao) {
    this.messageDao = messageDao;
    this.userDao = userDao;
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void saveMessage(MessageDTO messageDTO) {
    if (messageDTO == null) {
      return;
    }
    Message message = convertToEntity(messageDTO);
    applyDefaults(message);
    messageDao.insert(message);
  }

  @Override
  public List<MessageVO> getMessageListByUsernameAndReadTag(
          String username,
          Boolean readTag) {

    User user = requireUser(username);
    List<Message> messages = messageDao.selectListByUserIdAndReadTag(
            user.getId(), readTag);
    return convertToVOList(messages);
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void changeMessageStatus(List<Long> messageIdList) {
    toggleMessages(messageDao.selectListByMessageIdList(
            normalizeIds(messageIdList)));
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void changeMessageStatus(
          String username,
          List<Long> messageIdList) {

    User user = requireUser(username);
    toggleMessages(messageDao.selectListByMessageIdListAndUserId(
            normalizeIds(messageIdList), user.getId()));
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void saveMessages(List<MessageDTO> messageDTOList) {
    if (CollectionUtils.isEmpty(messageDTOList)) {
      return;
    }

    List<Message> messages = messageDTOList.stream()
            .filter(Objects::nonNull)
            .map(this::convertToEntity)
            .peek(this::applyDefaults)
            .collect(Collectors.toList());

    if (!messages.isEmpty()) {
      messageDao.insertBatch(messages);
    }
  }

  @Override
  public MessagePageVO getMessagePage(
          String username,
          MessagePageQueryDTO queryDTO) {

    User user = requireUser(username);
    MessagePageQueryDTO query = queryDTO == null
            ? new MessagePageQueryDTO()
            : queryDTO;

    int pageNum = query.getPageNum() == null
            ? 1
            : Math.max(1, query.getPageNum());
    int pageSize = query.getPageSize() == null
            ? DEFAULT_PAGE_SIZE
            : Math.max(1, Math.min(MAX_PAGE_SIZE, query.getPageSize()));

    IPage<Message> page = messageDao.selectPageByUserId(
            user.getId(),
            parseReadTag(query.getStatus()),
            normalizeText(query.getType()),
            query.getProjectId(),
            query.getStartTime(),
            query.getEndTime(),
            pageNum,
            pageSize);

    return new MessagePageVO(
            convertToVOList(page.getRecords()),
            page.getTotal());
  }

  @Override
  public MessageVO getMessageDetail(
          String username,
          Long messageId) {

    User user = requireUser(username);
    return convertToVO(messageDao.selectByMessageIdAndUserId(
            messageId, user.getId()));
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void markMessageRead(
          String username,
          Long messageId) {

    if (messageId == null) {
      return;
    }
    User user = requireUser(username);
    Message message = messageDao.selectByMessageIdAndUserId(
            messageId, user.getId());
    markRead(message);
  }

  @Override
  @Transactional(
          transactionManager = "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void markMessagesRead(
          String username,
          List<Long> messageIds) {

    List<Long> ids = normalizeIds(messageIds);
    if (ids.isEmpty()) {
      return;
    }
    User user = requireUser(username);
    messageDao.selectListByMessageIdListAndUserId(ids, user.getId())
            .forEach(this::markRead);
  }

  @Override
  public int getUnreadMessageCount(String username) {
    User user = requireUser(username);
    return Math.toIntExact(messageDao.countUnreadByUserId(user.getId()));
  }

  private void toggleMessages(List<Message> messages) {
    if (CollectionUtils.isEmpty(messages)) {
      return;
    }
    for (Message message : messages) {
      if (message == null) {
        continue;
      }
      boolean nextRead = !Boolean.TRUE.equals(message.getReadTag());
      message.setReadTag(nextRead);
      message.setReadTime(nextRead ? new Date() : null);
      messageDao.update(message);
    }
  }

  private void markRead(Message message) {
    if (message == null || Boolean.TRUE.equals(message.getReadTag())) {
      return;
    }
    message.setReadTag(true);
    message.setReadTime(new Date());
    messageDao.update(message);
  }

  private User requireUser(String username) {
    if (!StringUtils.hasText(username)) {
      throw new YakSecurityException(ResultCode.USER_NOT_EXISTS);
    }
    User user = userDao.selectByUsername(username.trim());
    if (user == null) {
      throw new YakSecurityException(ResultCode.USER_NOT_EXISTS);
    }
    return user;
  }

  private Boolean parseReadTag(String status) {
    if (!StringUtils.hasText(status)) {
      return null;
    }
    if (STATUS_READ.equalsIgnoreCase(status)) {
      return true;
    }
    if (STATUS_UNREAD.equalsIgnoreCase(status)) {
      return false;
    }
    throw new IllegalArgumentException("消息状态仅支持 READ 或 UNREAD");
  }

  private String normalizeText(String value) {
    return StringUtils.hasText(value) ? value.trim() : null;
  }

  private Message convertToEntity(MessageDTO messageDTO) {
    Message message = CopyBeanUtil.copy(messageDTO, Message.class);
    if (message == null) {
      throw new IllegalStateException("消息对象转换失败");
    }
    return message;
  }

  private void applyDefaults(Message message) {
    if (!StringUtils.hasText(message.getType())) {
      message.setType(message.getOplogId() == null ? "SYSTEM" : "SECURITY");
    }
    if (!StringUtils.hasText(message.getLevel())) {
      message.setLevel("INFO");
    }
    if (!StringUtils.hasText(message.getScope())) {
      message.setScope(message.getProjectId() == null ? "SYSTEM" : "PROJECT");
    }
    if (message.getReadTag() == null) {
      message.setReadTag(false);
    }
    if (!StringUtils.hasText(message.getSummary())
            && StringUtils.hasText(message.getContent())) {
      message.setSummary(compactSummary(message.getContent()));
    }
  }

  private String compactSummary(String content) {
    String normalized = content.replaceAll("\\s+", " ").trim();
    return normalized.length() <= 160
            ? normalized
            : normalized.substring(0, 157) + "...";
  }

  private List<MessageVO> convertToVOList(List<Message> messages) {
    if (CollectionUtils.isEmpty(messages)) {
      return new ArrayList<>();
    }
    return messages.stream()
            .filter(Objects::nonNull)
            .map(this::convertToVO)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
  }

  private MessageVO convertToVO(Message message) {
    if (message == null) {
      return null;
    }
    MessageVO messageVO = CopyBeanUtil.copy(message, MessageVO.class);
    if (messageVO == null) {
      throw new IllegalStateException("消息视图对象转换失败");
    }
    messageVO.setStatus(Boolean.TRUE.equals(message.getReadTag())
            ? STATUS_READ
            : STATUS_UNREAD);
    messageVO.setOperationLogId(message.getOplogId());
    if (message.getCreateTime() != null) {
      messageVO.setCreateTime(message.getCreateTime().getTime());
    }
    if (message.getReadTime() != null) {
      messageVO.setReadTime(message.getReadTime().getTime());
    }
    if (!StringUtils.hasText(messageVO.getSummary())
            && StringUtils.hasText(message.getContent())) {
      messageVO.setSummary(compactSummary(message.getContent()));
    }
    return messageVO;
  }

  private List<Long> normalizeIds(List<Long> idList) {
    if (CollectionUtils.isEmpty(idList)) {
      return new ArrayList<>();
    }
    return idList.stream()
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
  }
}
