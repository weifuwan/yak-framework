package io.yak.framework.security.extend;

import java.time.Duration;
import java.util.Optional;

/**
 * Token 会话存储接口。
 *
 * <p>可以使用内存、Redis 或其他存储组件实现。</p>
 *
 * @author weifuwan
 */
public interface TokenSessionStore {

  /**
   * 保存 Token 会话。
   *
   * @param token Token
   * @param session 会话对象
   * @param ttl 有效时长
   */
  void put(
          String token,
          Object session,
          Duration ttl);

  /**
   * 获取 Token 对应的会话对象。
   *
   * @param token Token
   * @return 会话对象；不存在或已过期时返回空
   */
  Optional<Object> get(String token);

  /**
   * 删除 Token 会话。
   *
   * @param token Token
   */
  void remove(String token);
}