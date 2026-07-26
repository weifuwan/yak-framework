/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 *  org.springframework.util.CollectionUtils
 */
package com.yak.security.dao.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yak.security.common.entity.Message;
import com.yak.security.common.po.MessagePO;
import com.yak.security.dao.MessageDao;
import com.yak.security.dao.mapper.MessageMapper;
import com.yak.security.util.CopyBeanUtil;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class MessageDaoImpl
extends BaseDaoImpl<MessagePO>
implements MessageDao {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void insert(Message message) {
        MessagePO messagePO = CopyBeanUtil.copy(message, MessagePO.class);
        messagePO.setAppName(this.logiSecurityProper.getAppName());
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
        List<MessagePO> messagePOList = CopyBeanUtil.copyList(messageList, MessagePO.class);
        for (MessagePO messagePO : messagePOList) {
            messagePO.setAppName(this.logiSecurityProper.getAppName());
            this.messageMapper.insert(messagePO);
        }
    }

    @Override
    public List<Message> selectListByUserIdAndReadTag(Integer userId, Boolean readTag) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        ((QueryWrapper)queryWrapper.eq(userId != null, (Object)"user_id", (Object)userId)).eq(readTag != null, (Object)"read_tag", (Object)readTag);
        return CopyBeanUtil.copyList(this.messageMapper.selectList((Wrapper)queryWrapper), Message.class);
    }

    @Override
    public List<Message> selectListByMessageIdList(List<Integer> messageIdList) {
        QueryWrapper queryWrapper = this.getQueryWrapperWithAppName();
        queryWrapper.in((Object)"id", messageIdList);
        return CopyBeanUtil.copyList(this.messageMapper.selectList((Wrapper)queryWrapper), Message.class);
    }
}

