package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.vo.message.MessageVO;
import io.yak.framework.security.service.MessageService;
import io.yak.framework.security.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/message"})
public class MessageController {
  @Autowired private MessageService messageService;

  @GetMapping(value = {"/list/{readTag}", "/list"})
  public Result<List<MessageVO>>
  list(@PathVariable(required = false) Boolean readTag,
       HttpServletRequest request) {
    List<MessageVO> messageVOList =
        this.messageService.getMessageListByUserIdAndReadTag(
            HttpRequestUtil.getOperator(request), readTag);
    return Result.success(messageVOList);
  }

  @PutMapping(value = {"/switch"})
  public Result<String> switched(@RequestBody @ApiParam(
      name = "idList", value = "\u6d88\u606fidList") List<Integer> idList) {
    this.messageService.changeMessageStatus(idList);
    return Result.success();
  }
}
