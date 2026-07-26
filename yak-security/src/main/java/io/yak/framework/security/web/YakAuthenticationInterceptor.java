package io.yak.framework.security.web;

import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.service.LoginService;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Yak Security 统一登录认证拦截器。
 *
 * <p>默认保护所有 MVC 接口，仅放行配置的公开路径、标记了
 * {@link PublicEndpoint} 的接口以及浏览器 CORS 预检请求。</p>
 */
public class YakAuthenticationInterceptor implements HandlerInterceptor {

  private final LoginService loginService;
  private final YakSecurityProperties properties;

  public YakAuthenticationInterceptor(
          LoginService loginService,
          YakSecurityProperties properties) {
    this.loginService = loginService;
    this.properties = properties;
  }

  @Override
  public boolean preHandle(
          HttpServletRequest request,
          HttpServletResponse response,
          Object handler) throws Exception {
    if (!properties.isAuthenticationEnabled()
            || "OPTIONS".equalsIgnoreCase(request.getMethod())
            || isPublicEndpoint(handler)) {
      return true;
    }

    String contextPath = request.getContextPath();
    String requestPath = request.getRequestURI();
    if (contextPath != null && !contextPath.isEmpty()
            && requestPath.startsWith(contextPath)) {
      requestPath = requestPath.substring(contextPath.length());
    }

    return loginService.interceptorCheck(
            request,
            response,
            requestPath,
            properties.getPublicPaths());
  }

  private boolean isPublicEndpoint(Object handler) {
    if (!(handler instanceof HandlerMethod)) {
      return false;
    }
    HandlerMethod method = (HandlerMethod) handler;
    return AnnotatedElementUtils.hasAnnotation(
            method.getMethod(), PublicEndpoint.class)
            || AnnotatedElementUtils.hasAnnotation(
            method.getBeanType(), PublicEndpoint.class);
  }
}
