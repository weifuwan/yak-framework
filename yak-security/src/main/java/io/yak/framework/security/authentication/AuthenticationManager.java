package io.yak.framework.security.authentication;

/**
 * 登录态管理边界。
 *
 * <p>该接口只描述“建立、清理和读取登录态”，不负责账号密码校验、用户查询、RBAC、项目权限等业务安全逻辑。
 * Yak Security 的上层代码应依赖该边界，而不是直接依赖具体 Token 框架。</p>
 *
 * @author weifuwan
 */
public interface AuthenticationManager {

  /**
   * 为指定用户建立登录态。
   *
   * @param userId 用户 ID
   */
  void login(Long userId);

  /**
   * 为指定用户建立登录态，并保存 Yak Security 校验请求所需的服务端认证元数据。
   *
   * <p>默认实现仅建立登录态，支持认证元数据的实现应覆盖此方法。</p>
   *
   * @param userId 用户 ID
   * @param userName 用户名
   * @param credentialVersion 凭证版本
   */
  default void login(
          Long userId,
          String userName,
          String credentialVersion) {
    login(userId);
  }

  /**
   * 清理当前请求上下文中的登录态。
   */
  void logout();

  /**
   * 判断当前请求是否已登录。
   *
   * @return 已登录返回 {@code true}
   */
  boolean isLogin();

  /**
   * 获取当前登录用户 ID。
   *
   * @return 未登录时返回 {@code null}
   */
  Long getLoginUserId();

  /**
   * 获取当前登录用户名。
   *
   * @return 未提供该元数据时返回 {@code null}
   */
  default String getLoginUsername() {
    return null;
  }

  /**
   * 获取建立登录态时记录的凭证版本。
   *
   * @return 未提供该元数据时返回 {@code null}
   */
  default String getCredentialVersion() {
    return null;
  }
}
