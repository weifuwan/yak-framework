/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.CollectionUtils
 */
package com.yak.security.service.impl;

import com.yak.security.common.dto.message.MessageDTO;
import com.yak.security.common.entity.Message;
import com.yak.security.common.entity.user.User;
import com.yak.security.common.vo.message.MessageVO;
import com.yak.security.dao.MessageDao;
import com.yak.security.service.MessageService;
import com.yak.security.service.UserService;
import com.yak.security.util.CopyBeanUtil;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service(value="logiSecurityMessageServiceImpl")
public class MessageServiceImpl
implements MessageService {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserService userService;

    @Override
    public void saveMessage(MessageDTO messageDTO) {
        Message message = CopyBeanUtil.copy(messageDTO, Message.class);
        this.messageDao.insert(message);
    }

    @Override
    public List<MessageVO> getMessageListByUserIdAndReadTag(String userName, Boolean readTag) {
        User user = this.userService.getUserByUserName(userName);
        List<Message> messageList = this.messageDao.selectListByUserIdAndReadTag(user.getId(), readTag);
        ArrayList<MessageVO> result = new ArrayList<MessageVO>();
        for (Message message : messageList) {
            MessageVO messageVO = CopyBeanUtil.copy(message, MessageVO.class);
            messageVO.setCreateTime(message.getCreateTime().getTime());
            result.add(messageVO);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void changeMessageStatus(List<Integer> messageIdList) {
        if (CollectionUtils.isEmpty(messageIdList)) {
            return;
        }
        List<Message> messageList = this.messageDao.selectListByMessageIdList(messageIdList);
        for (Message message : messageList) {
            message.setReadTag(message.getReadTag() == false);
            this.messageDao.update(message);
        }
    }

    @Override
    public void saveMessages(List<MessageDTO> messageDTOList) {
        if (CollectionUtils.isEmpty(messageDTOList)) {
            return;
        }
        List<Message> messageList = CopyBeanUtil.copyList(messageDTOList, Message.class);
        this.messageDao.insertBatch(messageList);
    }
}

