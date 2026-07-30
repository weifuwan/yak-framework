package io.yak.framework.security.extend.impl;

import io.yak.framework.security.common.po.TenantPO;
import io.yak.framework.security.common.po.UserTenantPO;
import io.yak.framework.security.context.TenantIdentity;
import io.yak.framework.security.dao.TenantRepository;
import io.yak.framework.security.dao.UserTenantRepository;
import io.yak.framework.security.extend.TenantContextProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;

/**
 * 基于用户租户成员关系的默认租户上下文解析器。
 *
 * <p>请求可通过 {@code X-Yak-Tenant-Id} 或 {@code X-Yak-Tenant-Code} 显式切换租户。
 * 未显式指定时优先使用默认租户，再使用第一条有效成员关系。
 */
public final class DefaultTenantContextProvider
    implements TenantContextProvider {

  public static final String TENANT_ID_HEADER =
      "X-Yak-Tenant-Id";
  public static final String TENANT_CODE_HEADER =
      "X-Yak-Tenant-Code";

  private final ObjectProvider<TenantRepository>
      tenantRepositoryProvider;
  private final ObjectProvider<UserTenantRepository>
      userTenantRepositoryProvider;

  public DefaultTenantContextProvider(
      ObjectProvider<TenantRepository> tenantRepositoryProvider,
      ObjectProvider<UserTenantRepository> userTenantRepositoryProvider) {
    this.tenantRepositoryProvider = tenantRepositoryProvider;
    this.userTenantRepositoryProvider = userTenantRepositoryProvider;
  }

  @Override
  public TenantIdentity resolve(
      HttpServletRequest request,
      Long userId) {
    if (request == null || userId == null) {
      return TenantIdentity.none();
    }

    TenantRepository tenantRepository =
        tenantRepositoryProvider.getIfAvailable();
    UserTenantRepository userTenantRepository =
        userTenantRepositoryProvider.getIfAvailable();
    if (tenantRepository == null
        || userTenantRepository == null) {
      return TenantIdentity.none();
    }

    String tenantIdValue = request.getHeader(TENANT_ID_HEADER);
    String tenantCode = request.getHeader(TENANT_CODE_HEADER);

    if (StringUtils.hasText(tenantIdValue)) {
      Long tenantId = parseLong(tenantIdValue);
      return resolveRequested(
          userId,
          tenantId,
          tenantRepository,
          userTenantRepository);
    }

    if (StringUtils.hasText(tenantCode)) {
      TenantPO tenant =
          tenantRepository.selectByCode(tenantCode.trim());
      return tenant == null
          ? TenantIdentity.none()
          : resolveRequested(
              userId,
              tenant.getId(),
              tenantRepository,
              userTenantRepository);
    }

    List<UserTenantPO> memberships =
        userTenantRepository.selectByUserId(userId);
    if (memberships.isEmpty()) {
      return TenantIdentity.none();
    }

    for (UserTenantPO membership : memberships) {
      TenantIdentity identity = resolveRequested(
          userId,
          membership.getTenantId(),
          tenantRepository,
          userTenantRepository);
      if (identity.isPresent()) {
        return identity;
      }
    }

    return TenantIdentity.none();
  }

  private TenantIdentity resolveRequested(
      Long userId,
      Long tenantId,
      TenantRepository tenantRepository,
      UserTenantRepository userTenantRepository) {
    if (tenantId == null) {
      return TenantIdentity.none();
    }

    UserTenantPO membership =
        userTenantRepository.selectMembership(userId, tenantId);
    if (membership == null
        || !Integer.valueOf(1).equals(membership.getStatus())) {
      return TenantIdentity.none();
    }

    TenantPO tenant = tenantRepository.selectById(tenantId);
    if (tenant == null
        || !Integer.valueOf(1).equals(tenant.getStatus())) {
      return TenantIdentity.none();
    }

    return new TenantIdentity(
        tenant.getId(),
        tenant.getTenantCode(),
        tenant.getTenantName());
  }

  private Long parseLong(String value) {
    try {
      return Long.valueOf(value.trim());
    } catch (NumberFormatException exception) {
      return null;
    }
  }
}
