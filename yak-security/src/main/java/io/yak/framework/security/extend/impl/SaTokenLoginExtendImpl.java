package io.yak.framework.security.extend.impl;

import io.yak.framework.security.authentication.AuthenticationManager;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 使用 Sa-Token 管理登录态的 Yak Security 登录扩展。
 *
 * <p>账号密码、账户状态、密码变更失效和请求白名单仍复用
 * {@link DefaultLoginExtendImpl}，本类只替换登录态的建立、读取和清理。</p>
 */
public class SaTokenLoginExtendImpl
        extends DefaultLoginExtendImpl {

  private final AuthenticationManager authenticationManager;

  public SaTokenLoginExtendImpl(
          UserService userService,
          PasswordEncoder passwordEncoder,
          YakSecurityProperties properties,
          AuthenticationManager authenticationManager) {

    super(userService, passwordEncoder, properties);
    this.authenticationManager =
            Objects.requireNonNull(
                    authenticationManager,
                    "authenticationManager must not be null");
  }

  @Override
  protected void initLoginContext(
          HttpServletRequest request,
          String userName,
          Long userId,
          String credentialVersion) {

    authenticationManager.login(
            userId,
            userName,
            credentialVersion);
  }

  @Override
  protected LoginIdentity resolveLoginIdentity(
          HttpServletRequest request) {

    if (!authenticationManager.isLogin()) {
      return null;
    }

    return new LoginIdentity(
            authenticationManager.getLoginUserId(),
            authenticationManager.getLoginUsername(),
            authenticationManager.getCredentialVersion());
  }

  @Override
  protected void clearLoginContext(
          HttpServletRequest request) {
    authenticationManager.logout();
  }
}
