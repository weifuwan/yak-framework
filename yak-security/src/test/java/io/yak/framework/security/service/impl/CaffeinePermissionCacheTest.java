package io.yak.framework.security.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.context.AuthorizationSnapshot;
import io.yak.framework.security.dao.UserRoleDao;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class CaffeinePermissionCacheTest {

  @Test
  void cachesLoadedPermissionsUntilUserIsInvalidated() {
    UserRoleDao userRoleDao = mock(UserRoleDao.class);
    CaffeinePermissionCache cache = new CaffeinePermissionCache(
            properties(), userRoleDao);
    AtomicInteger loads = new AtomicInteger();

    assertThat(cache.get(7L, () -> load(loads))).containsExactly("user:read");
    assertThat(cache.get(7L, () -> load(loads))).containsExactly("user:read");
    assertThat(loads).hasValue(1);

    cache.invalidateUser(7L);

    assertThat(cache.get(7L, () -> load(loads))).containsExactly("user:read");
    assertThat(loads).hasValue(2);
  }

  @Test
  void cachesAuthorizationSnapshotAndSharesUserInvalidation() {
    UserRoleDao userRoleDao = mock(UserRoleDao.class);
    CaffeinePermissionCache cache = new CaffeinePermissionCache(
            properties(), userRoleDao);
    AtomicInteger loads = new AtomicInteger();

    AuthorizationSnapshot first = cache.getAuthorizationSnapshot(
            7L,
            () -> snapshot(loads));
    AuthorizationSnapshot second = cache.getAuthorizationSnapshot(
            7L,
            () -> snapshot(loads));

    assertThat(first).isSameAs(second);
    assertThat(first.getRoleIds()).containsExactly(3L);
    assertThat(first.getPermissionCodes()).containsExactly("user:read");
    assertThat(first.getMenuCodes()).containsExactly("users");
    assertThat(first.getProjectIds()).containsExactly(9L);
    assertThat(loads).hasValue(1);

    cache.invalidateUser(7L);
    cache.getAuthorizationSnapshot(7L, () -> snapshot(loads));
    assertThat(loads).hasValue(2);
  }

  @Test
  void invalidatesEveryUserAssignedToRole() {
    UserRoleDao userRoleDao = mock(UserRoleDao.class);
    when(userRoleDao.selectUserIdListByRoleId(3L)).thenReturn(List.of(7L));
    CaffeinePermissionCache cache = new CaffeinePermissionCache(
            properties(), userRoleDao);
    AtomicInteger loads = new AtomicInteger();
    cache.getAuthorizationSnapshot(7L, () -> snapshot(loads));

    cache.invalidateRole(3L);
    cache.getAuthorizationSnapshot(7L, () -> snapshot(loads));

    assertThat(loads).hasValue(2);
  }

  private static YakSecurityProperties properties() {
    YakSecurityProperties properties = new YakSecurityProperties();
    properties.setApplicationName("test-app");
    return properties;
  }

  private static Set<String> load(AtomicInteger loads) {
    loads.incrementAndGet();
    return Set.of("user:read");
  }

  private static AuthorizationSnapshot snapshot(
          AtomicInteger loads) {
    loads.incrementAndGet();
    return new AuthorizationSnapshot(
            List.of(3L),
            Set.of("user:read"),
            List.of("users"),
            Set.of(9L));
  }
}
