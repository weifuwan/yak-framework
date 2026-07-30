package io.yak.framework.security.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** 当前请求解析出的租户身份。 */
@Getter
@ToString
@EqualsAndHashCode
public final class TenantIdentity {

  private static final TenantIdentity NONE =
      new TenantIdentity(null, null, null);

  private final Long tenantId;
  private final String tenantCode;
  private final String tenantName;

  public TenantIdentity(
      Long tenantId,
      String tenantCode,
      String tenantName) {
    this.tenantId = tenantId;
    this.tenantCode = tenantCode;
    this.tenantName = tenantName;
  }

  public static TenantIdentity none() {
    return NONE;
  }

  public boolean isPresent() {
    return tenantId != null;
  }
}
