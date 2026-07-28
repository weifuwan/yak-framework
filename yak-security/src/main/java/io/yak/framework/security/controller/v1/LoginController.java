package io.yak.framework.security.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.yak.framework.security.common.constant.Constants;
import io.yak.framework.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.user.CurrentUserVO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.context.CurrentUser;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.service.LoginService;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.service.impl.UserMenuGrantService;
import io.yak.framework.security.web.PublicEndpoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.*;

/**
 * 登录账户管理接口。
 *
 * @author weifuwan
 */
@Tag(name = Constants.SWAGGER_API_TAG_PREFIX + "账户认证接口")
@RestController
@RequestMapping("/yak-security/api/v1/account")
public class LoginController {

  private final LoginService loginService;
  private final UserService userService;
  private final CurrentUser currentUser;
  private final ObjectProvider<UserMenuGrantService>
          userMenuGrantServiceProvider;

  public LoginController(
          LoginService loginService,
          UserService userService,
          CurrentUser currentUser,
          ObjectProvider<UserMenuGrantService>
                  userMenuGrantServiceProvider) {
    this.loginService = loginService;
    this.userService = userService;
    this.currentUser = currentUser;
    this.userMenuGrantServiceProvider =
            userMenuGrantServiceProvider;
  }

  /**
   * 用户登录。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @param loginDTO 登录信息
   * @return 当前登录用户
   */
  @Operation(summary = "用户登录")
  @PostMapping("/login")
  @PublicEndpoint
  public Result<UserBriefVO> login(
          HttpServletRequest request,
          HttpServletResponse response,
          @Valid @RequestBody AccountLoginDTO loginDTO) {

    UserBriefVO currentUser =
            loginService.verifyLogin(
                    loginDTO,
                    request,
                    response);

    return Result.success(currentUser);

  }

  @Operation(summary = "获取当前登录用户")
  @GetMapping("/current")
  public Result<CurrentUserVO> current() {

    if (!currentUser.isAuthenticated()) {
      throw new YakSecurityException(
              ResultCode.USER_NOT_LOGIN);
    }

    CurrentUserVO user =
            userService.getCurrentUserByUsername(
                    currentUser.getUsername());

    if (user == null) {
      throw new YakSecurityException(
              ResultCode.USER_NOT_EXISTS);
    }

    UserMenuGrantService userMenuGrantService =
            userMenuGrantServiceProvider.getIfAvailable();
    if (userMenuGrantService != null) {
      UserMenuGrantService.MenuGrant menuGrant =
              userMenuGrantService.resolve(user.getId());
      user.setMenuCodes(menuGrant.getMenuCodes());

      Set<String> effectivePermissionCodes =
              new LinkedHashSet<>();
      if (user.getPermissionCodes() != null) {
        effectivePermissionCodes.addAll(
                user.getPermissionCodes());
      }
      effectivePermissionCodes.addAll(
              menuGrant.getPermissionCodes());
      user.setPermissionCodes(
              new ArrayList<>(effectivePermissionCodes));
    }

    return Result.success(user);
  }

  /**
   * 用户退出登录。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 退出结果
   */
  @Operation(summary = "用户退出登录")
  @PostMapping("/logout")
  public Result<Boolean> logout(
          HttpServletRequest request,
          HttpServletResponse response) {

    return loginService.logout(
            request,
            response);
  }
}
