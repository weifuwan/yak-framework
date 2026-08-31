package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.entity.Message;
import io.yak.framework.security.common.po.MessagePO;
import io.yak.framework.security.dao.MessageDao;
import io.yak.framework.security.dao.mapper.MessageMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/** 用户消息数据访问实现。 */
@Repository
@RequiredArgsConstructor
public class MessageDaoImpl implements MessageDao {

  private final MessageMapper messageMapper;

  @Override
  public void insert(Message message) {
    MessagePO messagePO = CopyBeanUtil.copy(message, MessagePO.class);
    messageMapper.insert(messagePO);
    message.setId(messagePO.getId());
  }

  @Override
  public void update(Message message) {
    messageMapper.updateById(CopyBeanUtil.copy(message, MessagePO.class));
  }

  @Override
  public void insertBatch(List<Message> messageList) {
    if (messageList == null || messageList.isEmpty()) {
      return;
    }
    CopyBeanUtil.copyList(messageList, MessagePO.class)
            .forEach(messageMapper::insert);
  }

  @Override
  public List<Message> selectListByUserIdAndReadTag(
          Long userId,
          Boolean readTag) {

    List<MessagePO> messagePOList = messageMapper.selectList(
            Wrappers.<MessagePO>lambdaQuery()
                    .eq(userId != null, MessagePO::getUserId, userId)
                    .eq(readTag != null, MessagePO::getReadTag, readTag)
                    .orderByDesc(MessagePO::getCreateTime));

    return CopyBeanUtil.copyList(messagePOList, Message.class);
  }

  @Override
  public List<Message> selectListByMessageIdList(
          List<Long> messageIdList) {

    if (messageIdList == null || messageIdList.isEmpty()) {
      return Collections.emptyList();
    }

    List<MessagePO> messagePOList = messageMapper.selectList(
            Wrappers.<MessagePO>lambdaQuery()
                    .in(MessagePO::getId, messageIdList));

    return CopyBeanUtil.copyList(messagePOList, Message.class);
  }

  @Override
  public List<Message> selectListByMessageIdListAndUserId(
          List<Long> messageIdList,
          Long userId) {

    if (messageIdList == null || messageIdList.isEmpty() || userId == null) {
      return Collections.emptyList();
    }

    List<MessagePO> messagePOList = messageMapper.selectList(
            Wrappers.<MessagePO>lambdaQuery()
                    .eq(MessagePO::getUserId, userId)
                    .in(MessagePO::getId, messageIdList));

    return CopyBeanUtil.copyList(messagePOList, Message.class);
  }

  @Override
  public Message selectByMessageIdAndUserId(
          Long messageId,
          Long userId) {

    if (messageId == null || userId == null) {
      return null;
    }

    MessagePO messagePO = messageMapper.selectOne(
            Wrappers.<MessagePO>lambdaQuery()
                    .eq(MessagePO::getId, messageId)
                    .eq(MessagePO::getUserId, userId));

    return CopyBeanUtil.copy(messagePO, Message.class);
  }

  @Override
  public IPage<Message> selectPageByUserId(
          Long userId,
          Boolean readTag,
          String type,
          Long projectId,
          Date startTime,
          Date endTime,
          int pageNum,
          int pageSize) {

    Page<MessagePO> page = Page.of(pageNum, pageSize);
    LambdaQueryWrapper<MessagePO> wrapper = Wrappers.<MessagePO>lambdaQuery()
            .eq(MessagePO::getUserId, userId)
            .eq(readTag != null, MessagePO::getReadTag, readTag)
            .eq(StringUtils.hasText(type), MessagePO::getType, type)
            .ge(startTime != null, MessagePO::getCreateTime, startTime)
            .le(endTime != null, MessagePO::getCreateTime, endTime)
            .and(
                    projectId != null,
                    nested -> nested
                            .eq(MessagePO::getScope, "SYSTEM")
                            .or()
                            .eq(MessagePO::getProjectId, projectId))
            .orderByDesc(MessagePO::getCreateTime)
            .orderByDesc(MessagePO::getId);

    IPage<MessagePO> result = messageMapper.selectPage(page, wrapper);
    return CopyBeanUtil.copyPage(result, Message.class);
  }

  @Override
  public long countUnreadByUserId(Long userId) {
    if (userId == null) {
      return 0L;
    }
    return messageMapper.selectCount(
            Wrappers.<MessagePO>lambdaQuery()
                    .eq(MessagePO::getUserId, userId)
                    .eq(MessagePO::getReadTag, false));
  }
}
