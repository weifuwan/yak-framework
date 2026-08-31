package io.yak.framework.security.controller.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.yak.framework.security.common.dto.message.MessagePageQueryDTO;
import io.yak.framework.security.common.vo.message.MessagePageVO;
import io.yak.framework.security.service.MessageService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** 通过真实 Spring MVC 参数绑定锁定消息中心 HTTP 查询契约。 */
class MessageControllerWebContractTest {

  @Test
  void pageBindsEpochMillisTimeFilters() throws Exception {
    MessageService service = mock(MessageService.class);
    when(service.getMessagePage(isNull(), any(MessagePageQueryDTO.class)))
            .thenReturn(new MessagePageVO(List.of(), 0L));
    MockMvc mvc = MockMvcBuilders
            .standaloneSetup(new MessageController(service))
            .build();

    mvc.perform(get("/yak-security/api/v1/message/page")
                    .param("pageNum", "2")
                    .param("pageSize", "20")
                    .param("status", "UNREAD")
                    .param("type", "TASK")
                    .param("projectId", "9")
                    .param("startTime", "1788163200000")
                    .param("endTime", "1788249599999"))
            .andExpect(status().isOk());

    ArgumentCaptor<MessagePageQueryDTO> query =
            ArgumentCaptor.forClass(MessagePageQueryDTO.class);
    verify(service).getMessagePage(isNull(), query.capture());

    assertEquals(2, query.getValue().getPageNum());
    assertEquals(20, query.getValue().getPageSize());
    assertEquals("UNREAD", query.getValue().getStatus());
    assertEquals("TASK", query.getValue().getType());
    assertEquals(9L, query.getValue().getProjectId());
    assertEquals(1788163200000L, query.getValue().getStartTime());
    assertEquals(1788249599999L, query.getValue().getEndTime());
  }
}
