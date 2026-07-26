package io.yak.framework.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.yak.framework.security.common.entity.Message;
import io.yak.framework.security.common.po.MessagePO;
import io.yak.framework.security.dao.MessageDao;
import io.yak.framework.security.dao.mapper.MessageMapper;
import io.yak.framework.security.util.CopyBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class MessageDaoImpl
        extends BaseDaoImpl<MessagePO> implements MessageDao {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void insert(Message message) {
        MessagePO messagePO = CopyBeanUtil.copy(message, MessagePO.class);
        this.messageMapper.insert(messagePO);
        message.setId(messagePO.getId());
    }

    @Override
    public void update(Message message) {
        this.messageMapper.updateById(CopyBeanUtil.copy(message, MessagePO.class));
    }

    @Override
    public void insertBatch(List<Message> messageList) {
        if (CollectionUtils.isEmpty(messageList)) {
            return;
        }
        List<MessagePO> messagePOList =
                CopyBeanUtil.copyList(messageList, MessagePO.class);
        for (MessagePO messagePO : messagePOList) {
            this.messageMapper.insert(messagePO);
        }
    }

    @Override
    public List<Message> selectListByUserIdAndReadTag(Long userId,
                                                      Boolean readTag) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        ((QueryWrapper) queryWrapper.eq(userId != null, (Object) "user_id",
                (Object) userId))
                .eq(readTag != null, (Object) "read_tag", (Object) readTag);
        return CopyBeanUtil.copyList(
                this.messageMapper.selectList((Wrapper) queryWrapper), Message.class);
    }

    @Override
    public List<Message> selectListByMessageIdList(List<Long> messageIdList) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.in((Object) "id", messageIdList);
        return CopyBeanUtil.copyList(
                this.messageMapper.selectList((Wrapper) queryWrapper), Message.class);
    }
}
