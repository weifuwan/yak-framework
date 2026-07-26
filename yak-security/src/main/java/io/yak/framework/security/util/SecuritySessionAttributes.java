package io.yak.framework.security.util;

/**
 * 服务端登录 Session 使用的属性名。
 *
 * <p>这些属性只能由认证流程在服务端写入，不对应也不接受任何客户端请求头。</p>
 */
public final class SecuritySessionAttributes {

  /** 当前登录用户名。 */
  public static final String USER_NAME =
          SecuritySessionAttributes.class.getName() + ".USER_NAME";

  /** 当前登录用户 ID。 */
  public static final String USER_ID =
          SecuritySessionAttributes.class.getName() + ".USER_ID";

  private SecuritySessionAttributes() {
    throw new IllegalStateException("Utility class");
  }
}
