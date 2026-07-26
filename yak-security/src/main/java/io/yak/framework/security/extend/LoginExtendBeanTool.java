package io.yak.framework.security.extend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 登录扩展兼容访问工具。
 *
 * <p>新代码建议直接注入 {@link LoginExtend}，不再通过该包装类访问。</p>
 *
 * @author weifuwan
 * @deprecated 请直接注入 {@link LoginExtend}
 */
@Getter
@Deprecated
@RequiredArgsConstructor
public class LoginExtendBeanTool {

  /**
   * 登录扩展实现。
   */
  private final LoginExtend loginExtend;

  /**
   * 获取登录扩展实现。
   *
   * @return 登录扩展实现
   * @deprecated 请使用 {@link #getLoginExtend()}
   */
  @Deprecated
  public LoginExtend getLoginExtendImpl() {
    return loginExtend;
  }
}