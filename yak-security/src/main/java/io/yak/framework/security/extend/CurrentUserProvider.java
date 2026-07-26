package io.yak.framework.security.extend;

import javax.servlet.http.HttpServletRequest;

/**
 * 当前登录用户提供器。
 *
 * <p>用于解除业务模块与具体登录实现、HTTP 工具类之间的耦合。</p>
 *
 * @author weifuwan
 */
@FunctionalInterface
public interface CurrentUserProvider {

  /**
   * 获取当前登录用户名。
   *
   * @param request HTTP 请求
   * @return 当前登录用户名；未登录时返回 {@code null}
   */
  String getCurrentUser(HttpServletRequest request);
}