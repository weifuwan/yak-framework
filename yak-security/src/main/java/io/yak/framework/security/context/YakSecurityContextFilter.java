package io.yak.framework.security.context;

import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.extend.TenantContextProvider;
import io.yak.framework.security.util.HttpRequestUtil;
import io.yak.framework.security.util.SecuritySessionAttributes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/** 从服务端 Session 构建当前用户和租户上下文。 */
public final class YakSecurityContextFilter
    extends OncePerRequestFilter {

  private final ObjectProvider<UserRoleDao>
      userRoleDaoProvider;
  private final ObjectProvider<TenantContextProvider>
      tenantContextProvider;

  public YakSecurityContextFilter(
      ObjectProvider<UserRoleDao>
          userRoleDaoProvider) {
    this(userRoleDaoProvider, null);
  }

  public YakSecurityContextFilter(
      ObjectProvider<UserRoleDao>
          userRoleDaoProvider,
      ObjectProvider<TenantContextProvider>
          tenantContextProvider) {
    this.userRoleDaoProvider =
        userRoleDaoProvider;
    this.tenantContextProvider =
        tenantContextProvider;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    YakSecurityContext.setCurrentUser(
        resolve(request));
    try {
      filterChain.doFilter(request, response);
    } finally {
      YakSecurityContext.clear();
    }
  }

  private CurrentUser resolve(
      HttpServletRequest request) {
    HttpSession session =
        request.getSession(false);
    Long userId = null;
    String username = null;
    if (session != null) {
      Object id = session.getAttribute(
          SecuritySessionAttributes.USER_ID);
      userId = id instanceof Number
          ? ((Number) id).longValue()
          : null;
      Object name = session.getAttribute(
          SecuritySessionAttributes.USER_NAME);
      username = name instanceof String
              && StringUtils.hasText(
                  (String) name)
          ? (String) name
          : null;
    }

    boolean authenticated =
        userId != null && username != null;
    List<Long> roleIds = authenticated
        ? findRoleIds(userId)
        : Collections.emptyList();
    TenantIdentity tenantIdentity =
        authenticated
            ? findTenant(request, userId)
            : TenantIdentity.none();

    return new YakSecurityContext
        .ImmutableCurrentUser(
            userId,
            username,
            HttpRequestUtil.getProjectId(
                request),
            roleIds,
            authenticated,
            tenantIdentity);
  }

  private List<Long> findRoleIds(
      Long userId) {
    UserRoleDao userRoleDao =
        userRoleDaoProvider.getIfAvailable();
    if (userRoleDao == null) {
      return Collections.emptyList();
    }
    List<Long> roleIds =
        userRoleDao
            .selectRoleIdListByUserId(
                userId);
    return roleIds == null
        ? Collections.emptyList()
        : roleIds;
  }

  private TenantIdentity findTenant(
      HttpServletRequest request,
      Long userId) {
    if (tenantContextProvider == null) {
      return TenantIdentity.none();
    }
    TenantContextProvider provider =
        tenantContextProvider
            .getIfAvailable();
    if (provider == null) {
      return TenantIdentity.none();
    }
    TenantIdentity identity =
        provider.resolve(request, userId);
    return identity == null
        ? TenantIdentity.none()
        : identity;
  }
}
