package io.yak.framework.security.extend;

import java.time.Duration;
import java.util.Optional;

/** Storage abstraction for tokens and their session values. */
public interface TokenSessionStore {
  void put(String token, Object session, Duration ttl);
  Optional<Object> get(String token);
  void remove(String token);
}
