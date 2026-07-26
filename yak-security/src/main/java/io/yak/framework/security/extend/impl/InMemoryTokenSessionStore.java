package io.yak.framework.security.extend.impl;

import io.yak.framework.security.extend.TokenSessionStore;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于本地内存的 Token 会话存储。
 *
 * <p>该实现仅适用于单实例应用、开发环境或测试环境。
 * 集群部署时应使用 Redis 等共享存储实现。</p>
 *
 * @author weifuwan
 */
public class InMemoryTokenSessionStore
        implements TokenSessionStore {

  /**
   * 每写入指定次数后执行一次过期数据清理。
   */
  private static final int CLEANUP_INTERVAL = 256;

  /**
   * Token 会话容器。
   */
  private final ConcurrentHashMap<String, Entry> sessions =
          new ConcurrentHashMap<String, Entry>();

  /**
   * 时间提供器。
   */
  private final Clock clock;

  /**
   * 写入次数。
   */
  private final AtomicInteger writeCount =
          new AtomicInteger();

  /**
   * 创建使用系统时间的会话存储。
   */
  public InMemoryTokenSessionStore() {
    this(Clock.systemUTC());
  }

  /**
   * 创建指定时间提供器的会话存储。
   *
   * @param clock 时间提供器
   */
  InMemoryTokenSessionStore(Clock clock) {
    this.clock = Objects.requireNonNull(
            clock,
            "clock must not be null");
  }

  /**
   * 保存 Token 会话。
   *
   * @param token Token
   * @param session 会话对象
   * @param ttl 有效时长
   */
  @Override
  public void put(
          String token,
          Object session,
          Duration ttl) {

    validateToken(token);

    Objects.requireNonNull(
            session,
            "session must not be null");

    Objects.requireNonNull(
            ttl,
            "ttl must not be null");

    if (ttl.isZero() || ttl.isNegative()) {
      throw new IllegalArgumentException(
              "ttl must be greater than zero");
    }

    long expiresAt =
            calculateExpiresAt(ttl);

    sessions.put(
            token,
            new Entry(session, expiresAt));

    if (writeCount.incrementAndGet()
            % CLEANUP_INTERVAL == 0) {

      removeExpiredEntries();
    }
  }

  /**
   * 获取 Token 对应的会话。
   *
   * @param token Token
   * @return 会话对象
   */
  @Override
  public Optional<Object> get(String token) {
    if (isBlank(token)) {
      return Optional.empty();
    }

    Entry entry = sessions.get(token);
    if (entry == null) {
      return Optional.empty();
    }

    if (isExpired(entry)) {
      sessions.remove(token, entry);
      return Optional.empty();
    }

    return Optional.of(entry.getValue());
  }

  /**
   * 删除 Token 会话。
   *
   * @param token Token
   */
  @Override
  public void remove(String token) {
    if (isBlank(token)) {
      return;
    }

    sessions.remove(token);
  }

  /**
   * 计算过期时间。
   *
   * @param ttl 有效时长
   * @return 过期时间戳
   */
  private long calculateExpiresAt(Duration ttl) {
    long ttlMillis;

    try {
      ttlMillis = ttl.toMillis();
    } catch (ArithmeticException exception) {
      return Long.MAX_VALUE;
    }

    if (ttlMillis <= 0) {
      ttlMillis = 1;
    }

    try {
      return Math.addExact(
              clock.millis(),
              ttlMillis);
    } catch (ArithmeticException exception) {
      return Long.MAX_VALUE;
    }
  }

  /**
   * 判断会话是否已过期。
   *
   * @param entry 会话记录
   * @return 已过期返回 {@code true}
   */
  private boolean isExpired(Entry entry) {
    return entry.getExpiresAt()
            <= clock.millis();
  }

  /**
   * 清理过期会话。
   */
  private void removeExpiredEntries() {
    for (Map.Entry<String, Entry> item
            : sessions.entrySet()) {

      Entry entry = item.getValue();

      if (isExpired(entry)) {
        sessions.remove(
                item.getKey(),
                entry);
      }
    }
  }

  /**
   * 校验 Token。
   *
   * @param token Token
   */
  private void validateToken(String token) {
    if (isBlank(token)) {
      throw new IllegalArgumentException(
              "token must not be blank");
    }
  }

  /**
   * 判断字符串是否为空白。
   *
   * <p>用于替代 Java 11 才提供的
   * {@link String#isBlank()} 方法。</p>
   *
   * @param value 字符串
   * @return 为空、空字符串或仅包含空白字符时返回 {@code true}
   */
  private boolean isBlank(String value) {
    if (value == null || value.isEmpty()) {
      return true;
    }

    for (int index = 0;
         index < value.length();
         index++) {

      if (!Character.isWhitespace(
              value.charAt(index))) {

        return false;
      }
    }

    return true;
  }

  /**
   * Token 会话记录。
   */
  private static final class Entry {

    /**
     * 会话对象。
     */
    private final Object value;

    /**
     * 过期时间戳。
     */
    private final long expiresAt;

    private Entry(
            Object value,
            long expiresAt) {

      this.value = value;
      this.expiresAt = expiresAt;
    }

    private Object getValue() {
      return value;
    }

    private long getExpiresAt() {
      return expiresAt;
    }
  }
}