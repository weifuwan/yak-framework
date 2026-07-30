package io.yak.framework.security.context;

import java.util.Collections;
import java.util.List;

/**
 * Yak Security 当前用户上下文的静态访问入口。
 *
 * <p>上下文与当前请求线程绑定，并在请求结束时自动清理。
 * 业务代码应优先注入 {@link CurrentUser}；非 Spring 管理的代码可使用此类。</p>
 */
public final class YakSecurityContext {

  private static final CurrentUser ANONYMOUS =
      new ImmutableCurrentUser(
          null,
          null,
          null,
          Collections.emptyList(),
          false,
          TenantIdentity.none());

  private static final ThreadLocal<CurrentUser> HOLDER =
      new ThreadLocal<>();

  private YakSecurityContext() {
    throw new IllegalStateException("Utility class");
  }

  public static Long getCurrentUserId() {
    return currentUser().getUserId();
  }

  public static String getCurrentUsername() {
    return currentUser().getUsername();
  }

  public static Long getCurrentProjectId() {
    return currentUser().getProjectId();
  }

  public static List<Long> getCurrentRoleIds() {
    return currentUser().getRoleIds();
  }

  public static Long getCurrentTenantId() {
    return currentUser().getTenantId();
  }

  public static String getCurrentTenantCode() {
    return currentUser().getTenantCode();
  }

  public static String getCurrentTenantName() {
    return currentUser().getTenantName();
  }

  public static boolean isAuthenticated() {
    return currentUser().isAuthenticated();
  }

  static CurrentUser currentUser() {
    CurrentUser currentUser = HOLDER.get();
    return currentUser == null
        ? ANONYMOUS
        : currentUser;
  }

  static void setCurrentUser(
      CurrentUser currentUser) {
    HOLDER.set(currentUser);
  }

  static void clear() {
    HOLDER.remove();
  }

  static final class ImmutableCurrentUser
      implements CurrentUser {

    private final Long userId;
    private final String username;
    private final Long projectId;
    private final List<Long> roleIds;
    private final boolean authenticated;
    private final TenantIdentity tenantIdentity;

    ImmutableCurrentUser(
        Long userId,
        String username,
        Long projectId,
        List<Long> roleIds,
        boolean authenticated) {
      this(
          userId,
          username,
          projectId,
          roleIds,
          authenticated,
          TenantIdentity.none());
    }

    ImmutableCurrentUser(
        Long userId,
        String username,
        Long projectId,
        List<Long> roleIds,
        boolean authenticated,
        TenantIdentity tenantIdentity) {
      this.userId = userId;
      this.username = username;
      this.projectId = projectId;
      this.roleIds = roleIds == null
          ? Collections.emptyList()
          : List.copyOf(roleIds);
      this.authenticated = authenticated;
      this.tenantIdentity =
          tenantIdentity == null
              ? TenantIdentity.none()
              : tenantIdentity;
    }

    @Override
    public Long getUserId() {
      return userId;
    }

    @Override
    public String getUsername() {
      return username;
    }

    @Override
    public Long getProjectId() {
      return projectId;
    }

    @Override
    public List<Long> getRoleIds() {
      return roleIds;
    }

    @Override
    public boolean isAuthenticated() {
      return authenticated;
    }

    @Override
    public Long getTenantId() {
      return tenantIdentity.getTenantId();
    }

    @Override
    public String getTenantCode() {
      return tenantIdentity.getTenantCode();
    }

    @Override
    public String getTenantName() {
      return tenantIdentity.getTenantName();
    }
  }
}
