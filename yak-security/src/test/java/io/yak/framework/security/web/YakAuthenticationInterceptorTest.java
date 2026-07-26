package io.yak.framework.security.web;

import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

  @BeforeEach
  void setUp() {
    loginService = mock(LoginService.class);
    properties = new YakSecurityProperties();
    interceptor = new YakAuthenticationInterceptor(loginService, properties);
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

  private static class TestController {
    @PublicEndpoint
    public void publicApi() {
    }
  }
}
