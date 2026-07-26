package io.yak.framework.security.controller.v1;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.LoginService;
import io.yak.framework.security.web.PublicEndpoint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录账户管理接口。
 *
 * @author weifuwan
 */
@RestController
@RequestMapping("/yak-security/api/v1/account")
public class LoginController {

  private final LoginService loginService;

  /**
   * 创建登录账户管理接口。
   *
   * @param loginService 登录服务
   */
  public LoginController(
          LoginService loginService) {

    this.loginService = loginService;
  }

  /**
   * 用户登录。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @param loginDTO 登录信息
   * @return 当前登录用户
   */
  @PostMapping("/login")
  @PublicEndpoint
  public Result<UserBriefVO> login(
          HttpServletRequest request,
          HttpServletResponse response,
          @RequestBody AccountLoginDTO loginDTO) {

    try {
      UserBriefVO currentUser =
              loginService.verifyLogin(
                      loginDTO,
                      request,
                      response);

      return Result.buildSucc(currentUser);
    } catch (YakSecurityException exception) {
      return Result.fail(exception);
    }
  }

  /**
   * 用户退出登录。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 退出结果
   */
  @PostMapping("/logout")
  public Result<Boolean> logout(
          HttpServletRequest request,
          HttpServletResponse response) {

    return loginService.logout(
            request,
            response);
  }
}
