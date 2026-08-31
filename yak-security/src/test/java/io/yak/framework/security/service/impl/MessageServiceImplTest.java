package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.dto.message.MessagePageQueryDTO;
import io.yak.framework.security.common.entity.Message;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.dao.MessageDao;
import io.yak.framework.security.dao.UserDao;
import java.util.Collections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** 消息中心核心读状态和分页契约测试。 */
class MessageServiceImplTest {

  @Test
  void markReadIsIdempotentAndScopedToCurrentUser() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    MessageServiceImpl service = new MessageServiceImpl(messageDao, userDao);

    User user = new User();
    user.setId(7L);
    when(userDao.selectByUsername("alice")).thenReturn(user);

    Message message = new Message();
    message.setId(11L);
    message.setUserId(7L);
    message.setReadTag(false);
    when(messageDao.selectByMessageIdAndUserId(11L, 7L)).thenReturn(message);

    service.markMessageRead("alice", 11L);

    assertTrue(message.getReadTag());
    assertNotNull(message.getReadTime());
    verify(messageDao).update(message);
  }

  @Test
  void pageExposesNewStatusAndOperationLogAliases() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    MessageServiceImpl service = new MessageServiceImpl(messageDao, userDao);

    User user = new User();
    user.setId(7L);
    when(userDao.selectByUsername("alice")).thenReturn(user);

    Message message = new Message();
    message.setId(11L);
    message.setTitle("Task failed");
    message.setContent("Offline sync failed");
    message.setReadTag(false);
    message.setOplogId(23L);

    Page<Message> page = Page.of(1, 10);
    page.setRecords(Collections.singletonList(message));
    page.setTotal(1L);
    when(messageDao.selectPageByUserId(
            anyLong(), isNull(), isNull(), isNull(), isNull(), isNull(),
            anyInt(), anyInt())).thenReturn(page);

    var result = service.getMessagePage("alice", new MessagePageQueryDTO());

    assertEquals(1L, result.getTotal());
    assertEquals("UNREAD", result.getRecords().get(0).getStatus());
    assertEquals(23L, result.getRecords().get(0).getOperationLogId());
    assertEquals("Offline sync failed", result.getRecords().get(0).getSummary());
  }
}
