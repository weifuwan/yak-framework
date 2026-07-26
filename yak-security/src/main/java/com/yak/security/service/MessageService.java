/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.service;

import com.yak.security.common.dto.message.MessageDTO;
import com.yak.security.common.vo.message.MessageVO;

import java.util.List;

public interface MessageService {
    public void saveMessage(MessageDTO var1);

    public List<MessageVO> getMessageListByUserIdAndReadTag(String var1, Boolean var2);

    public void changeMessageStatus(List<Integer> var1);

    public void saveMessages(List<MessageDTO> var1);
}

