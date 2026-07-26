package com.yak.security.service;
import java.util.UUID;
import java.util.concurrent.*;
public class InMemoryTokenService implements TokenService {
  private static class Entry {
    final AuthenticatedUser user;
    final long expires;
    Entry(AuthenticatedUser u, long e) {
      user = u;
      expires = e;
    }
  }
  private final ConcurrentMap<String, Entry> entries =
      new ConcurrentHashMap<String, Entry>();
  private final long ttlMillis;
  public InMemoryTokenService(long seconds) {
    ttlMillis = Math.multiplyExact(seconds, 1000L);
  }
  public String issue(AuthenticatedUser u) {
    String t = UUID.randomUUID().toString();
    entries.put(t, new Entry(u, System.currentTimeMillis() + ttlMillis));
    return t;
  }
  public AuthenticatedUser resolve(String t) {
    if (t == null)
      return null;
    Entry e = entries.get(t);
    if (e == null)
      return null;
    if (e.expires < System.currentTimeMillis()) {
      entries.remove(t);
      return null;
    }
    return e.user;
  }
  public void revoke(String t) {
    if (t != null)
      entries.remove(t);
  }
}
