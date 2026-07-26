package io.yak.framework.security.extend.impl;

import io.yak.framework.security.extend.CurrentUserProvider;
import io.yak.framework.security.util.SecuritySessionAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 默认当前用户提供器。
 *
 * <p>只从服务端 Session 中读取当前用户名，不信任客户端传入的
 * Header 或 Cookie。</p>
 *
 * @author weifuwan
 */
public class DefaultCurrentUserProvider
        implements CurrentUserProvider {

  /**
   * 获取当前登录用户名。
   *
   * @param request HTTP 请求
   * @return 当前登录用户名；未登录时返回 {@code null}
   */
  @Override
  public String getCurrentUser(
          HttpServletRequest request) {

    Objects.requireNonNull(
            request,
            "request must not be null");

    HttpSession session =
            request.getSession(false);

    if (session == null) {
      return null;
    }

    Object operator =
            session.getAttribute(SecuritySessionAttributes.USER_NAME);

    if (!(operator instanceof String)) {
      return null;
    }

    String userName = (String) operator;

    return StringUtils.hasText(userName)
            ? userName
            : null;
  }
}
