package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.entity.Message;
import io.yak.framework.security.common.po.MessagePO;
import io.yak.framework.security.dao.MessageDao;
import io.yak.framework.security.dao.mapper.MessageMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户消息数据访问实现。
 */
@Repository
@RequiredArgsConstructor
public class MessageDaoImpl
        implements MessageDao {

    private final MessageMapper messageMapper;

    /**
     * 新增消息，并回填消息主键。
     *
     * @param message 消息信息
     */
    @Override
    public void insert(Message message) {
        MessagePO messagePO =
                CopyBeanUtil.copy(message, MessagePO.class);

        messageMapper.insert(messagePO);

        // MyBatis-Plus 插入成功后会自动回填自增主键。
        message.setId(messagePO.getId());
    }

    /**
     * 根据主键更新消息。
     *
     * @param message 消息信息
     */
    @Override
    public void update(Message message) {
        messageMapper.updateById(
                CopyBeanUtil.copy(message, MessagePO.class)
        );
    }

    /**
     * 批量新增消息。
     *
     * <p>当前采用循环插入，适合消息数量较少的场景。</p>
     *
     * @param messageList 消息列表
     */
    @Override
    public void insertBatch(List<Message> messageList) {
        if (messageList == null || messageList.isEmpty()) {
            return;
        }

        CopyBeanUtil.copyList(messageList, MessagePO.class)
                .forEach(messageMapper::insert);
    }

    /**
     * 根据接收用户和已读状态查询消息。
     *
     * @param userId  接收用户标识，可为空
     * @param readTag 已读状态，可为空
     * @return 消息列表
     */
    @Override
    public List<Message> selectListByUserIdAndReadTag(
            Long userId,
            Boolean readTag) {

        List<MessagePO> messagePOList =
                messageMapper.selectList(
                        Wrappers.<MessagePO>lambdaQuery()
                                .eq(
                                        userId != null,
                                        MessagePO::getUserId,
                                        userId
                                )
                                .eq(
                                        readTag != null,
                                        MessagePO::getReadTag,
                                        readTag
                                )
                                .orderByDesc(
                                        MessagePO::getCreateTime
                                )
                );

        return CopyBeanUtil.copyList(
                messagePOList,
                Message.class
        );
    }

    /**
     * 根据消息主键集合查询消息。
     *
     * @param messageIdList 消息主键集合
     * @return 消息列表
     */
    @Override
    public List<Message> selectListByMessageIdList(
            List<Long> messageIdList) {

        if (messageIdList == null || messageIdList.isEmpty()) {
            return List.of();
        }

        List<MessagePO> messagePOList =
                messageMapper.selectList(
                        Wrappers.<MessagePO>lambdaQuery()
                                .in(
                                        MessagePO::getId,
                                        messageIdList
                                )
                );

        return CopyBeanUtil.copyList(
                messagePOList,
                Message.class
        );
    }
}