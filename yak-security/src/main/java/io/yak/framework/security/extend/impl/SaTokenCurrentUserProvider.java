package io.yak.framework.security.extend.impl;

import io.yak.framework.security.authentication.AuthenticationManager;
import io.yak.framework.security.extend.CurrentUserProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 从服务端 Sa-Token 登录态中读取当前用户名。
 */
public class SaTokenCurrentUserProvider
        implements CurrentUserProvider {

  private final AuthenticationManager authenticationManager;

  public SaTokenCurrentUserProvider(
          AuthenticationManager authenticationManager) {
    this.authenticationManager =
            Objects.requireNonNull(
                    authenticationManager,
                    "authenticationManager must not be null");
  }

  @Override
  public String getCurrentUser(
          HttpServletRequest request) {

    Objects.requireNonNull(
            request,
            "request must not be null");

    if (!authenticationManager.isLogin()) {
      return null;
    }

    return authenticationManager.getLoginUsername();
  }
}
