package io.yak.framework.security.extend.impl;
import io.yak.framework.security.extend.TokenSessionStore;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
public class InMemoryTokenSessionStore implements TokenSessionStore {
  private record Entry(Object value, long expiresAt) {}
  private final ConcurrentHashMap<String, Entry> sessions = new ConcurrentHashMap<>();
  public void put(String token, Object session, Duration ttl) {
    sessions.put(token, new Entry(session, System.currentTimeMillis() + ttl.toMillis()));
  }
  public Optional<Object> get(String token) {
    Entry entry = sessions.get(token);
    if (entry == null) return Optional.empty();
    if (entry.expiresAt() <= System.currentTimeMillis()) { sessions.remove(token); return Optional.empty(); }
    return Optional.ofNullable(entry.value());
  }
  public void remove(String token) { sessions.remove(token); }
}
