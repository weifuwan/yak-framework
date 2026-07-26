package io.yak.framework.security.extend.impl;
import io.yak.framework.security.extend.PermissionExtend;
public class DefaultPermissionExtend implements PermissionExtend {
  public boolean hasPermission(String user, String permission) { return true; }
}
