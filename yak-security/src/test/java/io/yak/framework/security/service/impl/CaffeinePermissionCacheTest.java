package io.yak.framework.security.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.dao.UserRoleDao;
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
  void invalidatesEveryUserAssignedToRole() {
    UserRoleDao userRoleDao = mock(UserRoleDao.class);
    when(userRoleDao.selectUserIdListByRoleId(3L)).thenReturn(java.util.List.of(7L));
    CaffeinePermissionCache cache = new CaffeinePermissionCache(
            properties(), userRoleDao);
    AtomicInteger loads = new AtomicInteger();
    cache.get(7L, () -> load(loads));

    cache.invalidateRole(3L);
    cache.get(7L, () -> load(loads));

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
}
