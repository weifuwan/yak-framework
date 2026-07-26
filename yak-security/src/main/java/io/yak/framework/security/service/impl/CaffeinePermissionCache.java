package io.yak.framework.security.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.service.PermissionCache;
import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/** Caffeine-backed permission cache for a single application instance. */
@Service
@ConditionalOnMissingBean(PermissionCache.class)
public class CaffeinePermissionCache implements PermissionCache {
  private final String applicationName;
  private final boolean enabled;
  private final Cache<Key, Set<String>> cache;
  private final UserRoleDao userRoleDao;

  public CaffeinePermissionCache(
          YakSecurityProperties properties,
          UserRoleDao userRoleDao) {
    YakSecurityProperties.PermissionCacheProperties settings =
            properties.getPermissionCache();
    this.applicationName = properties.getApplicationName();
    this.enabled = settings.isEnabled();
    this.userRoleDao = userRoleDao;
    this.cache = Caffeine.newBuilder()
            .maximumSize(Math.max(1, settings.getMaximumSize()))
            .expireAfterWrite(Duration.ofMinutes(
                    Math.max(1, settings.getTtlMinutes())))
            .build();
  }

  @Override
  public Set<String> get(Long userId, Supplier<Set<String>> loader) {
    if (userId == null) {
      return Collections.emptySet();
    }
    if (!enabled) {
      return immutable(loader.get());
    }
    return cache.get(new Key(applicationName, userId),
            ignored -> immutable(loader.get()));
  }

  @Override
  public void invalidateUser(Long userId) {
    if (userId != null) {
      cache.invalidate(new Key(applicationName, userId));
    }
  }

  @Override
  public void invalidateRole(Long roleId) {
    if (roleId != null) {
      userRoleDao.selectUserIdListByRoleId(roleId)
              .forEach(this::invalidateUser);
    }
  }

  @Override
  public void invalidateAll() {
    cache.invalidateAll();
  }

  private static Set<String> immutable(Set<String> values) {
    return values == null || values.isEmpty()
            ? Collections.emptySet() : Set.copyOf(values);
  }

  private record Key(String applicationName, Long userId) { }
}
