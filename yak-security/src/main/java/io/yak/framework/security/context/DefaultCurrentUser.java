package io.yak.framework.security.context;

import java.util.List;

/** Spring 可注入的当前用户实现。 */
public final class DefaultCurrentUser implements CurrentUser {

  @Override
  public Long getUserId() {
    return YakSecurityContext.getCurrentUserId();
  }

  @Override
  public String getUsername() {
    return YakSecurityContext.getCurrentUsername();
  }

  @Override
  public Long getProjectId() {
    return YakSecurityContext.getCurrentProjectId();
  }

  @Override
  public List<Long> getRoleIds() {
    return YakSecurityContext.getCurrentRoleIds();
  }

  @Override
  public boolean isAuthenticated() {
    return YakSecurityContext.isAuthenticated();
  }
}
