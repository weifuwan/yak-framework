package io.yak.framework.security.extend.impl;
import io.yak.framework.security.extend.CurrentUserProvider;
import io.yak.framework.security.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
public class DefaultCurrentUserProvider implements CurrentUserProvider {
  public String getCurrentUser(HttpServletRequest request) { return HttpRequestUtil.getOperator(request); }
}
