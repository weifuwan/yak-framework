package io.yak.framework.security.extend.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.yak.framework.security.common.po.TenantPO;
import io.yak.framework.security.common.po.UserTenantPO;
import io.yak.framework.security.context.TenantIdentity;
import io.yak.framework.security.dao.TenantRepository;
import io.yak.framework.security.dao.UserTenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

class DefaultTenantContextProviderTest {

  @Test
  void resolvesOnlyAnActiveTenantMembership() {
    TenantRepository tenantRepository =
        mock(TenantRepository.class);
    UserTenantRepository userTenantRepository =
        mock(UserTenantRepository.class);
    HttpServletRequest request =
        mock(HttpServletRequest.class);

    TenantPO tenant = tenant(10L, "tenant-a");
    UserTenantPO membership = membership(1L, 10L);
    when(request.getHeader(
        DefaultTenantContextProvider.TENANT_ID_HEADER))
        .thenReturn("10");
    when(userTenantRepository.selectMembership(1L, 10L))
        .thenReturn(membership);
    when(tenantRepository.selectById(10L))
        .thenReturn(tenant);

    TenantIdentity identity = provider(
        tenantRepository,
        userTenantRepository)
        .resolve(request, 1L);

    assertThat(identity.getTenantId()).isEqualTo(10L);
    assertThat(identity.getTenantCode()).isEqualTo("tenant-a");

    when(userTenantRepository.selectMembership(2L, 10L))
        .thenReturn(null);
    assertThat(provider(
        tenantRepository,
        userTenantRepository)
        .resolve(request, 2L)
        .isPresent())
        .isFalse();
  }

  @Test
  void fallsBackToTheUsersDefaultMembership() {
    TenantRepository tenantRepository =
        mock(TenantRepository.class);
    UserTenantRepository userTenantRepository =
        mock(UserTenantRepository.class);
    HttpServletRequest request =
        mock(HttpServletRequest.class);

    TenantPO tenant = tenant(20L, "tenant-default");
    UserTenantPO membership = membership(1L, 20L);
    membership.setDefaultTenant(Boolean.TRUE);
    when(userTenantRepository.selectByUserId(1L))
        .thenReturn(Collections.singletonList(membership));
    when(userTenantRepository.selectMembership(1L, 20L))
        .thenReturn(membership);
    when(tenantRepository.selectById(20L))
        .thenReturn(tenant);

    TenantIdentity identity = provider(
        tenantRepository,
        userTenantRepository)
        .resolve(request, 1L);

    assertThat(identity.getTenantId()).isEqualTo(20L);
    assertThat(identity.getTenantName()).isEqualTo("tenant-default");
  }

  @SuppressWarnings("unchecked")
  private DefaultTenantContextProvider provider(
      TenantRepository tenantRepository,
      UserTenantRepository userTenantRepository) {
    ObjectProvider<TenantRepository> tenantProvider =
        mock(ObjectProvider.class);
    ObjectProvider<UserTenantRepository> membershipProvider =
        mock(ObjectProvider.class);
    when(tenantProvider.getIfAvailable())
        .thenReturn(tenantRepository);
    when(membershipProvider.getIfAvailable())
        .thenReturn(userTenantRepository);
    return new DefaultTenantContextProvider(
        tenantProvider,
        membershipProvider);
  }

  private TenantPO tenant(Long id, String code) {
    TenantPO tenant = new TenantPO();
    tenant.setId(id);
    tenant.setTenantCode(code);
    tenant.setTenantName(code);
    tenant.setStatus(1);
    return tenant;
  }

  private UserTenantPO membership(
      Long userId,
      Long tenantId) {
    UserTenantPO membership = new UserTenantPO();
    membership.setUserId(userId);
    membership.setTenantId(tenantId);
    membership.setStatus(1);
    return membership;
  }
}
