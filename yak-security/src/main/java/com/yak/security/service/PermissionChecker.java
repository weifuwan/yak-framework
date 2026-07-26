package com.yak.security.service;
public class PermissionChecker {
  public boolean hasPermission(String permission) {
    AuthenticatedUser u = SecurityContext.get();
    return u != null && (u.getPermissions().contains("*") ||
                         u.getPermissions().contains(permission));
  }
  public void require(String permission) {
    if (!hasPermission(permission))
      throw new SecurityException("permission denied: " + permission);
  }
}
