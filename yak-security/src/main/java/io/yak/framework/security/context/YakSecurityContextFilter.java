package io.yak.framework.security.context;

import io.yak.framework.security.authentication.AuthenticationManager;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.dao.UserRoleDao;
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

/** 从服务端已验证登录态构建当前用户上下文。 */
public final class YakSecurityContextFilter extends OncePerRequestFilter {

  private final ObjectProvider<UserRoleDao> userRoleDaoProvider;
  private final ObjectProvider<AuthenticationManager> authenticationManagerProvider;
  private final YakSecurityProperties properties;

  /**
   * 兼容现有 Session 模式测试和自定义创建方式。
   */
  public YakSecurityContextFilter(
          ObjectProvider<UserRoleDao> userRoleDaoProvider) {
    this(
            userRoleDaoProvider,
            null,
            new YakSecurityProperties());
  }

  public YakSecurityContextFilter(
          ObjectProvider<UserRoleDao> userRoleDaoProvider,
          ObjectProvider<AuthenticationManager> authenticationManagerProvider,
          YakSecurityProperties properties) {
    this.userRoleDaoProvider = userRoleDaoProvider;
    this.authenticationManagerProvider = authenticationManagerProvider;
    this.properties = properties == null
            ? new YakSecurityProperties()
            : properties;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain)
          throws ServletException, IOException {
    YakSecurityContext.setCurrentUser(resolve(request));
    try {
      filterChain.doFilter(request, response);
    } finally {
      YakSecurityContext.clear();
    }
  }

  private CurrentUser resolve(HttpServletRequest request) {
    if (properties.getAuthentication().getMode()
            == YakSecurityProperties.AuthenticationMode.SATOKEN) {
      return resolveSaToken(request);
    }
    return resolveSession(request);
  }

  private CurrentUser resolveSaToken(
          HttpServletRequest request) {

    AuthenticationManager authenticationManager =
            authenticationManagerProvider == null
                    ? null
                    : authenticationManagerProvider.getIfAvailable();

    if (authenticationManager == null
            || !authenticationManager.isLogin()) {
      return buildCurrentUser(
              request,
              null,
              null);
    }

    return buildCurrentUser(
            request,
            authenticationManager.getLoginUserId(),
            authenticationManager.getLoginUsername());
  }

  private CurrentUser resolveSession(
          HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    Long userId = null;
    String username = null;
    if (session != null) {
      Object id = session.getAttribute(SecuritySessionAttributes.USER_ID);
      userId = id instanceof Number ? ((Number) id).longValue() : null;
      Object name = session.getAttribute(SecuritySessionAttributes.USER_NAME);
      username = name instanceof String && StringUtils.hasText((String) name)
              ? (String) name : null;
    }

    return buildCurrentUser(
            request,
            userId,
            username);
  }

  private CurrentUser buildCurrentUser(
          HttpServletRequest request,
          Long userId,
          String username) {

    boolean authenticated =
            userId != null
                    && StringUtils.hasText(username);

    List<Long> roleIds = authenticated
            ? findRoleIds(userId)
            : Collections.emptyList();

    return new YakSecurityContext.ImmutableCurrentUser(
            userId,
            username,
            HttpRequestUtil.getProjectId(request),
            roleIds,
            authenticated);
  }

  private List<Long> findRoleIds(Long userId) {
    UserRoleDao userRoleDao = userRoleDaoProvider.getIfAvailable();
    if (userRoleDao == null) {
      return Collections.emptyList();
    }
    List<Long> roleIds = userRoleDao.selectRoleIdListByUserId(userId);
    return roleIds == null ? Collections.emptyList() : roleIds;
  }
}
