package io.yak.framework.security.extend.impl;

import io.yak.framework.security.util.SecuritySessionAttributes;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DefaultCurrentUserProviderTest {

  private final DefaultCurrentUserProvider provider = new DefaultCurrentUserProvider();

  @Test
  void ignoresClientSuppliedIdentityHeaders() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-SSO-USER", "attacker");

    assertNull(provider.getCurrentUser(request));
  }

  @Test
  void returnsAuthenticatedSessionIdentity() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.getSession().setAttribute(SecuritySessionAttributes.USER_NAME, "yak");

    assertEquals("yak", provider.getCurrentUser(request));
  }
}
