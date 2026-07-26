package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.vo.message.MessageVO;
import io.yak.framework.security.service.MessageService;
import io.yak.framework.security.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息管理接口。
 *
 * @author weifuwan
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "消息管理接口")
@RestController
@RequestMapping("/yak-security/api/v1/message")
public class MessageController {

  private final MessageService messageService;

  /**
   * 创建消息管理接口。
   *
   * @param messageService 消息服务
   */
  public MessageController(
          MessageService messageService) {

    this.messageService = messageService;
  }

  /**
   * 查询当前用户消息。
   *
   * <p>readTag 为空时查询全部消息。
   *
   * @param readTag 已读状态
   * @param request HTTP 请求
   * @return 消息列表
   */
  @Operation(summary = "查询当前用户消息")
  @GetMapping({"/list", "/list/{readTag}"})
  public Result<List<MessageVO>> list(
          @PathVariable(required = false)
                  Boolean readTag,
          HttpServletRequest request) {

    String username =
            HttpRequestUtil.getOperator(request);

    return Result.success(
            messageService
                    .getMessageListByUsernameAndReadTag(
                            username,
                            readTag));

  }

  /**
   * 批量切换消息已读状态。
   *
   * @param messageIdList 消息 ID 列表
   * @return 操作结果
   */
  @Operation(summary = "批量切换消息已读状态")
  @PutMapping("/switch")
  public Result<Void> switchStatus(
          @RequestBody List<Long> messageIdList) {

    messageService.changeMessageStatus(
            messageIdList);

    return Result.success(null);
  }
}
