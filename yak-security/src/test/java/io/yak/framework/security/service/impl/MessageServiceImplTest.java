package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.constant.SecurityPermissionCode;
import io.yak.framework.security.common.dto.message.MessageDTO;
import io.yak.framework.security.common.dto.message.MessagePageQueryDTO;
import io.yak.framework.security.common.entity.Message;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.context.AuthorizationSnapshot;
import io.yak.framework.security.dao.MessageDao;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.exception.YakSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** 消息中心核心读状态、Project 授权和分页契约测试。 */
class MessageServiceImplTest {

  @Test
  void markReadIsIdempotentAndScopedToCurrentUser() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    AuthorizationSnapshotService authorizationService =
            mock(AuthorizationSnapshotService.class);
    MessageServiceImpl service = new MessageServiceImpl(
            messageDao, userDao, authorizationService);

    User user = user(7L);
    when(userDao.selectByUsername("alice")).thenReturn(user);
    when(authorizationService.get(7L)).thenReturn(AuthorizationSnapshot.empty());

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
  void revokedProjectMessageCannotBeReadOrMutated() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    AuthorizationSnapshotService authorizationService =
            mock(AuthorizationSnapshotService.class);
    MessageServiceImpl service = new MessageServiceImpl(
            messageDao, userDao, authorizationService);

    when(userDao.selectByUsername("alice")).thenReturn(user(7L));
    when(authorizationService.get(7L)).thenReturn(snapshot(Set.of(9L)));

    Message foreign = new Message();
    foreign.setId(11L);
    foreign.setUserId(7L);
    foreign.setProjectId(10L);
    foreign.setReadTag(false);
    when(messageDao.selectByMessageIdAndUserId(11L, 7L)).thenReturn(foreign);

    assertNull(service.getMessageDetail("alice", 11L));
    service.markMessageRead("alice", 11L);

    verify(messageDao, never()).update(foreign);
  }

  @Test
  void pageExposesAliasesAndRestrictsFullInboxToAuthorizedProjects() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    AuthorizationSnapshotService authorizationService =
            mock(AuthorizationSnapshotService.class);
    MessageServiceImpl service = new MessageServiceImpl(
            messageDao, userDao, authorizationService);

    when(userDao.selectByUsername("alice")).thenReturn(user(7L));
    when(authorizationService.get(7L)).thenReturn(snapshot(Set.of(9L)));

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
            eq(7L), isNull(), isNull(), eq(List.of(9L)), eq(true),
            isNull(), isNull(), eq(1), eq(10))).thenReturn(page);

    var result = service.getMessagePage("alice", new MessagePageQueryDTO());

    assertEquals(1L, result.getTotal());
    assertEquals("UNREAD", result.getRecords().get(0).getStatus());
    assertEquals(23L, result.getRecords().get(0).getOperationLogId());
    assertEquals("Offline sync failed", result.getRecords().get(0).getSummary());
  }

  @Test
  void requestedProjectMustBeAuthorized() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    AuthorizationSnapshotService authorizationService =
            mock(AuthorizationSnapshotService.class);
    MessageServiceImpl service = new MessageServiceImpl(
            messageDao, userDao, authorizationService);

    when(userDao.selectByUsername("alice")).thenReturn(user(7L));
    when(authorizationService.get(7L)).thenReturn(snapshot(Set.of(9L)));
    MessagePageQueryDTO query = new MessagePageQueryDTO();
    query.setProjectId(10L);

    assertThrows(
            YakSecurityException.class,
            () -> service.getMessagePage("alice", query));
    verify(messageDao, never()).selectPageByUserId(
            eq(7L), isNull(), isNull(), eq(List.of(10L)), eq(true),
            isNull(), isNull(), eq(1), eq(10));
  }

  @Test
  void rootInboxDoesNotRestrictProjectIds() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    AuthorizationSnapshotService authorizationService =
            mock(AuthorizationSnapshotService.class);
    MessageServiceImpl service = new MessageServiceImpl(
            messageDao, userDao, authorizationService);

    when(userDao.selectByUsername("root")).thenReturn(user(1L));
    when(authorizationService.get(1L)).thenReturn(
            new AuthorizationSnapshot(
                    List.of(),
                    Set.of(SecurityPermissionCode.ROOT),
                    List.of(),
                    Set.of()));
    Page<Message> page = Page.of(1, 10);
    page.setRecords(List.of());
    when(messageDao.selectPageByUserId(
            eq(1L), isNull(), isNull(), eq(List.of()), eq(false),
            isNull(), isNull(), eq(1), eq(10))).thenReturn(page);

    service.getMessagePage("root", new MessagePageQueryDTO());

    verify(messageDao).selectPageByUserId(
            eq(1L), isNull(), isNull(), eq(List.of()), eq(false),
            isNull(), isNull(), eq(1), eq(10));
  }

  @Test
  void epochMillisAreConvertedToDaoDates() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    AuthorizationSnapshotService authorizationService =
            mock(AuthorizationSnapshotService.class);
    MessageServiceImpl service = new MessageServiceImpl(
            messageDao, userDao, authorizationService);

    when(userDao.selectByUsername("alice")).thenReturn(user(7L));
    when(authorizationService.get(7L)).thenReturn(snapshot(Set.of(9L)));
    Page<Message> page = Page.of(1, 10);
    page.setRecords(List.of());
    when(messageDao.selectPageByUserId(
            eq(7L), isNull(), isNull(), eq(List.of(9L)), eq(true),
            org.mockito.ArgumentMatchers.any(Date.class),
            org.mockito.ArgumentMatchers.any(Date.class),
            eq(1), eq(10))).thenReturn(page);

    MessagePageQueryDTO query = new MessagePageQueryDTO();
    query.setStartTime(1788163200000L);
    query.setEndTime(1788249599999L);
    service.getMessagePage("alice", query);

    ArgumentCaptor<Date> start = ArgumentCaptor.forClass(Date.class);
    ArgumentCaptor<Date> end = ArgumentCaptor.forClass(Date.class);
    verify(messageDao).selectPageByUserId(
            eq(7L), isNull(), isNull(), eq(List.of(9L)), eq(true),
            start.capture(), end.capture(), eq(1), eq(10));
    assertEquals(1788163200000L, start.getValue().getTime());
    assertEquals(1788249599999L, end.getValue().getTime());
  }

  @Test
  void publishingDerivesScopeFromProjectId() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    AuthorizationSnapshotService authorizationService =
            mock(AuthorizationSnapshotService.class);
    MessageServiceImpl service = new MessageServiceImpl(
            messageDao, userDao, authorizationService);

    MessageDTO notification = new MessageDTO();
    notification.setUserId(7L);
    notification.setProjectId(9L);
    notification.setScope("SYSTEM");
    notification.setType("task");
    notification.setLevel("warning");
    notification.setTitle("Task failed");

    service.saveMessage(notification);

    ArgumentCaptor<Message> saved = ArgumentCaptor.forClass(Message.class);
    verify(messageDao).insert(saved.capture());
    assertEquals("PROJECT", saved.getValue().getScope());
    assertEquals("TASK", saved.getValue().getType());
    assertEquals("WARNING", saved.getValue().getLevel());
  }

  @Test
  void invalidEpochRangeIsRejectedBeforeDaoQuery() {
    MessageDao messageDao = mock(MessageDao.class);
    UserDao userDao = mock(UserDao.class);
    AuthorizationSnapshotService authorizationService =
            mock(AuthorizationSnapshotService.class);
    MessageServiceImpl service = new MessageServiceImpl(
            messageDao, userDao, authorizationService);

    when(userDao.selectByUsername("alice")).thenReturn(user(7L));
    when(authorizationService.get(7L)).thenReturn(snapshot(Set.of(9L)));
    MessagePageQueryDTO query = new MessagePageQueryDTO();
    query.setStartTime(200L);
    query.setEndTime(100L);

    assertThrows(
            YakSecurityException.class,
            () -> service.getMessagePage("alice", query));
  }

  private static User user(Long id) {
    User user = new User();
    user.setId(id);
    return user;
  }

  private static AuthorizationSnapshot snapshot(Set<Long> projectIds) {
    return new AuthorizationSnapshot(
            List.of(),
            Set.of(),
            List.of(),
            projectIds);
  }
}
