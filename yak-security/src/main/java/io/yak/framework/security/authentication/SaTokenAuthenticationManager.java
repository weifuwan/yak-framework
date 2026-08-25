package io.yak.framework.security.authentication;

import cn.dev33.satoken.stp.StpLogic;
import java.util.Objects;

/**
 * 基于 Sa-Token 的登录态管理适配器。
 *
 * <p>Sa-Token 被限制在该适配层内，上层 Yak Security 与业务模块无需直接使用 StpUtil 或 StpLogic。
 * 阶段一只提供适配能力，现有 {@code DefaultLoginExtendImpl} 仍保持 HttpSession 登录链路不变。</p>
 *
 * @author weifuwan
 */
public class SaTokenAuthenticationManager
        implements AuthenticationManager {

  private final StpLogic stpLogic;

  /**
   * 创建 Sa-Token 登录态管理器。
   *
   * @param stpLogic Sa-Token 登录逻辑
   */
  public SaTokenAuthenticationManager(
          StpLogic stpLogic) {

    this.stpLogic = Objects.requireNonNull(
            stpLogic,
            "stpLogic must not be null");
  }

  @Override
  public void login(Long userId) {
    Objects.requireNonNull(
            userId,
            "userId must not be null");

    stpLogic.login(userId);
  }

  @Override
  public void logout() {
    stpLogic.logout();
  }

  @Override
  public boolean isLogin() {
    return stpLogic.isLogin();
  }

  @Override
  public Long getLoginUserId() {
    Object loginId = stpLogic.getLoginIdDefaultNull();

    if (loginId == null) {
      return null;
    }

    if (loginId instanceof Number number) {
      return number.longValue();
    }

    if (loginId instanceof CharSequence sequence) {
      String value = sequence.toString().trim();
      if (value.isEmpty()) {
        throw invalidLoginId(loginId, null);
      }

      try {
        return Long.valueOf(value);
      } catch (NumberFormatException exception) {
        throw invalidLoginId(loginId, exception);
      }
    }

    throw invalidLoginId(loginId, null);
  }

  private IllegalStateException invalidLoginId(
          Object loginId,
          Exception cause) {

    String message =
            "Sa-Token loginId must be convertible to Long, actual type="
                    + loginId.getClass().getName();

    return cause == null
            ? new IllegalStateException(message)
            : new IllegalStateException(message, cause);
  }
}
