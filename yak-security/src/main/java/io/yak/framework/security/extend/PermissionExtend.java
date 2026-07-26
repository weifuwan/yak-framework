package io.yak.framework.security.extend;

/** Host hook for adding permission decisions to Yak's built-in model. */
public interface PermissionExtend {
  boolean hasPermission(String user, String permission);
}
