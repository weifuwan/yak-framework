package io.yak.framework.security.service;

import java.util.Set;
import java.util.function.Supplier;

/** Local cache of permission codes granted to a user. */
public interface PermissionCache {
  Set<String> get(Long userId, Supplier<Set<String>> loader);

  void invalidateUser(Long userId);

  void invalidateRole(Long roleId);

  void invalidateAll();
}
