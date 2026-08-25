package io.yak.framework.security.util;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertNull;

class HttpRequestUtilTest {

  @Test
  void clientIdentityHeadersAreNeverTrusted() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-SSO-USER", "attacker");
    request.addHeader("X-SSO-USER-ID", "999");

    assertNull(HttpRequestUtil.getOperator(request));
    assertNull(HttpRequestUtil.getOperatorId(request));
    assertNull(request.getSession(false));
  }
}
