package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/common"})
public class CommonController {
  @GetMapping(value = {"/heart"})
  public Result<String> health() {
    return Result.success("\u4e00\u4e2a\u666e\u901a\u7684\u8bf7\u6c42\u54cd" +
                          "\u5e94\u4e86\u666e\u901a\u7684\u7ed3\u679c");
  }
}
