package io.yak.framework.security.extend.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.yak.framework.security.authentication.AuthenticationManager;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class DefaultCurrentUserProviderTest {

  @Test
  void ignoresClientIdentityAndUsesAuthenticationManager() {
    AuthenticationManager manager = mock(AuthenticationManager.class);
    DefaultCurrentUserProvider provider = new DefaultCurrentUserProvider(manager);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-SSO-USER", "attacker");

    when(manager.isLogin()).thenReturn(false);
    assertNull(provider.getCurrentUser(request));

    when(manager.isLogin()).thenReturn(true);
    when(manager.getLoginUsername()).thenReturn("yak");
    assertEquals("yak", provider.getCurrentUser(request));
  }
}
