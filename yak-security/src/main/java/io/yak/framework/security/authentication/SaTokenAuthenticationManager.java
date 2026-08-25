package io.yak.framework.security.authentication;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import java.time.Duration;
import java.util.Objects;
import org.springframework.util.StringUtils;

/**
 * 基于 Sa-Token 的登录态管理适配器。
 *
 * <p>Sa-Token 被限制在该适配层内，上层 Yak Security 与业务模块无需直接使用 StpUtil 或 StpLogic。</p>
 *
 * @author weifuwan
 */
public class SaTokenAuthenticationManager
        implements AuthenticationManager {

  private static final String USERNAME_KEY =
          "yak-security:username";

  private static final String CREDENTIAL_VERSION_KEY =
          "yak-security:credential-version";

  private final StpLogic stpLogic;

  private final Long activeTimeoutSeconds;

  /**
   * 创建使用 Sa-Token 全局默认超时配置的登录态管理器。
   *
   * @param stpLogic Sa-Token 登录逻辑
   */
  public SaTokenAuthenticationManager(
          StpLogic stpLogic) {
    this(stpLogic, null);
  }

  /**
   * 创建 Sa-Token 登录态管理器，并把 Yak Security 的 Session 空闲超时映射为 Sa-Token activeTimeout。
   *
   * @param stpLogic Sa-Token 登录逻辑
   * @param activeTimeout 登录态无操作超时；为 {@code null} 时使用 Sa-Token 全局配置
   */
  public SaTokenAuthenticationManager(
          StpLogic stpLogic,
          Duration activeTimeout) {

    this.stpLogic = Objects.requireNonNull(
            stpLogic,
            "stpLogic must not be null");

    if (activeTimeout == null) {
      this.activeTimeoutSeconds = null;
      return;
    }

    long seconds = activeTimeout.getSeconds();
    if (seconds < 1) {
      throw new IllegalArgumentException(
              "activeTimeout must be greater than 0 seconds");
    }

    this.activeTimeoutSeconds = seconds;

    /*
     * Sa-Token 只有在全局 activeTimeout 已开启或 dynamicActiveTimeout=true 时
     * 才会检查单次登录参数中的 activeTimeout。这里开启动态模式，使 Yak Security
     * 可以继续使用自己的 session.timeout 表达“无操作超时”。
     */
    SaTokenConfig config =
            this.stpLogic.getConfigOrGlobal();
    config.setDynamicActiveTimeout(true);
  }

  @Override
  public void login(Long userId) {
    Objects.requireNonNull(
            userId,
            "userId must not be null");

    if (activeTimeoutSeconds == null) {
      stpLogic.login(userId);
      return;
    }

    SaLoginParameter loginParameter =
            stpLogic.createSaLoginParameter()
                    .setActiveTimeout(activeTimeoutSeconds)
                    .setIsLastingCookie(false)
                    .setIsConcurrent(true)
                    .setIsShare(false)
                    .setMaxLoginCount(-1);

    stpLogic.login(userId, loginParameter);
  }

  @Override
  public void login(
          Long userId,
          String userName,
          String credentialVersion) {

    if (!StringUtils.hasText(userName)) {
      throw new IllegalArgumentException(
              "userName must not be blank");
    }
    if (!StringUtils.hasText(credentialVersion)) {
      throw new IllegalArgumentException(
              "credentialVersion must not be blank");
    }

    login(userId);

    SaSession tokenSession =
            stpLogic.getTokenSession(true);
    tokenSession.set(USERNAME_KEY, userName);
    tokenSession.set(
            CREDENTIAL_VERSION_KEY,
            credentialVersion);
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

  @Override
  public String getLoginUsername() {
    return getTokenSessionString(USERNAME_KEY);
  }

  @Override
  public String getCredentialVersion() {
    return getTokenSessionString(
            CREDENTIAL_VERSION_KEY);
  }

  private String getTokenSessionString(
          String key) {

    if (!stpLogic.isLogin()) {
      return null;
    }

    SaSession tokenSession =
            stpLogic.getTokenSession(false);
    if (tokenSession == null) {
      return null;
    }

    Object value = tokenSession.get(key);
    if (!(value instanceof String text)
            || !StringUtils.hasText(text)) {
      return null;
    }

    return text;
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
