package io.yak.framework.security.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * HTTP 请求工具类。
 *
 * <p>用户身份只从服务端 Session 获取；项目 ID 仍作为业务上下文从请求头获取。
 *
 * @author weifuwan
 */
public class HttpRequestUtil {

  /**
   * 项目 ID 请求头名称。
   */
  public static final String PROJECT_ID = "X-YAK-SECURITY-PROJECT-ID";

  /**
   * 未授权重定向状态码。
   */
  public static final Integer REDIRECT_CODE = 401;

  /**
   * Cookie 或会话最大有效时间，单位为秒。
   */
  public static final Integer COOKIE_OR_SESSION_MAX_AGE_UNIT_SEC = 86400;

  /**
   * 禁止实例化工具类。
   */
  private HttpRequestUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * 获取当前请求中指定请求头的值。
   *
   * @param headerKey 请求头名称
   * @return 请求头对应的值
   */
  public static String getHeaderValue(String headerKey) {
    HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
    return request.getHeader(headerKey);
  }

  /**
   * 获取当前请求的操作人用户名。
   *
   * @return 操作人用户名
   */
  public static String getOperator() {
    HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
    return HttpRequestUtil.getOperator(request);
  }

  /**
   * 从服务端 Session 中获取操作人用户名。
   *
   * @param request HTTP 请求对象
   * @return 操作人用户名
   */
  public static String getOperator(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object operator = session.getAttribute(SecuritySessionAttributes.USER_NAME);
    return operator instanceof String && StringUtils.hasText((String) operator)
            ? (String) operator
            : null;
  }

  /**
   * 从服务端 Session 中获取操作人用户 ID。
   *
   * @param request HTTP 请求对象
   * @return 操作人用户 ID
   */
  public static Long getOperatorId(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object value = session.getAttribute(SecuritySessionAttributes.USER_ID);
    return value instanceof Number
            ? ((Number) value).longValue()
            : null;
  }

  /**
   * 从请求头中获取项目 ID，不存在时返回默认项目 ID。
   *
   * @param request HTTP 请求对象
   * @param defaultAppid 默认项目 ID
   * @return 项目 ID
   */
  public static Long getProjectId(
          HttpServletRequest request,
          int defaultAppid) {

    String projectIdStr = request.getHeader(PROJECT_ID);
    if (StringUtils.isEmpty((Object) projectIdStr)) {
      return (long) defaultAppid;
    }
    return HttpRequestUtil.strConvertInteger(projectIdStr);
  }

  /**
   * 从请求头中获取项目 ID。
   *
   * @param request HTTP 请求对象
   * @return 项目 ID，请求头不存在时返回 null
   */
  public static Long getProjectId(HttpServletRequest request) {
    String projectIdStr = request.getHeader(PROJECT_ID);
    if (StringUtils.isEmpty((Object) projectIdStr)) {
      return null;
    }
    return HttpRequestUtil.strConvertInteger(projectIdStr);
  }

  /**
   * 将字符串转换为 Long 类型。
   *
   * @param str 待转换的字符串
   * @return 转换后的 Long 值，字符串为空或转换失败时返回 null
   */
  private static Long strConvertInteger(String str) {
    try {
      return StringUtils.isEmpty((Object) str)
              ? null
              : Long.valueOf(str);
    } catch (Exception ignore) {
      return null;
    }
  }
}
