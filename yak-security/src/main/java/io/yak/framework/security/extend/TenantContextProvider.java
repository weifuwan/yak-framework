package io.yak.framework.security.extend;

import io.yak.framework.security.context.TenantIdentity;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 当前租户解析扩展点。
 *
 * <p>宿主系统可覆盖默认实现，从 OIDC、LDAP、网关声明或企业内部 IAM 中解析租户。
 */
public interface TenantContextProvider {

  TenantIdentity resolve(
      HttpServletRequest request,
      Long userId);
}
