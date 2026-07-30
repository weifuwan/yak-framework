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

  /**
   * 当前业务租户标识。
   *
   * <p>使用默认方法保持已有宿主实现兼容。
   */
  default Long getTenantId() {
    return null;
  }

  /** 当前业务租户编码。 */
  default String getTenantCode() {
    return null;
  }

  /** 当前业务租户名称。 */
  default String getTenantName() {
    return null;
  }
}
