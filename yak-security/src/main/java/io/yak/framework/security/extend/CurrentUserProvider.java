package io.yak.framework.security.extend;

import jakarta.servlet.http.HttpServletRequest;

/** Resolves the current user without coupling applications to Yak's HTTP utility. */
public interface CurrentUserProvider {
  String getCurrentUser(HttpServletRequest request);
}
