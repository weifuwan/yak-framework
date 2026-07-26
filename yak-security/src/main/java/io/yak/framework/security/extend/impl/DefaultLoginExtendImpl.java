package io.yak.framework.security.extend.impl;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.LoginExtend;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.SecuritySessionAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 默认登录认证扩展实现。
 *
 * <p>使用用户名、密码和服务端 Session 完成登录认证。</p>
 *
 * @author weifuwan
 */
public class DefaultLoginExtendImpl
        implements LoginExtend {

  private static final Logger LOGGER =
          LoggerFactory.getLogger(
                  DefaultLoginExtendImpl.class);

  /**
   * 用户禁用状态。
   *
   * <p>后续建议替换为明确的用户状态枚举。</p>
   */
  private static final Integer USER_DISABLED_STATUS = 2;

  /**
   * 路径匹配器。
   */
  private static final AntPathMatcher PATH_MATCHER =
          new AntPathMatcher();

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;
  private final LoginAttemptGuard loginAttemptGuard;
  private final YakSecurityProperties.LoginSecurityProperties loginProperties;
  private final int sessionTimeoutSeconds;

  /**
   * 创建默认登录扩展。
   *
   * @param userService 用户服务
   * @param passwordEncoder 密码编码器
   */
  public DefaultLoginExtendImpl(
          UserService userService,
          PasswordEncoder passwordEncoder) {

    this(userService, passwordEncoder, new YakSecurityProperties());
  }

  @Autowired
  public DefaultLoginExtendImpl(
          UserService userService,
          PasswordEncoder passwordEncoder,
          YakSecurityProperties properties) {

    this.userService =
            Objects.requireNonNull(
                    userService,
                    "userService must not be null");

    this.passwordEncoder =
            Objects.requireNonNull(
                    passwordEncoder,
                    "passwordEncoder must not be null");
    Objects.requireNonNull(properties, "properties must not be null");
    this.loginProperties = properties.getLogin();
    this.loginAttemptGuard = new LoginAttemptGuard(loginProperties);
    long timeout = properties.getSession().getTimeout().getSeconds();
    if (timeout < 1 || timeout > Integer.MAX_VALUE) {
      throw new IllegalArgumentException(
              "session.timeout must be between 1 second and 2147483647 seconds");
    }
    this.sessionTimeoutSeconds = (int) timeout;
  }

  /**
   * 校验账号密码并初始化登录会话。
   *
   * @param loginDTO 登录参数
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 登录用户简要信息
   */
  @Override
  public UserBriefVO verifyLogin(
          AccountLoginDTO loginDTO,
          HttpServletRequest request,
          HttpServletResponse response)
          throws YakSecurityException {

    validateLoginParam(
            loginDTO,
            request,
            response);

    String userName =
            loginDTO.getUserName().trim();

    String remoteAddress = request.getRemoteAddr();
    if (loginAttemptGuard.isBlocked(userName, remoteAddress)) {
      throw new YakSecurityException(ResultCode.USER_ACCOUNT_LOCKED);
    }

    User user =
            userService.getUserByUsername(userName);

    if (user == null) {
      loginAttemptGuard.recordFailure(userName, remoteAddress);
      throw new YakSecurityException(
              loginProperties.isHideAccountNotFound()
                      ? ResultCode.USER_CREDENTIALS_ERROR
                      : ResultCode.USER_NOT_EXISTS);
    }

    if (USER_DISABLED_STATUS.equals(
            user.getStatus())) {

      throw new YakSecurityException(
              ResultCode.USER_ACCOUNT_DISABLE);
    }

    if (!passwordEncoder.matches(
            loginDTO.getPw(),
            user.getPw())) {

      loginAttemptGuard.recordFailure(userName, remoteAddress);
      throw new YakSecurityException(
              ResultCode.USER_CREDENTIALS_ERROR);
    }

    if (user.getId() == null) {
      LOGGER.error(
              "登录用户缺少用户 ID，userName={}",
              userName);

      throw new IllegalStateException(
              "Login user id must not be null");
    }

    initLoginContext(
            request,
            userName,
            user.getId(),
            user.getPw());

    loginAttemptGuard.recordSuccess(userName, remoteAddress);

    return CopyBeanUtil.copy(
            user,
            UserBriefVO.class);
  }

  /**
   * 退出登录。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 退出结果
   */
  @Override
  public Result<Boolean> logout(
          HttpServletRequest request,
          HttpServletResponse response) {

    Objects.requireNonNull(
            request,
            "request must not be null");

    Objects.requireNonNull(
            response,
            "response must not be null");

    clearLoginContext(request);

    return Result.success(Boolean.TRUE);
  }

  /**
   * 检查请求登录状态。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @param requestPath 请求路径
   * @param whiteListPatterns 白名单表达式
   * @return 是否允许继续访问
   */
  @Override
  public boolean interceptorCheck(
          HttpServletRequest request,
          HttpServletResponse response,
          String requestPath,
          List<String> whiteListPatterns)
          throws IOException {

    Objects.requireNonNull(
            request,
            "request must not be null");

    Objects.requireNonNull(
            response,
            "response must not be null");

    if (!StringUtils.hasText(requestPath)) {
      LOGGER.error(
              "请求路径为空，requestUri={}",
              request.getRequestURI());

      response.setStatus(
              HttpServletResponse.SC_BAD_REQUEST);

      return false;
    }

    if (isWhiteListPath(
            requestPath,
            whiteListPatterns)) {

      return true;
    }

    HttpSession session =
            request.getSession(false);

    if (session == null) {
      handleUnauthorized(
              request,
              response);

      return false;
    }

    String operator =
            getSessionUserName(session);

    Long sessionUserId =
            getSessionUserId(session);

    if (!StringUtils.hasText(operator)
            || sessionUserId == null) {

      handleUnauthorized(
              request,
              response);

      return false;
    }

    User user =
            userService.getUserByUsername(operator);

    if (user == null
            || USER_DISABLED_STATUS.equals(
            user.getStatus())
            || !Objects.equals(
            sessionUserId,
            user.getId())
            || !Objects.equals(
            session.getAttribute(SecuritySessionAttributes.CREDENTIAL_VERSION),
            user.getPw())) {

      LOGGER.warn(
              "登录会话失效，operator={}, sessionUserId={}",
              operator,
              sessionUserId);

      handleUnauthorized(
              request,
              response);

      return false;
    }

    return true;
  }

  /**
   * 初始化登录上下文。
   */
  private void initLoginContext(
          HttpServletRequest request,
          String userName,
          Long userId,
          String credentialVersion) {

    HttpSession existingSession =
            request.getSession(false);

    if (existingSession != null) {
      request.changeSessionId();
    }

    HttpSession session =
            request.getSession(true);

    session.setMaxInactiveInterval(
            sessionTimeoutSeconds);

    session.setAttribute(
            SecuritySessionAttributes.USER_NAME,
            userName);

    session.setAttribute(
            SecuritySessionAttributes.USER_ID,
            userId);

    session.setAttribute(
            SecuritySessionAttributes.CREDENTIAL_VERSION,
            credentialVersion);
  }

  /**
   * 清理登录上下文。
   */
  private void clearLoginContext(
          HttpServletRequest request) {

    HttpSession session =
            request.getSession(false);

    if (session != null) {
      try {
        session.invalidate();
      } catch (IllegalStateException exception) {
        LOGGER.debug(
                "Session 已失效，无需重复清理");
      }
    }

  }

  /**
   * 处理未登录请求。
   */
  private void handleUnauthorized(
          HttpServletRequest request,
          HttpServletResponse response) {

    clearLoginContext(request);

    response.setStatus(
            HttpServletResponse.SC_UNAUTHORIZED);
  }

  /**
   * 判断是否为白名单路径。
   */
  private boolean isWhiteListPath(
          String requestPath,
          List<String> whiteListPatterns) {

    if (CollectionUtils.isEmpty(
            whiteListPatterns)) {

      return false;
    }

    for (String pattern : whiteListPatterns) {
      if (!StringUtils.hasText(pattern)) {
        continue;
      }

      if (PATH_MATCHER.match(
              pattern.trim(),
              requestPath)) {

        return true;
      }
    }

    return false;
  }

  /**
   * 获取 Session 用户名。
   */
  private String getSessionUserName(
          HttpSession session) {

    Object value =
            session.getAttribute(SecuritySessionAttributes.USER_NAME);

    return value instanceof String
            ? (String) value
            : null;
  }

  /**
   * 获取 Session 用户 ID。
   */
  private Long getSessionUserId(
          HttpSession session) {

    Object value =
            session.getAttribute(SecuritySessionAttributes.USER_ID);

    if (value instanceof Long) {
      return (Long) value;
    }

    if (value instanceof Number) {
      return ((Number) value).longValue();
    }

    return null;
  }

  /**
   * 校验登录参数。
   */
  private void validateLoginParam(
          AccountLoginDTO loginDTO,
          HttpServletRequest request,
          HttpServletResponse response)
          throws YakSecurityException {

    if (loginDTO == null
            || request == null
            || response == null
            || !StringUtils.hasText(
            loginDTO.getUserName())
            || !StringUtils.hasText(
            loginDTO.getPw())) {

      throw new YakSecurityException(
              ResultCode.PARAM_NOT_VALID);
    }
  }
}
