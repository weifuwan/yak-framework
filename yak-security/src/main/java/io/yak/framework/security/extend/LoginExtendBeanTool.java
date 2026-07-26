package io.yak.framework.security.extend;

/** Backward-compatible access wrapper that now resolves extensions by type. */
public class LoginExtendBeanTool {
  private final LoginExtend loginExtend;
  public LoginExtendBeanTool(LoginExtend loginExtend) { this.loginExtend = loginExtend; }
  public LoginExtend getLoginExtendImpl() { return loginExtend; }
}
