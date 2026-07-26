package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.dao.MessageDao;
import io.yak.framework.security.dao.UserDao;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageServiceImplTest {

  @Test
  void queriesUserDirectlyWithoutDependingOnUserService() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    User user = new User();
    user.setId(1L);
    when(userDao.selectByUsername("admin"))
            .thenReturn(user);

    MessageServiceImpl messageService =
            new MessageServiceImpl(messageDao, userDao);

    messageService.getMessageListByUsernameAndReadTag(
            " admin ", false);

    verify(userDao).selectByUsername("admin");
    verify(messageDao)
            .selectListByUserIdAndReadTag(1L, false);
  }
}
