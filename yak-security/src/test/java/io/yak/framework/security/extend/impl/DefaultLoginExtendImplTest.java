package io.yak.framework.security.extend.impl;

import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

  @Test
  void missingAccountUsesSameErrorAsBadPassword() {
    UserService userService = mock(UserService.class);
    DefaultLoginExtendImpl extension = new DefaultLoginExtendImpl(
            userService, mock(PasswordEncoder.class));
    AccountLoginDTO login = login("unknown", "secret");

    YakSecurityException exception = assertThrows(YakSecurityException.class,
            () -> extension.verifyLogin(login,
                    new MockHttpServletRequest(), new MockHttpServletResponse()));

    assertEquals("2003-密码错误", exception.getMessage());
  }

  @Test
  void passwordChangeInvalidatesExistingSession() throws Exception {
    UserService userService = mock(UserService.class);
    PasswordEncoder encoder = mock(PasswordEncoder.class);
    User user = new User();
    user.setId(7L);
    user.setUserName("alice");
    user.setPw("hash-v1");
    when(userService.getUserByUsername("alice")).thenReturn(user);
    when(encoder.matches("secret", "hash-v1")).thenReturn(true);
    DefaultLoginExtendImpl extension = new DefaultLoginExtendImpl(userService, encoder);
    MockHttpServletRequest request = new MockHttpServletRequest();

    extension.verifyLogin(login("alice", "secret"), request,
            new MockHttpServletResponse());
    user.setPw("hash-v2");

    assertFalse(extension.interceptorCheck(request, new MockHttpServletResponse(),
            "/private", Collections.<String>emptyList()));
    assertNull(request.getSession(false));
  }

  private AccountLoginDTO login(String username, String password) {
    AccountLoginDTO login = new AccountLoginDTO();
    login.setUserName(username);
    login.setPw(password);
    return login;
  }
}
