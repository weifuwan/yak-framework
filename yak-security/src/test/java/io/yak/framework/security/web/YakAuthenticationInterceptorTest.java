package io.yak.framework.security.web;

import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.service.LoginService;
import io.yak.framework.security.service.RbacPermissionService;
import io.yak.framework.security.extend.CurrentUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class YakAuthenticationInterceptorTest {

  private LoginService loginService;
  private YakSecurityProperties properties;
  private YakAuthenticationInterceptor interceptor;
  private RbacPermissionService permissionService;
  private CurrentUserProvider currentUserProvider;

  @BeforeEach
  void setUp() {
    loginService = mock(LoginService.class);
    properties = new YakSecurityProperties();
    permissionService = mock(RbacPermissionService.class);
    currentUserProvider = mock(CurrentUserProvider.class);
    interceptor = new YakAuthenticationInterceptor(
            loginService, properties, permissionService, currentUserProvider);
  }

  @Test
  void protectedEndpointDelegatesToLoginCheck() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/app/api/private");
    request.setContextPath("/app");
    MockHttpServletResponse response = new MockHttpServletResponse();
    when(loginService.interceptorCheck(
            eq(request), eq(response), eq("/api/private"), any()))
            .thenReturn(false);

    assertFalse(interceptor.preHandle(request, response, new Object()));
    verify(loginService).interceptorCheck(
            request, response, "/api/private", properties.getPublicPaths());
  }

  @Test
  void annotatedEndpointIsPublic() throws Exception {
    HandlerMethod handler = new HandlerMethod(
            new TestController(), TestController.class.getMethod("publicApi"));

    assertTrue(interceptor.preHandle(
            new MockHttpServletRequest("GET", "/api/public"),
            new MockHttpServletResponse(), handler));
    verify(loginService, never()).interceptorCheck(any(), any(), any(), any());
  }

  @Test
  void authenticationCanBeDisabled() throws Exception {
    properties.setAuthenticationEnabled(false);
    properties.setPublicPaths(Collections.<String>emptyList());

    assertTrue(interceptor.preHandle(
            new MockHttpServletRequest("GET", "/api/private"),
            new MockHttpServletResponse(), new Object()));
    verify(loginService, never()).interceptorCheck(any(), any(), any(), any());
  }

  @Test
  void requiredPermissionAllowsAuthorizedUser() throws Exception {
    HandlerMethod handler = new HandlerMethod(
            new TestController(), TestController.class.getMethod("protectedApi"));
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    MockHttpServletResponse response = new MockHttpServletResponse();
    when(loginService.interceptorCheck(any(), any(), any(), any())).thenReturn(true);
    when(currentUserProvider.getCurrentUser(request)).thenReturn("yak");
    when(permissionService.hasPermission("yak", "user:read")).thenReturn(true);

    assertTrue(interceptor.preHandle(request, response, handler));
  }

  @Test
  void requiredPermissionReturnsUnifiedForbiddenResult() throws Exception {
    HandlerMethod handler = new HandlerMethod(
            new TestController(), TestController.class.getMethod("protectedApi"));
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/protected");
    MockHttpServletResponse response = new MockHttpServletResponse();
    when(loginService.interceptorCheck(any(), any(), any(), any())).thenReturn(true);
    when(currentUserProvider.getCurrentUser(request)).thenReturn("yak");

    assertFalse(interceptor.preHandle(request, response, handler));
    assertEquals(403, response.getStatus());
    assertEquals("application/json", response.getContentType());
    assertTrue(response.getContentAsString().contains("\"code\":3001"));
  }

  private static class TestController {
    @PublicEndpoint
    public void publicApi() {
    }

    @RequiresPermission("user:read")
    public void protectedApi() {
    }
  }
}
