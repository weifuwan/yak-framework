package io.yak.framework.security.extend.impl;

import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class DefaultLoginExtendImplTest {

  private final DefaultLoginExtendImpl loginExtend = new DefaultLoginExtendImpl(
          mock(UserService.class), mock(PasswordEncoder.class));

  @Test
  void authenticationRejectsClientIdentityHeaders() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/private");
    request.addHeader("X-SSO-USER", "attacker");
    request.addHeader("X-SSO-USER-ID", "999");
    MockHttpServletResponse response = new MockHttpServletResponse();

    assertFalse(loginExtend.interceptorCheck(
            request, response, "/private", Collections.<String>emptyList()));
    assertNull(request.getSession(false));
  }

  @Test
  void authenticationRejectsLegacyClientNamedSessionAttributes() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/private");
    request.getSession().setAttribute("X-SSO-USER", "attacker");
    request.getSession().setAttribute("X-SSO-USER-ID", 999L);
    MockHttpServletResponse response = new MockHttpServletResponse();

    assertFalse(loginExtend.interceptorCheck(
            request, response, "/private", Collections.<String>emptyList()));
  }
}
