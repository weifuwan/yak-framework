package io.yak.framework.security.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.yak.framework.security.common.po.MessagePO;
import io.yak.framework.security.dao.mapper.MessageMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/** 锁定消息中心以 project_id 作为 Project 安全边界。 */
class MessageDaoProjectVisibilityTest {

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  void restrictedPageUsesSystemNullOrAuthorizedProjectIds() {
    MessageMapper mapper = mock(MessageMapper.class);
    when(mapper.selectPage(any(Page.class), any()))
            .thenAnswer(invocation -> invocation.getArgument(0));
    MessageDaoImpl dao = new MessageDaoImpl(mapper);

    dao.selectPageByUserId(
            7L,
            null,
            null,
            List.of(9L),
            true,
            null,
            null,
            1,
            10);

    ArgumentCaptor<LambdaQueryWrapper<MessagePO>> wrapper =
            ArgumentCaptor.forClass(LambdaQueryWrapper.class);
    verify(mapper).selectPage(any(Page.class), wrapper.capture());

    String sql = wrapper.getValue().getSqlSegment().toLowerCase();
    assertThat(sql).contains("user_id");
    assertThat(sql).contains("project_id");
    assertThat(sql).contains("is null");
    assertThat(sql).contains("in");
    assertThat(sql).doesNotContain("message_scope");
    assertThat(wrapper.getValue().getParamNameValuePairs())
            .containsValue(7L)
            .containsValue(9L);
  }

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  void userWithoutProjectsOnlySeesSystemMessages() {
    MessageMapper mapper = mock(MessageMapper.class);
    when(mapper.selectPage(any(Page.class), any()))
            .thenAnswer(invocation -> invocation.getArgument(0));
    MessageDaoImpl dao = new MessageDaoImpl(mapper);

    dao.selectPageByUserId(
            7L,
            null,
            null,
            List.of(),
            true,
            null,
            null,
            1,
            10);

    ArgumentCaptor<LambdaQueryWrapper<MessagePO>> wrapper =
            ArgumentCaptor.forClass(LambdaQueryWrapper.class);
    verify(mapper).selectPage(any(Page.class), wrapper.capture());

    String sql = wrapper.getValue().getSqlSegment().toLowerCase();
    assertThat(sql).contains("project_id");
    assertThat(sql).contains("is null");
    assertThat(sql).doesNotContain(" in ");
  }
}
