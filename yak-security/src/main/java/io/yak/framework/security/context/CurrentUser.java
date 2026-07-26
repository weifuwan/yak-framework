package io.yak.framework.security.context;

import java.util.List;

/**
 * 当前请求中已验证的用户上下文。
 *
 * <p>业务 Service 可直接注入本接口，无需在方法间反复传递 operator。</p>
 */
public interface CurrentUser {

  Long getUserId();

  String getUsername();

  Long getProjectId();

  List<Long> getRoleIds();

  boolean isAuthenticated();
}
