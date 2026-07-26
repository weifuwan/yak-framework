package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/yak-security/api/v1/account"})
public class LoginController {
  @Autowired private LoginService loginService;

  @PostMapping(value = {"/login"})
  public Result<UserBriefVO> login(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody AccountLoginDTO loginDTO) {
    try {
      UserBriefVO userBriefVO =
          this.loginService.verifyLogin(loginDTO, request, response);
      return Result.success(userBriefVO);
    } catch (YakSecurityException e) {
      return Result.fail(e);
    }
  }

  @PostMapping(value = {"/logout"})
  public Result<Boolean> logout(HttpServletRequest request,
                                HttpServletResponse response) {
    return this.loginService.logout(request, response);
  }
}
