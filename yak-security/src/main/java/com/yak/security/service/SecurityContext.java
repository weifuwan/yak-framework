package com.yak.security.service;
public final class SecurityContext {
  private static final ThreadLocal<AuthenticatedUser> USER =
      new ThreadLocal<AuthenticatedUser>();
  private SecurityContext() {}
  public static AuthenticatedUser get() { return USER.get(); }
  public static void set(AuthenticatedUser u) { USER.set(u); }
  public static void clear() { USER.remove(); }
}
